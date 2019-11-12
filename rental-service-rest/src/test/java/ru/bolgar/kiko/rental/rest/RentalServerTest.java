package ru.bolgar.kiko.rental.rest;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Ignore;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RentalServerTest {
    private int port = 19080;

    private HttpClient client = HttpClientBuilder.create().build();

    @Test
    @Ignore
    public void testIt() throws Exception {
        try (AutoCloseable ignore = RentalServer.start(port)) {
            System.out.println(">>> " + get(get(port, "USR-1", "tenant/flats")));
            System.out.println(">>> " + get(get(port, "USR-1", "owner/flats")));
            System.out.println(">>> " + get(get(port, "USR-1", "tenant/reserve",
                    "flat", "FLAT-1.1",
                    "time", Long.toString(toDate("12:00").getTime())))
            );
        }
    }

    private String get(HttpResponse response) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (InputStream input = response.getEntity().getContent()) {
            byte[] b = new byte[1024];
            int c;
            while ((c = input.read(b)) > -1) out.write(b, 0, c);
        }
        return out.toString();
    }

    private HttpResponse get(int port, String user, String path, String... args) throws Exception {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < args.length; i = i + 2) {
            if (str.length() > 0) str.append("&");
            str.append(args[i]).append("=").append(args[i + 1]);
        }
        HttpGet get = new HttpGet("http://localhost:" + port + "/" + path + "?" + str);
        get.setHeader("user-id", user);
        return client.execute(get);
    }

    protected Date toDate(String time) {
        try {
            return new SimpleDateFormat("dd.MM.yyyy HH:mm").parse(new SimpleDateFormat("dd.MM.yyyy").format(new Date()) + " " + time);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
