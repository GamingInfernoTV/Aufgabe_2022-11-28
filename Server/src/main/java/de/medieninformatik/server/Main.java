package de.medieninformatik.server;

/**
 * Startet den Server mittels {@link Server#start(String)} am Pfad TODO
 *
 * @author Malte Kasolowsky <code>m30114</code>
 */
public class Main {
    /**
     * Ruft {@link Server#start(String)}
     * mit der URI <a href="http://0.0.0.0:8080/rest">http://0.0.0.0:8080/rest</a> auf
     *
     * @param args nicht benutzt
     */
    public static void main(String[] args) {
        Server.start("http://0.0.0.0:8080/rest");
    }
}
