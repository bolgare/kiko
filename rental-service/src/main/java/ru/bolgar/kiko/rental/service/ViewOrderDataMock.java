package ru.bolgar.kiko.rental.service;

import ru.bolgar.kiko.rental.api.ViewSlotIsNotAvailableException;
import ru.bolgar.kiko.rental.api.data.Flat;
import ru.bolgar.kiko.rental.api.data.ViewSlotState;
import ru.bolgar.kiko.rental.data.ViewOrder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Mock realization for ViewOrderData
 * Service is not 100% thread safe
 */
public class ViewOrderDataMock implements ViewOrderData {
    private List<Flat> flatList = new ArrayList<>();

    private Map<String, List<Flat>> flatMap = new HashMap<>();

    private Map<String, ViewOrder> orders = new ConcurrentHashMap<>();

    /**
     * Add flat to data storage
     *
     * @param flats flat collection
     *              METHOD only for initialization
     */
    public void flat(Flat... flats) {
        flatList.addAll(Arrays.asList(flats));
        for (Flat flat : flats) {
            flatMap.computeIfAbsent(flat.getOwnerId(), u -> new ArrayList<>()).add(flat);
        }
    }

    /**
     * Add order to data storage
     *
     * @param orders order collection
     *               METHOD only for initialization
     */
    public void order(ViewOrder... orders) {
        for (ViewOrder order : orders) {
            this.orders.put(order.getId(), order);
        }
    }

    @Override
    public Flat getFlat(String id) {
        return flatList.stream().filter(f -> id.equals(f.getId())).findAny().orElse(null);
    }

    @Override
    public List<Flat> getFlats() {
        return flatList;
    }

    @Override
    public List<Flat> getFlats(String userId) {
        return flatMap.get(userId);
    }

    @Override
    public List<ViewOrder> getReservedSlots(String flatId, Date from, Date until) {
        return orders.values().stream().filter(order -> order.getFlatId().equals(flatId)).collect(Collectors.toList());
    }

    @Override
    public List<ViewOrder> getOrders(String userId) {
        return orders.values().stream().filter(order -> userId.equals(order.getTenantId())).collect(Collectors.toList());

    }

    @Override
    public List<ViewOrder> getWaitingOrders(String userId) {
        Set<String> flats = flatMap.get(userId).stream().map(Flat::getId).collect(Collectors.toSet());
        return orders.values().stream().filter(order -> order.getState() == ViewSlotState.WAITING && flats.contains(order.getFlatId())).collect(Collectors.toList());
    }

    @Override
    public ViewOrder create(ViewOrder order) throws ViewSlotIsNotAvailableException {
        if (orders.values().stream().anyMatch(o -> o.getFlatId().equals(order.getFlatId()) && o.getTime().equals(order.getTime()))) {
            throw new ViewSlotIsNotAvailableException();
        }
        order.setId(UUID.randomUUID().toString());
        orders.put(order.getId(), order);
        return order;
    }

    @Override
    public ViewOrder getOrder(String id) {
        return orders.get(id);
    }

    @Override
    public void remove(ViewOrder order) {
        orders.remove(order.getId());
    }

    @Override
    public void setOrderState(ViewOrder order, ViewSlotState state) {
        order = getOrder(order.getId());
        if (order != null) order.setState(state);
    }
}
