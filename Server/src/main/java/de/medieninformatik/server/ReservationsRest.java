package de.medieninformatik.server;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("reservation")
public class ReservationsRest {

    @GET
    @Path("{row}/{num}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getReservation(
            @PathParam("row") int row,
            @PathParam("num") int num) {
        return null; // TODO: Create response
    }

    @GET
    @Path("{row}/{num}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response hasReservation(
            @PathParam("row") int row,
            @PathParam("num") int num) {
        return null; // TODO: Create response
    }

    @POST
    @Path("{row}/{num}/{name}")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response makeReservation(
            @PathParam("row") int row,
            @PathParam("num") int num,
            @PathParam("name") String name) {
        return null; // TODO: Create response
    }
}
