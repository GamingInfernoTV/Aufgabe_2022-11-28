package de.medieninformatik.client;

import de.medieninformatik.common.InvalidSeatException;
import de.medieninformatik.common.Reservation;
import de.medieninformatik.common.Seat;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;

import java.net.URI;
import java.util.Objects;
import java.util.Optional;

/**
 * Clientseitige Implementierung von {@link Reservation}
 * TODO: specify how the implementation works
 *
 * @author Malte Kasolowsky <code>m30114</code>
 * @author Aaron Pöhlmann <code>m30115</code>
 */
// TODO: remove suppression
@SuppressWarnings({"unused", "FieldCanBeLocal", "RedundantThrows"})
public class ReservationClient implements Reservation {
    private final Client client;
    private final URI baseURI;

    /**
     * TODO
     *
     * @param uri TODO
     */
    public ReservationClient(URI uri) {
        this.baseURI = Objects.requireNonNull(uri);
        this.client = ClientBuilder.newClient();
    }

    /**
     * TODO
     *
     * @param seat Der Sitz, für welchen die Reservierung abgefragt werden soll
     * @return TODO
     * @throws InvalidSeatException TODO
     */
    @Override
    public Optional<String> getReservation(Seat seat) throws InvalidSeatException {
        return Optional.empty(); // TODO: Make getReservation request
    }

    /**
     * TODO
     *
     * @param seat Der abzufragende Sitz
     * @return TODO
     * @throws InvalidSeatException TODO
     */
    @Override
    public boolean hasReservation(Seat seat) throws InvalidSeatException {
        return false; // TODO: Make hasReservation request
    }

    /**
     * TODO
     *
     * @param seat Der Sitz, welcher reserviert werden soll
     * @param name Der Name, auf den reserviert werden soll
     * @return TODO
     * @throws InvalidSeatException TODO
     */
    @Override
    public boolean makeReservation(Seat seat, String name) throws InvalidSeatException {
        return false; // TODO: Make makeReservation request
    }
}
