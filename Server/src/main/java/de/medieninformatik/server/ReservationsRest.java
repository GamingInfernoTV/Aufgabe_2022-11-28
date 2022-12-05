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

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Rest-Endpoint des Servers
 * TODO add a more specific implementation explanation
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
     * TODO
     *
     * @param row TODO
     * @param num TODO
     * @return TODO
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
     * TODO
     *
     * @param row TODO
     * @param num TODO
     * @return TODO
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
     * TODO
     *
     * @param row  TODO
     * @param num  TODO
     * @param name TODO
     * @return TODO
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
