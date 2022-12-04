package de.medieninformatik.server;

/**
 * Startet den Server mittels {@link Server#start(String)} am Pfad TODO
 *
 * @author Malte Kasolowsky <code>m30114</code>
 */
public class ServerMain {
    public static void main(String[] args) {
        Server.start("http://0.0.0.0:8080/rest"); // TODO: URI
    }
}
