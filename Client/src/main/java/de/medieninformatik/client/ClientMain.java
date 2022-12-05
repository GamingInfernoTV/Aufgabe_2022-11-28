package de.medieninformatik.client;

import de.medieninformatik.common.InvalidSeatException;
import de.medieninformatik.common.Reservation;
import de.medieninformatik.common.Seat;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Scanner;

/**
 * Hauptklasse der clientseitigen Anwendung
 *
 * @author Malte Kasolowsky <code>m30114</code>
 */
public final class ClientMain {

    /**
     * Öffnet einen neuen {@link ReservationClient} am Pfad TODO
     * und startet ein Eingabe-Programm über die Konsole,
     * worüber Reservierungen abgefragt und erstellt werden können
     *
     * @param args die URI, an welcher der client geöffnet werden soll; optional
     */
    public static void main(String[] args) {
        var client = new ReservationClient(
                URI.create(args.length > 0
                        ? args[0]
                        : "http://localhost:8080/rest/reservation")
        );
        var scanner = new Scanner(System.in, StandardCharsets.UTF_8);
        while (true) {
            try {
                System.out.print("Enter action to execute: [ GET/g | HAS/h | MAKE/m ]: ");
                var action = Action.fromString(scanner.next());
                System.out.print(switch (action) {
                    case GET -> "Enter seat [row, num] for getting the reservation from: ";
                    case HAS -> "Enter seat [row, num] to check on existing reservation: ";
                    case MAKE -> "Enter seat [row, num] and [name] to make reservation on: ";
                    case NULL -> "No valid input, exiting" + System.lineSeparator();
                });
                if (action == Action.NULL) break;
                reservationAction(client, scanner, action);
            } catch (InvalidSeatException e) {
                System.out.println(e.getLocalizedMessage());
            }
        }
    }

    /**
     * Führt eine {@link ClientMain.Action} aus
     *
     * @param client  Der Client, für welche die Aktion ausgeführt werden soll
     * @param scanner Der Scanner über den die Eingabe läuft
     * @param action  Die Action, welche ausgeführt werden soll
     * @throws InvalidSeatException Wenn bei der Eingabe eines Sitzes
     *                              für diesen eine {@link InvalidSeatException} geworfen wurde
     */
    private static void reservationAction(
            final Reservation client,
            final Scanner scanner,
            final Action action
    ) throws InvalidSeatException {
        var seat = new Seat(scanner.nextInt(), scanner.nextInt());
        switch (action) {
            case GET -> {
                System.out.println("Getting reservation for: " + seat);
                System.out.println("Reservation: " + client.getReservation(seat).orElse("none"));
            }
            case HAS -> {
                System.out.println("Checking reservation for: " + seat);
                System.out.println(
                        client.hasReservation(seat)
                                ? "Existing reservation found"
                                : "No reservation exists"
                );
            }
            case MAKE -> {
                System.out.println("Making new reservation for: " + seat);
                System.out.println(
                        client.makeReservation(seat, scanner.next())
                                ? "New reservation has been made"
                                : "Seat already has a reservation. No new reservation has been made"
                );
            }
            case NULL -> throw new IllegalArgumentException("Action must not be null");
        }
    }

    /**
     * Aufzählungen für Aktionen, welche vom User ausgeführt werden können
     */
    private enum Action {
        GET, HAS, MAKE, NULL;

        /**
         * Liest eine Action aus einem String aus,
         * wobei dieser in Lang- oder Kurzform und beliebigen Case vorliegen kann
         *
         * @param str Der String, welcher die Action beinhaltet
         * @return Die Aktion, welche aus dem String ausgelesen wurde,
         * oder {@link Action#NULL}, wenn keine Action gelesen werden konnte
         */
        static Action fromString(String str) {
            return switch (str.toLowerCase(Locale.ROOT)) {
                case ("get"), ("g") -> GET;
                case ("has"), ("h") -> HAS;
                case ("make"), ("m") -> MAKE;
                default -> NULL;
            };
        }
    }
}
