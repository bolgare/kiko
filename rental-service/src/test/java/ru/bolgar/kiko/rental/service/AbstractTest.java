package ru.bolgar.kiko.rental.service;

import org.mockito.Mock;
import ru.bolgar.kiko.rental.api.data.Flat;
import ru.bolgar.kiko.rental.context.RentalContext;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.mockito.Mockito.when;

/**
 * Abstract test
 */
public class AbstractTest {
    @Mock
    protected RentalContext context;

    @Mock
    protected NotificationService notifications;

    protected ViewOrderDataMock data;

    protected ViewOrderCache cache;

    public void before() {
        data = new ViewOrderDataMock();
        cache = new ViewOrderCache(data);
        data.flat(
                Flat.builder().id("1").name("1").ownerId("001").build(),
                Flat.builder().id("2").name("2").ownerId("001").build(),
                Flat.builder().id("3").name("3").ownerId("002").build(),
                Flat.builder().id("4").name("4").ownerId("002").build()
        );
        when(context.getCurrentUser()).thenReturn("003");
    }

    /**
     * Receive current date with defined time
     *
     * @param time time in format HH:mm
     * @return date
     */
    protected Date toDate(String time) {
        try {
            return new SimpleDateFormat("dd.MM.yyyy HH:mm").parse(new SimpleDateFormat("dd.MM.yyyy").format(new Date()) + " " + time);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}
