package de.medieninformatik.server;

import de.medieninformatik.common.InvalidSeatException;
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

/**
 * Rest-Endpoint des Servers
 *
 * @author Malte Kasolowsky <code>m30114</code>
 * @author Author Pöhlmann <code>m30115</code>
 */
@Path("reservation")
public class ReservationsRest {
    ReservationsImpl reservation = new ReservationsImpl();
    Response response;

    /**
     *
     * @param row
     * @param num
     * @return
     */
    @GET
    @Path("{row}/{num}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getReservation(
            @PathParam("row") int row,
            @PathParam("num") int num) {
        System.out.println("GET " + row + ", " + num);
        Seat seat = new Seat(row, num);
        try {
            Optional<String> optional = reservation.getReservation(seat);
            if (optional.isPresent()) {
                response = Response.ok(seat.toString()).build();
            } else {
                response = Response.noContent().build();
            }
        } catch (InvalidSeatException e) {
            response = Response.status(Response.Status.FORBIDDEN).build();
        }
        return response;
        // TODO: Create response
    }

    /**
     *
     * @param row
     * @param num
     * @return
     */
    @GET
    @Path("{row}/{num}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response hasReservation(
            @PathParam("row") int row,
            @PathParam("num") int num) {
        Seat seat = new Seat(row, num);
        try {
            response = Response.ok(reservation.hasReservation(seat)).build();
        } catch (InvalidSeatException e) {
            response = Response.status(Response.Status.NO_CONTENT).build();
        }
        return response; // TODO: Create response
    }

    /**
     *
     * @param row
     * @param num
     * @param name
     * @return
     */
    @POST
    @Path("{row}/{num}/{name}")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response makeReservation(
            @PathParam("row") int row,
            @PathParam("num") int num,
            @PathParam("name") String name) {
        Seat seat = new Seat(row, num);
        try {
            if (reservation.makeReservation(seat, name)) {
                response = Response.ok().build();
            } else {
                response = Response.status(Response.Status.NOT_ACCEPTABLE).build();
            }
        } catch (InvalidSeatException e) {
            response = Response.status(Response.Status.NOT_FOUND).build();
        }
        return response; // TODO: Create response
    }
}
