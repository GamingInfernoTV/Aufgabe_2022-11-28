package de.medieninformatik.server;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility-Klasse zum Starten des Rest-Servers
 *
 * @author Malte Kasolowsky <code>m30114</code>
 */
public final class Server {
    private static final Logger LOGGER = Logger.getLogger("org.glassfish");

    /**
     * Privater Konstruktor; da keine Instanziierung erlaubt ist
     */
    private Server() {
    }

    /**
     * Startet den Rest-Server an der angegebenen {@link URI}
     *
     * @param uri Eine URI als Zeichenkette
     */
    public static void start(String uri) {
        try {
            LOGGER.setLevel(Level.ALL);
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
            LOGGER.log(Level.SEVERE, "Exception thrown during server start", e);
        }
    }
}
