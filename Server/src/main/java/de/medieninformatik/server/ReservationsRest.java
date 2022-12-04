package de.medieninformatik.server;

import de.medieninformatik.common.InvalidSeatException;
import de.medieninformatik.common.Reservation;
import de.medieninformatik.common.Seat;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Optional;
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
     * TODO
     *
     * @param row TODO
     * @param num TODO
     * @return TODO
     */
    @GET
    @Path("{row}/{num}/name")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getReservation(
            @PathParam("row") int row,
            @PathParam("num") int num) {
        System.out.println("GET " + row + ", " + num);
        final var seat = new Seat(row, num);
        try {
            Optional<String> optional = reservation.getReservation(seat);
            return optional.isPresent()
                    ? Response.ok(seat.toString()).build()
                    : Response.noContent().build();
        } catch (InvalidSeatException e) {
            LOGGER.log(Level.SEVERE, "exception thrown when getting reservation", e);
            return Response.status(Response.Status.FORBIDDEN).build();
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
    @Path("{row}/{num}/check")
    @Produces(MediaType.TEXT_PLAIN)
    public Response hasReservation(
            @PathParam("row") int row,
            @PathParam("num") int num) {
        final var seat = new Seat(row, num);
        try {
            return Response.ok(
                    reservation.hasReservation(seat) ? "seat has a reservation" : "seat has no reservation"
            ).build();
        } catch (InvalidSeatException e) {
            LOGGER.log(Level.SEVERE, "exception thrown when checking reservation", e);
            return Response.status(Response.Status.NO_CONTENT).build();
        }
    }

    /**
     * TODO
     *
     * @param row TODO
     * @param num TODO
     * @param name TODO
     * @return TODO
     */
    @POST
    @Path("{row}/{num}/{name}")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response makeReservation(
            @PathParam("row") int row,
            @PathParam("num") int num,
            @PathParam("name") String name) {
        final var seat = new Seat(row, num);
        try {
            return reservation.makeReservation(seat, name)
                    ? Response.ok().build()
                    : Response.status(Response.Status.NOT_ACCEPTABLE).build();
        } catch (InvalidSeatException e) {
            LOGGER.log(Level.SEVERE, "exception thrown when making reservation", e);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
