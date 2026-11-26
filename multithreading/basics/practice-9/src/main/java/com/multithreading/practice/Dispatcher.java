package com.multithreading.practice;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Dispatcher {

    private final Set<Taxi> taxis = new HashSet<>();
    private final Set<Taxi> availableTaxis = new HashSet<>();

    public synchronized void register(Taxi taxi) {
        taxis.add(taxi);
        availableTaxis.add(taxi);
    }

    public synchronized void notifyAvailable(Taxi taxi) {
        availableTaxis.add(taxi);
    }

    /**
     * Снимок местоположений всех такси. Метод удерживает замок Dispatcher и при этом
     * вызывает taxi.getLocation(), захватывая замок Taxi — порядок Dispatcher → Taxi.
     * Если setLocation захватит их в обратном порядке (Taxi → Dispatcher) под одним замком,
     * возникнет дедлок. Этот метод изменять не нужно — проблему решает open call в Taxi.setLocation.
     */
    public synchronized List<Point> getImage() {
        List<Point> image = new ArrayList<>();
        for (Taxi taxi : taxis) {
            image.add(taxi.getLocation());
        }
        return image;
    }
}
