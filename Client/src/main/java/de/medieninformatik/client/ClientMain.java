package de.medieninformatik.client;

import de.medieninformatik.common.InvalidSeatException;
import de.medieninformatik.common.Seat;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class ClientMain {
    public static void main(String[] args) {
        var client = new ReservationClient(null);
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

    private static void reservationAction(
            final ReservationClient client,
            final Scanner scanner,
            final Action action
    ) throws InvalidSeatException {
        Seat seat = new Seat(scanner.nextInt(), scanner.nextInt());
        switch (action) {
            case GET -> {
                System.out.println("Getting reservation for: " + seat);
                System.out.println("Reservation: " + client.getReservation(seat));
            }
            case HAS -> {
                System.out.println("Checking reservation for: " + seat);
                if (client.hasReservation(seat)) System.out.println("No reservation exist");
                else System.out.println("Existing reservation found");
            }
            case MAKE -> {
                String name = scanner.next();
                if (client.makeReservation(seat, name)) System.out.println("Reservation has been made");
                System.out.println("No reservation has been made");
            }
            case NULL -> throw new IllegalArgumentException("Action must not be null");
        }
    }

    private enum Action {
        GET, HAS, MAKE, NULL;

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
