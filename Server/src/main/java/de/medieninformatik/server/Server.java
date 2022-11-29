package de.medieninformatik.server;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Server {

    private Server() {
    }

    public static void start(String uri) {
        try {
            Logger.getLogger("org.glassfish").setLevel(Level.ALL);
            URI baseURI = new URI(uri);
            ResourceConfig config = ResourceConfig.forApplicationClass(ReservationsApplication.class);
            HttpServer server = GrizzlyHttpServerFactory.createHttpServer(baseURI, config);
            if (!server.isStarted()) server.start();
            System.out.println(uri.replace("0.0.0.0", "localhost"));
            System.out.println("Any input will stop the server");
            //noinspection ResultOfMethodCallIgnored
            System.in.read();
            server.shutdown();
        } catch (IOException | URISyntaxException e) {
            // TODO: Exception handling
        }
    }
}
