package de.medieninformatik.common;

import java.util.Map;
import java.util.Optional;

/**
 * Schnittstelle für das Abfragen und Erstellen von Reservierungen
 *
 * @author Malte Kasolowsky <code>m30114</code>
 */
public interface Reservation {

    /**
     * Methode zur Abfrage aller gespeicherten Reservierungen
     *
     * @return Eine {@link Map},
     * welche die {@link Seat Seats} mit den Namen, auf welche sie reserviert wurden, beinhaltet
     */
    Map<Seat, String> getAllReservations();

    /**
     * Methode zum Abfragen des Namens, auf den ein {@link Seat} reserviert wurde
     *
     * @param seat Der Sitz, für welchen die Reservierung abgefragt werden soll
     * @return Ein {@link Optional}, welches bei vorhandener Reservierung den Namen,
     * auf den reserviert wurde, beinhaltet
     * @throws InvalidSeatException Wenn der übergebene Seat nicht im System liegt
     */
    Optional<String> getReservation(Seat seat) throws InvalidSeatException;

    /**
     * Methode zum Abfragen, ob ein Sitz reserviert wurde
     *
     * @param seat Der abzufragende Sitz
     * @return true, wenn der Sitz eine Reservierung besitz, sonst false
     * @throws InvalidSeatException Wenn der übergebene Seat nicht im System liegt
     */
    boolean hasReservation(Seat seat) throws InvalidSeatException;

    /**
     * Methode zum Erstellen einer Reservierung auf einen Namen
     *
     * @param seat Der Sitz, welcher reserviert werden soll
     * @param name Der Name, auf den reserviert werden soll
     * @return true, wenn der Sitz reserviert wurde,
     * false, wenn für den Sitz bereits eine Reservierung vorliegt
     * @throws InvalidSeatException Wenn der übergebene Seat nicht im System liegt
     */
    boolean makeReservation(Seat seat, String name) throws InvalidSeatException;
}
