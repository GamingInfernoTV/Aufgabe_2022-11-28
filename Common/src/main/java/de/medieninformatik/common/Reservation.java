package de.medieninformatik.common;

import java.util.Optional;

public interface Reservation {
    Optional<String> getReservation(Seat seat) throws InvalidSeatException;
    boolean hasReservation(Seat seat) throws InvalidSeatException;
    boolean makeReservation(Seat seat, String name) throws InvalidSeatException;
}
