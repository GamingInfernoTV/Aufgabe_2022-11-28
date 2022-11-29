package de.medieninformatik.server;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Rest-Endpoint des Servers
 *
 * @author Malte Kasolowsky <code>m30114</code>
 * @author Author Pöhlmann <code>m30115</code>
 */
@Path("reservation")
public class ReservationsRest {

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
        return null; // TODO: Create response
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
        return null; // TODO: Create response
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
        return null; // TODO: Create response
    }
}
