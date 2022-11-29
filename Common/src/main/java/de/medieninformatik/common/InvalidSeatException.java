package de.medieninformatik.common;

/**
 * Ausnahme, die bei der Übergabe eines invaliden {@link Seat Seats} geworfen werden soll
 *
 * @author Malte Kasolowsky <code>m30114</code>
 */
public class InvalidSeatException extends Exception {

    /**
     * Konstruktor; instanziiert eine Ausnahme ausgehend von einem {@link Seat}
     * und einem {@link Throwable} als Grund für die Ausnahme
     *
     * @param seat Der Sitz, welcher der invalide ist
     * @param cause Der Grund für die Invalidität des Sitzes
     */
    public InvalidSeatException(Seat seat, Throwable cause) {
        super("Seat is not valid: " + seat, cause);
    }
}
