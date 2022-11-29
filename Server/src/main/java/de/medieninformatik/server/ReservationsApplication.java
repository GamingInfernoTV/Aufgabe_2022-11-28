package de.medieninformatik.server;

import jakarta.ws.rs.core.Application;

import java.util.HashSet;
import java.util.Set;

/**
 * {@link Application} für die Reservierung
 *
 * @author Malte Kasolowsky <code>m30114</code>
 */
public class ReservationsApplication extends Application {
    private static final Set<Object> singletons = new HashSet<>();
    private static final Set<Class<?>> classes = new HashSet<>();

    /**
     * Konstruktor; fügt eine neue Instanz von {@link ReservationsRest}
     * und die zugehörige Klasse den unterliegenden Speichern hinzu
     */
    public ReservationsApplication() {
        singletons.add(new ReservationsRest());
        classes.add(ReservationsRest.class);
    }

    /**
     * Gibt die gespeicherten Klassen zurück
     *
     * @return Die gespeicherten Klassen
     */
    @Override
    public Set<Class<?>> getClasses() {
        return classes;
    }

    /**
     * Gibt die gespeicherten Singleton-Objekte zurück
     *
     * @return Die gespeicherten Singleton-Objekte
     */
    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }
}
