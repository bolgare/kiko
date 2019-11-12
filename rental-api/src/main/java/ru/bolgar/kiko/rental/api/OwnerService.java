package ru.bolgar.kiko.rental.api;

import ru.bolgar.kiko.rental.api.data.Flat;
import ru.bolgar.kiko.rental.api.data.ViewSlot;

import java.util.List;

/**
 * Flat current tenant service
 */
public interface OwnerService {
    /**
     * @return current rental flats
     */
    List<Flat> getFlats();

    /**
     * @return current orders
     */
    List<ViewSlot> getOrders();

    /**
     * approve order
     *
     * @param orderId order identifier
     */
    void approve(String orderId);

    /**
     * reject order
     *
     * @param orderId order identifier
     */
    void reject(String orderId);
}
