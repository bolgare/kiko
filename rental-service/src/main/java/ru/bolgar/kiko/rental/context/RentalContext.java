package ru.bolgar.kiko.rental.context;

/**
 * Application context
 */
public interface RentalContext {
    /**
     * @return current user identifier
     */
    String getCurrentUser();
}
