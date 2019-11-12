package ru.bolgar.kiko.rental.service;

import ru.bolgar.kiko.rental.api.TenantService;
import ru.bolgar.kiko.rental.api.ViewSlotIsNotAvailableException;
import ru.bolgar.kiko.rental.api.data.Flat;
import ru.bolgar.kiko.rental.api.data.ViewSlot;
import ru.bolgar.kiko.rental.api.data.ViewSlotState;
import ru.bolgar.kiko.rental.context.RentalContext;
import ru.bolgar.kiko.rental.data.ViewOrder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.bolgar.kiko.rental.utilities.CollectionUtils.asHashMap;
import static ru.bolgar.kiko.rental.utilities.DateUtils.trunc;

/**
 * Implementation of TenantService
 */
public class TenantServiceBean implements TenantService {
    private final RentalContext context;

    private final NotificationService notifications;

    private final ViewOrderCache cache;

    public TenantServiceBean(RentalContext context, NotificationService notifications, ViewOrderCache cache) {
        this.context = context;
        this.notifications = notifications;
        this.cache = cache;
    }

    public List<Flat> getFlats() {
        return cache.getFlats();
    }

    public List<Date> getViewSlots(String flatId, Date from, Date until) {
        Set<Date> reserved = asHashMap(cache.getReservedSlots(flatId, from, until), ViewOrder::getTime).keySet();
        List<Date> slots = new ArrayList<>();
        Calendar calendar = trunc(from);
        while (true) {
            Date begin = calendar.getTime();
            calendar.add(Calendar.MINUTE, ViewSlot.SIZE);
            if (begin.before(from)) continue;
            if (reserved.contains(begin)) continue;
            Date finish = calendar.getTime();
            if (finish.after(until)) break;
            slots.add(begin);
        }
        return slots;
    }

    public String reserve(String flatId, Date from) throws ViewSlotIsNotAvailableException {
        ViewOrder order = cache.reserve(context.getCurrentUser(), flatId, from);
        notifications.onOrderCreated(cache.getFlat(order.getFlatId()).getOwnerId(), order);
        return order.getId();
    }

    @Override
    public void cancel(String orderId) {
        ViewOrder order = cache.getOrder(orderId);
        if (order == null) return;
        if (!context.getCurrentUser().equals(order.getTenantId())) return;
        if (order.getState() == ViewSlotState.APPROVED) {
            throw new IllegalStateException("Order was approved");
        }
        if (order.getState() == ViewSlotState.WAITING) {
            cache.remove(order);
            notifications.onOrderCanceled(cache.getFlat(order.getFlatId()).getOwnerId(), order);
        }
    }

    @Override
    public List<ViewSlot> getOrders() {
        return cache.getOrders(context.getCurrentUser()).stream().map(ViewOrder::asViewSlot).collect(Collectors.toList());
    }
}
