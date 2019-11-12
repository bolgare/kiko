package ru.bolgar.kiko.rental.api;

import ru.bolgar.kiko.rental.api.data.Flat;
import ru.bolgar.kiko.rental.api.data.ViewSlot;

import java.util.Date;
import java.util.List;

/**
 * Service for future tenant
 */
public interface TenantService {
    /**
     * @return available flats
     */
    List<Flat> getFlats(/*some filter*/);

    /**
     * get available slots for flat
     *
     * @param flatId flat identifier
     * @param from   filter date 'time'
     * @param until  filter date 'until'
     * @return available slots
     */
    List<Date> getViewSlots(String flatId, Date from, Date until);

    /**
     * Create order for view slot
     *
     * @param flatId flat identifier
     * @param time   time of view
     * @return order identifier
     * @throws ViewSlotIsNotAvailableException when slot already busy
     */
    String reserve(String flatId, Date time) throws ViewSlotIsNotAvailableException;

    /**
     * Cancel order
     *
     * @param orderId order identifier
     */
    void cancel(String orderId);

    /**
     * @return current user orders
     */
    List<ViewSlot> getOrders();
}
