package de.medieninformatik.server;

import de.medieninformatik.common.InvalidSeatException;
import de.medieninformatik.common.Reservation;
import de.medieninformatik.common.Seat;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Serverseitige Implementierung von {@link Reservation},
 * welche die Reservierung in einer unterliegenden, statischen {@link HashMap} speichert
 *
 * @author Malte Kasolowsky <code>m30114</code>
 */
public final class ReservationsImpl implements Reservation {
    private static final int NUM_OF_SEAT_ROWS = 6;
    private static final int NUM_OF_SEATS_PER_ROW = 10;
    private static final Map<Seat, String> RESERVATIONS_MAP
            = new HashMap<>(NUM_OF_SEAT_ROWS * NUM_OF_SEATS_PER_ROW);

    /**
     * Prüft, ob ein {@link Seat} in den Begrenzungen des Systems liegt,
     * genauer, ob der Seat nicht null ist und die Reihennummer kleiner als die maximale Reihennummer
     * und die Sitznummer in der Reihe kleiner als die maximale Sitznummer ist
     *
     * @param seat Der zu prüfende Sitz
     * @return Der geprüfte Seat, sofern dieser valide ist
     * @throws InvalidSeatException Wenn der Sitz nicht dem System genügt
     */
    private static Seat checkIndex(Seat seat) throws InvalidSeatException {
        try {
            if (seat == null) throw new IllegalArgumentException("seat must not be null");
            Objects.checkFromToIndex(1, seat.row(), NUM_OF_SEAT_ROWS + 1);
            Objects.checkFromToIndex(1, seat.num(), NUM_OF_SEATS_PER_ROW + 1);
        } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
            throw new InvalidSeatException(seat, e);
        }
        return seat;
    }

    /**
     * Liest den Namen, auf dem Sitz, der reserviert wurde, aus dem Speicher
     *
     * @param seat Der Sitz, für welchen die Reservierung abgefragt werden soll
     * @return Ein {@link Optional}, welches den Namen, auf den Reserviert wurde, beinhaltet,
     * sofern eine Reservierung vorliegt
     * @throws InvalidSeatException Wenn die Nummer des Sitzes nicht innerhalb des Systems liegt
     */
    @Override
    public Optional<String> getReservation(Seat seat) throws InvalidSeatException {
        return Optional.ofNullable(RESERVATIONS_MAP.get(checkIndex(seat)));
    }

    /**
     * Prüft, ob für den Sitz eine Reservierung vorliegt
     *
     * @param seat Der abzufragende Sitz
     * @return true, wenn im Speicher eine Reservierung für den Sitz gefunden wurde, sonst false
     * @throws InvalidSeatException Wenn die Nummer des Sitzes nicht innerhalb des Systems liegt
     */
    @Override
    public boolean hasReservation(Seat seat) throws InvalidSeatException {
        return RESERVATIONS_MAP.containsKey(checkIndex(seat));
    }

    /**
     * Erstellt eine neue Reservierung,
     * genauer speichert den Namen der Reservierung für den Sitz im internen Speicher,
     * sofern für den Sitz noch keine Reservierung vorliegt
     *
     * @param seat Der Sitz, welcher reserviert werden soll
     * @param name Der Name, auf den reserviert werden soll
     * @return true, wenn der Sitz erfolgreich reserviert wurde, false, wenn bereits eine Reservierung vorliegt
     * @throws InvalidSeatException Wenn die Nummer des Sitzes nicht innerhalb des Systems liegt
     */
    @Override
    public boolean makeReservation(Seat seat, String name) throws InvalidSeatException {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("name must not be null or blank");
        return Optional.ofNullable(RESERVATIONS_MAP.putIfAbsent(checkIndex(seat), name)).isEmpty();
    }

    /**
     * Gibt die unterliegende, statische {@link Map} mit den gespeicherten Reservierungen zurück
     *
     * @return Eine {@link Map},
     * welche die {@link Seat Seats} mit den Namen, auf welche sie reserviert wurden, beinhaltet
     */
    @Override
    public Map<Seat, String> getAllReservations() {
        return RESERVATIONS_MAP;
    }
}

