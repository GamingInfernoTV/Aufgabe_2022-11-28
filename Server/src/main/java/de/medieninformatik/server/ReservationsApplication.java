package de.medieninformatik.server;

import jakarta.ws.rs.core.Application;

import java.util.HashSet;
import java.util.Set;

public class ReservationsApplication extends Application {
    private static final Set<Object> singletons = new HashSet<>();
    private static final Set<Class<?>> classes = new HashSet<>();

    public ReservationsApplication() {
        singletons.add(new ReservationsRest());
        classes.add(ReservationsRest.class);
    }

    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }

    @Override
    public Set<Class<?>> getClasses() {
        return classes;
    }
}
