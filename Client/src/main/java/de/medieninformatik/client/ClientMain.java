package de.medieninformatik.client;

import de.medieninformatik.common.InvalidSeatException;
import de.medieninformatik.common.Reservation;
import de.medieninformatik.common.Seat;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    public static void main(String[] args) throws InvalidSeatException, URISyntaxException, IOException, InterruptedException {
        URI BaseURI = new URI("http://localhost:8080/rest");
        var client = new ReservationClient(args.length > 0 ? URI.create(args[0]) : BaseURI); // TODO: URI
        System.out.println(client);
        var scanner = new Scanner(System.in, StandardCharsets.UTF_8);
        while (true) {
            try {
                System.out.print("Enter action to execute: [ GET/g | HAS/h | MAKE/m ]: ");
                Action action = Action.fromString(scanner.next());
                System.out.println(switch (action) {
                    case GET -> "Enter seat [row, num] for getting the reservation from: ";
                    case HAS -> "Enter seat [row, num] to check on existing reservation: ";
                    case MAKE -> "Enter seat [row, num] and [name] to make reservation on: ";
                    case NULL -> "No valid input, exiting";
                });
                if (action == Action.NULL) break;
                reservationAction(client, scanner, action);
            } catch (InvalidSeatException e) {
                Logger.getLogger("org.glassfish")
                        .log(Level.SEVERE, "exception thrown from client", e);
                System.exit(-1);
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
    ) throws InvalidSeatException, URISyntaxException {
        var seat = new Seat(scanner.nextInt(), scanner.nextInt());
        switch (action) {
            case GET -> {
                System.out.println("Getting reservation for: " + seat);
                System.out.println("Reservation: " + client.getReservation(seat).orElse("none"));
            }
            case HAS -> {
                System.out.println("Checking reservation for: " + seat);
                if (client.hasReservation(seat)) System.out.println("No reservation exists");
                else System.out.println("Existing reservation found");
            }
            case MAKE -> {
                String name = scanner.next();
                if (client.makeReservation(seat, name)) System.out.println("Reservation has been made");
                else System.out.println("No reservation has been made");
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
