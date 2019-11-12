package ru.bolgar.kiko.rental.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import ru.bolgar.kiko.rental.api.ViewSlotIsNotAvailableException;
import ru.bolgar.kiko.rental.api.data.Flat;
import ru.bolgar.kiko.rental.api.data.ViewSlot;
import ru.bolgar.kiko.rental.data.ViewOrder;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.mockito.Mockito.verify;
import static ru.bolgar.kiko.rental.utilities.CollectionUtils.asHashMap;

/**
 * Tests for TenantServiceBean
 */
@RunWith(MockitoJUnitRunner.class)
public class TenantServiceBeanTest extends AbstractTest {
    private TenantServiceBean service;

    @Before
    public void before() {
        super.before();
        service = new TenantServiceBean(context, notifications, cache);
    }

    @Test
    public void getFlatsTest() {
        List<Flat> list = service.getFlats();
        Assert.assertEquals(4, list.size());
        Assert.assertEquals(4, asHashMap(list, Flat::getId).size());
    }

    @Test
    public void getViewSlotsTest() {
        data.order(ViewOrder.builder()
                        .id(UUID.randomUUID().toString())
                        .flatId("1").time(toDate("11:00"))
                        .build(),
                ViewOrder.builder()
                        .id(UUID.randomUUID().toString())
                        .flatId("1").time(toDate("13:40"))
                        .build()
        );
        List<Date> list = service.getViewSlots("1", toDate("00:00"), toDate("24:00"));
        Assert.assertEquals(24 * 3 - 2, list.size());
        Assert.assertEquals(24 * 3 - 2, new HashSet<>(list).size());
        Assert.assertFalse(isVisible(list, "11:00"));
        Assert.assertFalse(isVisible(list, "13:40"));
    }

    @Test
    public void reserveTest() throws ViewSlotIsNotAvailableException {
        Assert.assertTrue(isVisible("1", "12:00"));
        String order = service.reserve("1", toDate("12:00"));
        Assert.assertNotNull(order);
        Assert.assertFalse(isVisible("1", "12:00"));
        try {
            service.reserve("1", toDate("12:00"));
            Assert.fail();
        } catch (ViewSlotIsNotAvailableException ex) {
            //ok
        }
        verify(notifications).onOrderCreated("001", ViewOrder.builder().id(order).flatId("1").tenantId("003").time(toDate("12:00")).build());
    }

    @Test
    public void cancelTest() throws Exception {
        Assert.assertTrue(isVisible("1", "12:00"));
        String order = service.reserve("1", toDate("12:00"));
        Assert.assertNotNull(order);
        Assert.assertFalse(isVisible("1", "12:00"));
        service.cancel(order);
        Assert.assertTrue(isVisible("1", "12:00"));
        verify(notifications).onOrderCanceled("001", ViewOrder.builder().id(order).flatId("1").tenantId("003").time(toDate("12:00")).build());
    }

    @Test
    public void getOrdersTest() throws Exception {
        String a = service.reserve("1", toDate("12:00"));
        String b = service.reserve("1", toDate("13:00"));
        Set<Date> orders = service.getOrders().stream().map(ViewSlot::getTime).collect(Collectors.toSet());
        Assert.assertEquals(2, orders.size());
        Assert.assertFalse(orders.add(toDate("12:00")));
        Assert.assertFalse(orders.add(toDate("13:00")));
    }

    private boolean isVisible(String flat, String time) {
        List<Date> list = service.getViewSlots(flat, toDate(time), toDate("23:59"));
        return isVisible(list, time);
    }

    private boolean isVisible(List<Date> list, String time) {
        Date date = toDate(time);
        return list.contains(date);
    }

}
