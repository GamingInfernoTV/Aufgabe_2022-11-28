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
import java.util.Objects;
import java.util.Optional;

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
     * TODO
     *
     * @param seat Der Sitz, für welchen die Reservierung abgefragt werden soll
     * @return Der Name, auf den der Seat reserviert wurde
     * @throws InvalidSeatException Wenn ein {@link Seat} angegeben wird, welcher nicht vom Server akzeptiert wird
     */
    @Override
    public Optional<String> getReservation(Seat seat) throws InvalidSeatException {
        var target = getTarget("get", seat);
        try (Response response = target.request().accept(MediaType.TEXT_PLAIN).get()) {
            checkStatus(response, seat);
            var name = response.readEntity(String.class);
            return Optional.ofNullable(name.isEmpty() ? null : name);
        }
    }

    /**
     * TODO
     *
     * @param seat Der abzufragende Sitz
     * @return TODO
     * @throws InvalidSeatException Wenn ein {@link Seat} angegeben wird, welcher nicht vom Server akzeptiert wird
     */
    @Override
    public boolean hasReservation(Seat seat) throws InvalidSeatException {
        var target = getTarget("check", seat);
        try (Response response = target.request().accept(MediaType.TEXT_PLAIN).get()) {
            checkStatus(response, seat);
            var responseContent = response.readEntity(String.class);
            return Boolean.parseBoolean(responseContent);
        }
    }

    /**
     * TODO
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
        try (Response response = target.request().post(entity)) {
            checkStatus(response, seat);
            var responseContent = response.readEntity(String.class);
            return Boolean.parseBoolean(responseContent);
        }
    }

    /**
     * Überprüft eine {@link Response} auf ihren Status; ist dieser gleich {@link Response.Status#FORBIDDEN},
     * so wird eine neue {@link InvalidSeatException} ausgehend vom übergebenen {@link Seat} geworfen,
     * da dieser Status bedeutet, dass bei der Verarbeitung der Anfrage auf dem Server ebenfalls eine
     * InvalidSeatException geworfen wurde
     *
     * @param response Die zu überprüfende {@link Response}
     * @param seat Der Seat, welcher Grund für die InvalidSeatException ist
     */
    private void checkStatus(Response response, Seat seat) throws InvalidSeatException {
        if (response.getStatus() == Response.Status.FORBIDDEN.getStatusCode()) {
            throw new InvalidSeatException(seat, null);
        }
    }

    /**
     * TODO
     *
     * @param path TODO
     * @param seat TODO
     * @return TODO
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
