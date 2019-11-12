package ru.bolgar.kiko.rental.rest;

import ru.bolgar.kiko.rental.data.ViewOrder;
import ru.bolgar.kiko.rental.service.NotificationService;

class NotificationServerMock implements NotificationService {
    @Override
    public void onOrderCreated(String userId, ViewOrder order) {
        System.out.println("onOrderCreated: " + userId + ", " + order);
    }

    @Override
    public void onOrderCanceled(String userId, ViewOrder order) {
        System.out.println("onOrderCanceled: " + userId + ", " + order);
    }

    @Override
    public void onOrderProcessed(String userId, ViewOrder order) {
        System.out.println("onOrderProcessed: " + userId + ", " + order);
    }
}
