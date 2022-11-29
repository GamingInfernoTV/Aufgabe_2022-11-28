package de.medieninformatik.client;

import de.medieninformatik.common.InvalidSeatException;
import de.medieninformatik.common.Reservation;
import de.medieninformatik.common.Seat;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;

import java.net.URI;
import java.util.Objects;
import java.util.Optional;

public class ReservationClient implements Reservation {
    private final Client client;
    private final URI baseURI;

    public ReservationClient(URI uri) {
        this.baseURI = Objects.requireNonNull(uri);
        this.client = ClientBuilder.newClient();
    }

    @Override
    public Optional<String> getReservation(Seat seat) throws InvalidSeatException {
        return Optional.empty(); // TODO: Make getReservation request
    }

    @Override
    public boolean hasReservation(Seat seat) throws InvalidSeatException {
        return false; // TODO: Make hasReservation request
    }

    @Override
    public boolean makeReservation(Seat seat, String name) throws InvalidSeatException {
        return false; // TODO: Make makeReservation request
    }
}
