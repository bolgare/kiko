package ru.bolgar.kiko.rental.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import ru.bolgar.kiko.rental.api.OwnerService;
import ru.bolgar.kiko.rental.api.TenantService;
import ru.bolgar.kiko.rental.api.ViewSlotIsNotAvailableException;
import ru.bolgar.kiko.rental.api.data.Flat;
import ru.bolgar.kiko.rental.context.RentalContext;
import ru.bolgar.kiko.rental.service.NotificationService;
import ru.bolgar.kiko.rental.service.OwnerServiceBean;
import ru.bolgar.kiko.rental.service.TenantServiceBean;
import ru.bolgar.kiko.rental.service.ViewOrderCache;
import ru.bolgar.kiko.rental.service.ViewOrderData;
import ru.bolgar.kiko.rental.service.ViewOrderDataMock;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class RentalServer implements AutoCloseable {
    protected ThreadLocal<String> user = new ThreadLocal<>();

    protected RentalContext context;

    protected NotificationService notifications;

    protected ViewOrderData data;

    protected ViewOrderCache cache;

    protected TenantService tenantService;

    protected OwnerService ownerService;

    protected Server server;

    protected ObjectMapper mapper;

    public static void main(String[] args) throws Exception {
        start(8080);
    }

    public static AutoCloseable start(int port) throws Exception {
        return new RentalServer(port);
    }

    private RentalServer(int port) throws Exception {
        this.context = () -> user.get();
        this.notifications = new NotificationServerMock();
        this.data = new ViewOrderDataMock();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                ((ViewOrderDataMock) this.data).flat(Flat.builder()
                        .id("FLAT-" + i + "." + j)
                        .name("FLAT-" + i + "." + j)
                        .ownerId("USR-" + i)
                        .build()
                );
            }
        }
        this.cache = new ViewOrderCache(data);
        this.tenantService = new TenantServiceBean(context, notifications, cache);
        this.ownerService = new OwnerServiceBean(context, notifications, cache);
        this.mapper = new ObjectMapper();
        this.server = new Server(port);

        this.server.setHandler(new AbstractHandler() {
            @Override
            public void handle(String s, Request request, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException, ServletException {
                System.out.println();
                System.out.println("Request: " + s + " ~ " + request);
                Object answer = null;
                user.set(request.getHeader("user-id"));
                if ("/tenant/flats".equals(s)) {
                    answer = tenantService.getFlats();
                } else if ("/tenant/reserve".equals(s)) {
                    try {
                        answer = tenantService.reserve(
                                request.getParameter("flat"),
                                new Date(Long.parseLong(request.getParameter("time")))
                        );
                    } catch (ViewSlotIsNotAvailableException ex) {
                        answer = ex;
                    }
                } else if ("/tenant/cancel".equals(s)) {
                    tenantService.cancel(request.getParameter("order"));
                    answer = "Ok";
                } else if ("/owner/flats".equals(s)) {
                    answer = ownerService.getFlats();
                } else {
                    //TODO Other mapping
                    //TODO I dont not understand whe i can't use spring framework
                }
                if (answer == null) {
                    return;
                }
                if (answer instanceof Exception) {
                    httpServletResponse.setStatus(400);
                }
                String data = mapper.writeValueAsString(answer);
                System.out.println("Response: " + data);
                httpServletResponse.setContentLength(data.length());
                httpServletResponse.getWriter().println(mapper.writeValueAsString(answer));
                httpServletResponse.setStatus(200);

            }
        });
        this.server.start();

    }

    @Override
    public void close() throws Exception {
        this.server.stop();
    }
}
