package de.medieninformatik.client;

import de.medieninformatik.common.InvalidSeatException;
import de.medieninformatik.common.Reservation;
import de.medieninformatik.common.Seat;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;

/**
 * Clientseitige Implementierung von {@link Reservation};
 * für jede Methode des Interfaces wird eine entsprechende Anfrage an einen Server gestellt
 * und die zurückerhaltenden Werte verarbeitet
 *
 * @author Malte Kasolowsky <code>m30114</code>
 * @author Aaron Pöhlmann <code>m30115</code>
 */
public class ReservationClient implements Reservation {
    private final Client client;
    private final URI baseURI;

    /**
     * Konstruktor; speichert die übergebene {@link URI} ab und erstellt einen neuen {@link Client}
     *
     * @param uri Die URI, über welche Anfragen an den Server gestellt werden
     */
    public ReservationClient(URI uri) {
        this.baseURI = Objects.requireNonNull(uri);
        this.client = ClientBuilder.newClient();
    }

    /**
     * Überprüft eine {@link Response} auf ihren Status; ist dieser gleich {@link Response.Status#FORBIDDEN},
     * so wird eine neue {@link InvalidSeatException} ausgehend vom übergebenen {@link Seat} geworfen,
     * da dieser Status bedeutet, dass bei der Verarbeitung der Anfrage auf dem Server ebenfalls eine
     * InvalidSeatException geworfen wurde; wird eine
     *
     * @param response Die zu überprüfende {@link Response}
     * @param seat     Der Seat, welcher Grund für die InvalidSeatException ist
     * @throws InvalidSeatException  Wenn der Server mit FORBIDDEN antwortet
     * @throws IllegalStateException Wenn der Server nicht mit einem Code zwischen 200 und 206 antwortet
     */
    private static void checkStatus(Response response, Seat seat) throws InvalidSeatException {
        if (response.getStatus() == Response.Status.FORBIDDEN.getStatusCode()) {
            throw new InvalidSeatException(seat, null);
        }
        if (response.getStatus() < Response.Status.OK.getStatusCode()
            || response.getStatus() >= Response.Status.PARTIAL_CONTENT.getStatusCode()) {
            throw new IllegalStateException("got invalid server response: " + response.getStatus());
        }
    }

    /**
     * Fragt alle Reservierungen vom Server an; da die Rückgabe ein String ist,
     * wird dieser in eine {@link TreeMap} geparst
     *
     * @return Eine {@link Map},
     * welche die {@link Seat Seats} mit den Namen, auf welche sie reserviert wurden, beinhaltet,
     * oder eine leere Map, wenn der Server mit {@link Response.Status#NO_CONTENT} antwortet
     */
    @Override
    public Map<Seat, String> getAllReservations() {
        var target = client.target(baseURI);
        try (var response = target.request().accept(MediaType.TEXT_PLAIN).get()) {
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                var responseContent = response.readEntity(String.class);
                responseContent = responseContent.replace("\r", "");
                responseContent = responseContent.replace("\n", "");
                var map = new TreeMap<Seat, String>();
                for (String reservation : responseContent.split(";")) {
                    var data = reservation.split(":");
                    map.put(Seat.fromString(data[0]), data[1].strip());
                }
                return map;
            } else if (response.getStatus() == Response.Status.NO_CONTENT.getStatusCode()) {
                return Collections.emptyMap();
            } else {
                throw new IllegalStateException("got invalid server response: " + response.getStatus());
            }
        }
    }

    /**
     * Stellt eine anfrage zum Lesen des Namens, auf dem der {@link Seat} reserviert wurde, an den Server
     *
     * @param seat Der Sitz, für welchen die Reservierung abgefragt werden soll
     * @return Der Name, auf den der Seat reserviert wurde
     * @throws InvalidSeatException Wenn ein {@link Seat} angegeben wird, welcher nicht vom Server akzeptiert wird
     */
    @Override
    public Optional<String> getReservation(Seat seat) throws InvalidSeatException {
        var target = getTarget("get", seat);
        try (var response = target.request().accept(MediaType.TEXT_PLAIN).get()) {
            checkStatus(response, seat);
            var name = response.readEntity(String.class).strip();
            return Optional.ofNullable(name.isEmpty() ? null : name);
        }
    }

    /**
     * Stellt eine Anfrage an der Server, ob der angefragt {@link Seat} bereits eine {@link Reservation} hat
     *
     * @param seat Der abzufragende Sitz
     * @return True, wenn der Sitz bereits reserviert wurde, false, wenn nicht
     * @throws InvalidSeatException Wenn ein {@link Seat} angegeben wird, welcher nicht vom Server akzeptiert wird
     */
    @Override
    public boolean hasReservation(Seat seat) throws InvalidSeatException {
        var target = getTarget("check", seat);
        try (var response = target.request().accept(MediaType.TEXT_PLAIN).get()) {
            checkStatus(response, seat);
            var responseContent = response.readEntity(String.class);
            return Boolean.parseBoolean(responseContent);
        }
    }

    /**
     * Erstellt eine neue {@link Reservation},
     * genauer speichert den Namen der Reservierung für den {@link Seat} im internen Speicher,
     * sofern für den Sitz noch keine Reservierung vorliegt
     *
     * @param seat Der Sitz, welcher reserviert werden soll
     * @param name Der Name, auf den reserviert werden soll
     * @return true, wenn die {@link Seat} reservierung erfolgreich war, false, wenn nicht
     * @throws InvalidSeatException Wenn ein {@link Seat} angegeben wird, welcher nicht vom Server akzeptiert wird
     */
    @Override
    public boolean makeReservation(Seat seat, String name) throws InvalidSeatException {
        var target = getTarget("make", seat);
        var entity = Entity.entity(name, MediaType.TEXT_PLAIN);
        try (var response = target.request().accept(MediaType.TEXT_PLAIN).post(entity)) {
            checkStatus(response, seat);
            var responseContent = response.readEntity(String.class);
            return Boolean.parseBoolean(responseContent);
        }
    }

    /**
     * Erstellt die {@link URI} zu dem zu überprüfenden {@link Seat}
     *
     * @param path Teil der URI welcher zwischen den einzelnen Methoden unterscheidet
     * @param seat Teil der URI welcher zu dem gewünschten Sitz führt
     * @return URI, welche die gewünschte Methode zu dem gewünschten Sitz führt
     */
    private WebTarget getTarget(String path, Seat seat) {
        var uri = UriBuilder.fromUri(baseURI)
                .path(path)
                .queryParam("row", seat.row())
                .queryParam("num", seat.num())
                .build();
        return client.target(uri);
    }
}
