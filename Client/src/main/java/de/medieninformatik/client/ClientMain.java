package de.medieninformatik.client;

import de.medieninformatik.common.Seat;

import java.util.Locale;
import java.util.Scanner;

public final class ClientMain {
    public static void main(String[] args) {
        ReservationClient client = new ReservationClient(null);
        Scanner scanner = new Scanner(System.in);
        boolean loop = true;
        do {
            try {
                System.out.print("Enter action to execute: [ GET/g | HAS/h | MAKE/m ]: ");
                Action action = Action.fromString(scanner.next());
                System.out.println(switch (action) {
                    case GET -> "Enter seat [row, num] for getting the reservation from: ";
                    case HAS -> "Enter seat [row, num] to check on existing reservation: ";
                    case MAKE -> "Enter seat [row, num] and [name] to make reservation on: ";
                });
                switch (action) {
                    case GET -> {
                        Seat seat = new Seat(scanner.nextInt(), scanner.nextInt());
                        System.out.println("Getting reservation for: " + seat);
                        System.out.println("Reservation: " + client.getReservation(seat));
                    }
                    case HAS -> {
                        Seat seat = new Seat(scanner.nextInt(), scanner.nextInt());
                        System.out.println("Checking reservation for: " + seat);
                        if (client.hasReservation(seat)) System.out.println("No reservation exist");
                        else System.out.println("Existing reservation found");
                    }
                    case MAKE -> {
                        Seat seat = new Seat(scanner.nextInt(), scanner.nextInt());
                        String name = scanner.next();
                        if (client.makeReservation(seat, name)) System.out.println("Reservation has been made");
                        System.out.println("No reservation has been made");
                    }
                    default -> {
                        System.out.println("No valid input, exiting");
                        loop = false;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        } while (loop);
    }

    private enum Action {
        GET, HAS, MAKE;

        static Action fromString(String str) {
            return switch (str.toLowerCase(Locale.ROOT)) {
                case ("get"), ("g") -> GET;
                case ("has"), ("h") -> HAS;
                case ("make"), ("m") -> MAKE;
                default -> null;
            };
        }
    }
}
