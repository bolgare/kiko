package ru.bolgar.kiko.rental.service;

import ru.bolgar.kiko.rental.api.ViewSlotIsNotAvailableException;
import ru.bolgar.kiko.rental.api.data.Flat;
import ru.bolgar.kiko.rental.api.data.ViewSlotState;
import ru.bolgar.kiko.rental.data.ViewOrder;

import java.util.Date;
import java.util.List;

/**
 * View order data access layer
 * TODO getFlat and getFlats method must be in FlatData
 */
public interface ViewOrderData {
    /**
     * Receive flat by id
     *
     * @param id identifier
     * @return flat or <code>null</code>
     */
    Flat getFlat(String id);

    /**
     * Receive all flats
     * //todo some filter
     *
     * @return all flats
     */
    List<Flat> getFlats();

    /**
     * Receive flats where owner is user
     *
     * @param userId user identifier
     * @return user's flats
     */
    List<Flat> getFlats(String userId);

    /**
     * Receive reserved slots
     *
     * @param flatId flat identifier
     * @param from   filter date 'from'
     * @param until  filter date 'until'
     * @return existing orders
     */
    List<ViewOrder> getReservedSlots(String flatId, Date from, Date until);

    /**
     * Receive user's orders
     *
     * @param userId user identifier
     * @return user's orders
     */
    List<ViewOrder> getOrders(String userId);

    /**
     * Receive orders waiting decision
     *
     * @param userId user identifier (current tenant (flat owner) for order)
     * @return orders waiting decision
     */
    List<ViewOrder> getWaitingOrders(String userId);

    /**
     * Create order
     *
     * @param order order
     * @return merged order
     * @throws ViewSlotIsNotAvailableException where slot is not available
     */
    ViewOrder create(ViewOrder order) throws ViewSlotIsNotAvailableException;

    /**
     * Get order by identifier
     *
     * @param id identifier
     * @return order or <code>null</code>
     */
    ViewOrder getOrder(String id);

    /**
     * Remove order from DataBase
     *
     * @param order order
     */
    void remove(ViewOrder order);

    /**
     * Change order state
     *
     * @param order order
     * @param state new order state
     */
    void setOrderState(ViewOrder order, ViewSlotState state);

}
