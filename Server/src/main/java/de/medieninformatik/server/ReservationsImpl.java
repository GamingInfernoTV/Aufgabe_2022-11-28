package de.medieninformatik.server;

import de.medieninformatik.common.InvalidSeatException;
import de.medieninformatik.common.Reservation;
import de.medieninformatik.common.Seat;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class ReservationsImpl implements Reservation {
    private static final int NUM_OF_SEAT_ROWS = 6;
    private static final int NUM_OF_SEATS_PER_ROW = 10;
    private static final Map<Seat, String> RESERVATIONS_MAP
            = new HashMap<>(NUM_OF_SEAT_ROWS * NUM_OF_SEATS_PER_ROW);
    @Override
    public Optional<String> getReservation(Seat seat) throws InvalidSeatException {
        return Optional.ofNullable(RESERVATIONS_MAP.get(checkIndex(seat)));
    }

    @Override
    public boolean hasReservation(Seat seat) throws InvalidSeatException {
        return RESERVATIONS_MAP.containsKey(checkIndex(seat));
    }

    @Override
    public boolean makeReservation(Seat seat, String name) throws InvalidSeatException {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("name must not be null or blank");
        return Optional.ofNullable(RESERVATIONS_MAP.putIfAbsent(checkIndex(seat), name)).isEmpty();
    }

    private static Seat checkIndex(Seat seat) throws InvalidSeatException {
        try {
            if (seat == null) throw new IllegalArgumentException("seat must not be null");
            Objects.checkIndex(seat.row(), NUM_OF_SEAT_ROWS);
            Objects.checkIndex(seat.num(), NUM_OF_SEATS_PER_ROW);
        } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
            throw new InvalidSeatException(seat, e);
        }
        return seat;
    }
}

