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
public final class Main {

    /**
     * Öffnet einen neuen {@link ReservationClient}
     * am Pfad <a href="http://localhost:8080/rest/reservation">http://localhost:8080/rest/reservation</a>
     * und startet ein Eingabe-Programm über die Konsole,
     * worüber Reservierungen abgefragt und erstellt werden können
     *
     * @param args nicht benutzt
     */
    public static void main(String[] args) {
        var client = new ReservationClient(URI.create("http://localhost:8080/rest/reservation"));
        var scanner = new Scanner(System.in, StandardCharsets.UTF_8);
        while (true) {
            try {
                System.out.print("Enter action to execute [ ALL/a | GET/g | HAS/h | MAKE/m ]: ");
                var action = Action.fromString(scanner.next());
                System.out.print(switch (action) {
                    case ALL -> "";
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
     * Führt eine {@link Main.Action} aus
     *
     * @param client  Der Client, für welche die Aktion ausgeführt werden soll
     * @param scanner Der Scanner über den die Eingabe läuft
     * @param action  Die Action, welche ausgeführt werden soll
     * @throws InvalidSeatException     Wenn bei der Eingabe eines Sitzes
     *                                  für diesen eine {@link InvalidSeatException} geworfen wurde
     * @throws IllegalArgumentException Wenn die Action nicht gleich {@link Action#ALL}, {@link Action#GET},
     *                                  {@link Action#HAS} oder {@link Action#MAKE} ist
     */
    private static void reservationAction(
            final Reservation client,
            final Scanner scanner,
            final Action action
    ) throws InvalidSeatException {
        var seat = action == Action.ALL ? null : new Seat(scanner.nextInt(), scanner.nextInt());
        switch (action) {
            case ALL -> {
                System.out.println("Getting all reservations: ");
                var map = client.getAllReservations();
                if (map.isEmpty()) System.out.println("No reservations have been made yet");
                else map.forEach((Seat s, String name) -> System.out.println(s + ": " + name));
            }
            case GET -> {
                System.out.println("Getting reservation for: " + seat);
                System.out.println("Reservation: " + client.getReservation(seat).orElse("none"));
            }
            case HAS -> {
                System.out.println("Checking reservation for: " + seat);
                System.out.printf("Found %s existing reservation%n", client.hasReservation(seat) ? "an" : "no");
            }
            case MAKE -> makeReservation(client, scanner, seat);
            default -> throw new IllegalArgumentException("Action must not be null");
        }
    }

    /**
     * Liest eine Zeile vom {@link Scanner} ein und ruft {@link Reservation#makeReservation(Seat, String)} auf;
     * das Resultat der Reservierung wird in der Konsole ausgegeben
     *
     * @param client Ein Objekt von {@link Reservation}, über welches die Reservierung gemacht wird
     * @param scanner Ein Scanner, über den der Name, auf welchen reserviert werden soll, eingeben wird
     * @param seat Der {@link Seat}, der reserviert werden soll
     * @throws InvalidSeatException Wenn {@link Reservation#makeReservation(Seat, String)} diese Ausnahme wirft
     */
    private static void makeReservation(Reservation client, Scanner scanner, Seat seat) throws InvalidSeatException {
        var name = scanner.nextLine().strip();
        if (name.isBlank()) {
            System.out.println("Name mut not be empty or blank");
            return;
        }
        System.out.println("Making new reservation for: " + seat);
        System.out.println(
                client.makeReservation(seat, name)
                        ? "New reservation has been made"
                        : "Seat already has a reservation. No new reservation has been made"
        );
    }

    /**
     * Aufzählungen für Aktionen, welche vom User ausgeführt werden können
     */
    private enum Action {
        ALL, GET, HAS, MAKE, NULL;

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
                case ("all"), ("a") -> ALL;
                case ("get"), ("g") -> GET;
                case ("has"), ("h") -> HAS;
                case ("make"), ("m") -> MAKE;
                default -> NULL;
            };
        }
    }
}
