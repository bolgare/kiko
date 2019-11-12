package ru.bolgar.kiko.rental.service;

import ru.bolgar.kiko.rental.data.ViewOrder;

/**
 * Notification service
 */
public interface NotificationService {
    /**
     * Called on order created
     *
     * @param userId receiver
     * @param order  order info
     */
    void onOrderCreated(String userId, ViewOrder order);

    /**
     * Called on order canceled
     *
     * @param userId receiver
     * @param order  order info
     */
    void onOrderCanceled(String userId, ViewOrder order);

    /**
     * Called on order approved or rejected
     *
     * @param userId receiver
     * @param order  order info
     */
    void onOrderProcessed(String userId, ViewOrder order);
}