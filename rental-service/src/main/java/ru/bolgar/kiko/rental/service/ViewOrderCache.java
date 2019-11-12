package ru.bolgar.kiko.rental.service;

import ru.bolgar.kiko.rental.api.ViewSlotIsNotAvailableException;
import ru.bolgar.kiko.rental.api.data.Flat;
import ru.bolgar.kiko.rental.api.data.ViewSlotState;
import ru.bolgar.kiko.rental.data.ViewOrder;

import java.util.Date;
import java.util.List;

/**
 * Cached wrapper of ViewOrderData (ViewOrder data access level)
 */
public class ViewOrderCache {

    private ViewOrderData data;

    public ViewOrderCache(ViewOrderData data) {
        this.data = data;
    }

    public Flat getFlat(String id) {
        return data.getFlat(id);
    }

    public List<Flat> getFlats() {
        return data.getFlats();
    }

    public List<Flat> getFlats(String userId) {
        return data.getFlats(userId);
    }

    public List<ViewOrder> getReservedSlots(String flatId, Date from, Date until) {
        return data.getReservedSlots(flatId, from, until);
    }

    public ViewOrder getOrder(String id) {
        return data.getOrder(id);
    }

    public List<ViewOrder> getOrders(String userId) {
        return data.getOrders(userId);
    }

    public List<ViewOrder> getWaitingOrders(String userId) {
        return data.getWaitingOrders(userId);
    }

    public ViewOrder reserve(String userId, String flatId, Date time) throws ViewSlotIsNotAvailableException {
        ViewOrder order = new ViewOrder();
        order.setTenantId(userId);
        order.setFlatId(flatId);
        order.setTime(time);
        order.setState(ViewSlotState.WAITING);
        return data.create(order);
    }

    public void remove(ViewOrder order) {
        data.remove(order);
    }

    public void setOrderState(ViewOrder order, ViewSlotState state) {
        data.setOrderState(order, state);
    }
}
