package ru.bolgar.kiko.rental.service;

import ru.bolgar.kiko.rental.api.OwnerService;
import ru.bolgar.kiko.rental.api.data.Flat;
import ru.bolgar.kiko.rental.api.data.ViewSlot;
import ru.bolgar.kiko.rental.api.data.ViewSlotState;
import ru.bolgar.kiko.rental.context.RentalContext;
import ru.bolgar.kiko.rental.data.ViewOrder;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of OwnerService
 */
public class OwnerServiceBean implements OwnerService {
    private RentalContext context;

    private NotificationService notifications;

    private ViewOrderCache cache;

    public OwnerServiceBean(RentalContext context,
                            NotificationService notifications,
                            ViewOrderCache cache) {
        this.context = context;
        this.notifications = notifications;
        this.cache = cache;
    }

    @Override
    public List<Flat> getFlats() {
        return cache.getFlats(context.getCurrentUser());
    }

    @Override
    public List<ViewSlot> getOrders() {
        return cache.getWaitingOrders(context.getCurrentUser()).stream().map(ViewOrder::asViewSlot).collect(Collectors.toList());
    }

    @Override
    public void approve(String orderId) {
        ViewOrder order = cache.getOrder(orderId);
        if (order == null) return;
        if (order.getState() != ViewSlotState.WAITING) {
            throw new IllegalStateException();
        }
        cache.setOrderState(order, ViewSlotState.APPROVED);
        notifications.onOrderProcessed(order.getTenantId(), order);
    }

    @Override
    public void reject(String orderId) {
        ViewOrder order = cache.getOrder(orderId);
        if (order == null) return;
        if (order.getState() != ViewSlotState.WAITING) {
            throw new IllegalStateException();
        }
        cache.setOrderState(order, ViewSlotState.REJECTED);
        notifications.onOrderProcessed(order.getTenantId(), order);
    }
}
