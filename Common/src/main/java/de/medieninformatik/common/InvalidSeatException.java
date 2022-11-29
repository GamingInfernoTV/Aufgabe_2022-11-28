package de.medieninformatik.common;

public class InvalidSeatException extends Exception {
    public InvalidSeatException(Seat seat, Throwable cause) {
        super("Seat is not valid: " + seat, cause);
    }
}
