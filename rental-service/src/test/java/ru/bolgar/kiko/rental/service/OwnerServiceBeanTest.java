package ru.bolgar.kiko.rental.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import ru.bolgar.kiko.rental.api.OwnerService;
import ru.bolgar.kiko.rental.api.data.Flat;
import ru.bolgar.kiko.rental.api.data.ViewSlot;
import ru.bolgar.kiko.rental.api.data.ViewSlotState;
import ru.bolgar.kiko.rental.data.ViewOrder;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * Test for OwnerServiceBean
 */
@RunWith(MockitoJUnitRunner.class)
public class OwnerServiceBeanTest extends AbstractTest {
    private OwnerService service;

    private ViewOrder.ViewOrderBuilder order;

    @Before
    @Override
    public void before() {
        super.before();
        service = new OwnerServiceBean(context, notifications, cache);
        order = ViewOrder.builder()
                .id("X001")
                .flatId("1")
                .tenantId("003")
                .state(ViewSlotState.WAITING)
                .time(toDate("13:00"));
        data.order(order.build(),
                ViewOrder.builder()
                        .id("X002")
                        .flatId("1")
                        .tenantId("004")
                        .state(ViewSlotState.WAITING)
                        .time(toDate("15:00"))
                        .build()
        );
        when(context.getCurrentUser()).thenReturn("001");
    }

    @Test
    public void getFlatsTest() {
        List<Flat> list = service.getFlats();
        Assert.assertEquals(2, list.size());
        Set<String> set = list.stream().map(Flat::getId).collect(Collectors.toSet());
        Assert.assertFalse(set.add("1"));
        Assert.assertFalse(set.add("2"));
    }

    @Test
    public void getOrdersTest() {
        List<ViewSlot> orders = service.getOrders();
        Assert.assertEquals(2, orders.size());
        Set<String> set = orders.stream().map(ViewSlot::getId).collect(Collectors.toSet());
        Assert.assertFalse(set.add("X001"));
        Assert.assertFalse(set.add("X002"));
    }

    @Test
    public void approveTest() {
        service.approve("X001");
        Assert.assertEquals(ViewSlotState.APPROVED, data.getOrder("X001").getState());
        verify(notifications).onOrderProcessed("003", order.state(ViewSlotState.APPROVED).build());
    }

    @Test
    public void rejectTest() {
        service.reject("X001");
        Assert.assertEquals(ViewSlotState.REJECTED, data.getOrder("X001").getState());
        verify(notifications).onOrderProcessed("003", order.state(ViewSlotState.REJECTED).build());
    }
}