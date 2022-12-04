package de.medieninformatik.client;

import de.medieninformatik.common.InvalidSeatException;
import de.medieninformatik.common.Reservation;
import de.medieninformatik.common.Seat;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;
import java.util.Optional;

/**
 * Clientseitige Implementierung von {@link Reservation}
 * TODO: specify how the implementation works
 *
 * @author Malte Kasolowsky <code>m30114</code>
 * @author Aaron Pöhlmann <code>m30115</code>
 */
// TODO: remove suppression
@SuppressWarnings({"unused", "FieldCanBeLocal", "RedundantThrows"})
public class ReservationClient implements Reservation {
    private final Client client;
    private final URI baseURI;
    private final HttpClient httpClient = HttpClient.newHttpClient();

    /**
     * TODO
     *
     * @param uri TODO
     */
    public ReservationClient(URI uri) {
        this.baseURI = Objects.requireNonNull(uri);
        this.client = ClientBuilder.newClient();
    }

    /**
     * TODO
     *
     * @param seat Der Sitz, für welchen die Reservierung abgefragt werden soll
     * @return TODO
     * @throws InvalidSeatException TODO
     */
    @Override
    public Optional<String> getReservation(Seat seat) throws InvalidSeatException {
        URI url = null;
        try {
            url = new URI(baseURI + "/reservation/get?row=" + seat.row()
                    + "&num=" + seat.num());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(url)
                .build();
        WebTarget target = client.target(url);
        Response response = target.request().accept(MediaType.TEXT_PLAIN).get();
        if (status(response) == 200) {
            String name = response.readEntity(String.class);
            return Optional.ofNullable(name);
        }
        return Optional.empty(); // TODO: Make getReservation request
    }

    /**
     * TODO
     *
     * @param seat Der abzufragende Sitz
     * @return TODO
     * @throws InvalidSeatException TODO
     */
    @Override
    public boolean hasReservation(Seat seat) throws InvalidSeatException {
        URI url = null;
        try {
            url = new URI(baseURI + "/reservation/check?row=" + seat.row()
                    + "&num=" + seat.num());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        HttpRequest hasRequest = HttpRequest.newBuilder()
                .uri(url)
                .build();
        WebTarget target = client.target(url);
        Response response = target.request().accept(MediaType.TEXT_PLAIN).get();
        return false; // TODO: Make hasReservation request
    }

    /**
     * Es kann einen {@link Seat} reservieren.
     * Dazu wird die Sitzreihe (Row) und die Sitznummer (Num),
     * sowie der Name angegeben auf den reserviert werden soll.
     *
     * @param seat Der Sitz, welcher reserviert werden soll
     * @param name Der Name, auf den reserviert werden soll
     * @return TODO
     * @throws InvalidSeatException TODO
     */
    @Override
    public boolean makeReservation(Seat seat, String name) throws InvalidSeatException {
        URI url = null;
        String platzhalter = " ";
        try {
            url = new URI(baseURI +  "/reservation/make?row=" + seat.row()
                    + "&num=" + seat.num()
                    + "&name=" + name);
            System.out.println(url);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        HttpRequest makeRequest = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(platzhalter))
                .build();
        try {
            HttpResponse<String> response = httpClient.send(makeRequest,
                    HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return false; // TODO: Make makeReservation request
    }

    private int status(Response response) {
        int status = response.getStatus();
        String reason = response.getStatusInfo().getReasonPhrase();
        System.out.printf("Status: %d %s%n", status, reason);
        return status;
    }
}
