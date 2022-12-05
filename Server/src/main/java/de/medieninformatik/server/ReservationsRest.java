package de.medieninformatik.server;

import de.medieninformatik.common.InvalidSeatException;
import de.medieninformatik.common.Reservation;
import de.medieninformatik.common.Seat;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Rest-Endpoint des Servers; beinhaltet alle Methode, die in {@link ReservationsImpl} implementiert werden
 * TODO check all comments please, kuss
 *
 * @author Malte Kasolowsky <code>m30114</code>
 * @author Author Pöhlmann <code>m30115</code>
 */
@Path("reservation")
public class ReservationsRest {
    private static final Logger LOGGER = Logger.getLogger("org.glassfish");
    private final Reservation reservation = new ReservationsImpl();

    /**
     * Handler für das root-Verzeichnis der Klasse, welcher mit der Rückgabe aller Reservierungen antwortet
     *
     * @return Eine {@link Response}, welche die {@link java.util.Map} von {@link Reservation#getAllReservations()}
     * als String verpackt beinhaltet und, wenn Reservierungen vorliegen, den Status {@link Response.Status#OK},
     * ansonsten den Status {@link Response.Status#NO_CONTENT} hat
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response getAllReservations() {
        var stringBuilder = new StringBuilder();
        reservation.getAllReservations().forEach((Seat seat, String name) -> {
            stringBuilder.append(seat);
            stringBuilder.append(": ");
            stringBuilder.append(name);
            stringBuilder.append(';');
            stringBuilder.append(System.lineSeparator());
        });
        var s = stringBuilder.toString();
        return Response.status(
                s.isEmpty()
                        ? Response.Status.NO_CONTENT
                        : Response.Status.OK
        ).entity(s).build();
    }
    /**
     * Liest den Namen, auf den {@link Seat} der reserviert wurde, aus dem Speicher
     *
     * @param row Die Reihe des zu überprüfenden Sitzes
     * @param num Die Nummer des zu überprüfenden Sitzes
     * @return Eine {@link Response}, welche den Status der Anfrage sowie den Wert von {@link Reservation#getReservation(Seat)}
     * beinhaltet, wenn eine {@link Reservation} vorliegt den Status {@link Response.Status#OK},
     * ansonsten den Status {@link Response.Status#NO_CONTENT}
     */
    @GET
    @Path("get")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getReservation(
            @QueryParam("row") int row,
            @QueryParam("num") int num) {
        try {
            var seat = new Seat(row, num);
            var optional = reservation.getReservation(seat);
            return Response.status(
                    optional.isEmpty()
                            ? Response.Status.NO_CONTENT
                            : Response.Status.OK
            ).entity(
                    optional.orElse("No reservation found for " + seat)
            ).build();
        } catch (InvalidSeatException e) {
            LOGGER.log(Level.WARNING, "invalid seat exception thrown when getting reservation", e);
            return Response.status(Response.Status.FORBIDDEN).entity(e.getLocalizedMessage()).build();
        }
    }

    /**
     * Überprüft, ob ein {@link Seat} bereits eine {@link Reservation} hat
     *
     * @param row Reihe des zu überprüfenden Sitzes
     * @param num Nummer des zu überprüfenden Sitzes
     * @return Eine {@link Response}, welche den Status der Anfrage sowie den Wert von {@link Reservation#hasReservation(Seat)}
     * beinhaltet, wenn eine {@link Reservation} vorliegt den Status {@link Response.Status#OK},
     * ansonsten wird eine {@link InvalidSeatException} in einem {@link Logger} gespeichert und
     * der Status {@link Response.Status#FORBIDDEN}
     */
    @GET
    @Path("check")
    @Produces(MediaType.TEXT_PLAIN)
    public Response hasReservation(
            @QueryParam("row") int row,
            @QueryParam("num") int num) {
        try {
            var seat = new Seat(row, num);
            return Response.ok(reservation.hasReservation(seat)).build();
        } catch (InvalidSeatException e) {
            LOGGER.log(Level.WARNING, "invalid seat exception thrown when checking reservation", e);
            return Response.status(Response.Status.FORBIDDEN).entity(e.getLocalizedMessage()).build();
        }
    }

    /**
     * Erstellt eine neue {@link Reservation},
     * genauer speichert den Namen der Reservierung für den {@link Seat} im internen Speicher,
     * sofern für den Sitz noch keine Reservierung vorliegt
     *
     * @param row Reihe des zu reservierenden Sitzes
     * @param num  Nummer des zu reservierenden Sitzes
     * @param name Der Name, auf dem reserviert werden soll
     * @return Eine {@link Response}, welche den Status der Anfrage sowie den Wert von {@link Reservation#makeReservation(Seat, String)}
     * beinhaltet, wenn die {@link Reservation} erfolgreich war den Status {@link Response.Status#OK},
     * ansonsten wird eine {@link InvalidSeatException} in einem {@link Logger} gespeichert und
     * der Status {@link Response.Status#FORBIDDEN}
     */
    @POST
    @Path("make")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response makeReservation(
            @QueryParam("row") int row,
            @QueryParam("num") int num,
            String name) {
        try {
            var seat = new Seat(row, num);
            var success = reservation.makeReservation(seat, name);
            return Response.ok(success).build();
        } catch (InvalidSeatException e) {
            LOGGER.log(Level.WARNING, "invalid seat exception thrown when making reservation", e);
            return Response.status(Response.Status.FORBIDDEN).entity(e.getLocalizedMessage()).build();
        }
    }
}
