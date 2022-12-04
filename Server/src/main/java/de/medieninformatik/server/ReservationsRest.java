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

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response getAllReservations() {
        var s = ReservationsImpl.getAllReservations();
        return Response.status(
                s.isEmpty()
                        ? Response.Status.NO_CONTENT
                        : Response.Status.OK
        ).entity(
                s.isEmpty()
                        ? "no reservations have been made yet"
                        : s
        ).build();
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
            System.out.println(optional);
            return Response.status(
                    optional.isEmpty()
                            ? Response.Status.NO_CONTENT
                            : Response.Status.OK
            ).entity(
                    optional
                            .orElse("No reservation found for " + seat)
            ).build();
        } catch (InvalidSeatException e) {
            LOGGER.log(Level.WARNING, "invalid seat exception thrown when getting reservation", e);
            return Response.status(Response.Status.FORBIDDEN).entity(e.getLocalizedMessage()).build();
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "exception thrown when getting reservation", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(e.getLocalizedMessage()).build();
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
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "exception thrown when checking reservation", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(e.getLocalizedMessage()).build();
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
    @Path("make")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response makeReservation(
            @QueryParam("row") int row,
            @QueryParam("num") int num,
            @QueryParam("name") String name) {
        try {
            var seat = new Seat(row, num);
            var success = reservation.makeReservation(seat, name);
            return Response.status(
                    success
                            ? Response.Status.OK
                            : Response.Status.NOT_ACCEPTABLE
            ).entity(
                    success
                            ? ("Made reservation for " + seat + " on: " + name)
                            : (seat + " already has a reservation")
            ).build();
        } catch (InvalidSeatException e) {
            LOGGER.log(Level.WARNING, "invalid seat exception thrown when making reservation", e);
            return Response.status(Response.Status.FORBIDDEN).entity(e.getLocalizedMessage()).build();
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "exception thrown when making reservation", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(e.getLocalizedMessage()).build();
        }
    }
}
