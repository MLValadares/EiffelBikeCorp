package edu.m2sia.eiffelbikecorp.rental;

import edu.m2sia.model.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BikeRentalManager {
    private List<Bike> bikes = new ArrayList<>();

    public BikeRentalManager() {
        // Initialize with some bikes
        bikes.add(new Bike(1, "Mountain Bike", true));
        bikes.add(new Bike(2, "Road Bike", true));
        bikes.add(new Bike(3, "Hybrid Bike", false));
    }

    public List<Bike> getAllBikes() {
        return bikes;
    }

    public Bike rentBike(int id) {
        for (Bike bike : bikes) {
            if (bike.getId() == id && bike.isAvailable()) {
                bike.setAvailable(false);
                return bike;
            }
        }
        return null; // Bike not available
    }

    public Bike returnBike(int id) {
        for (Bike bike : bikes) {
            if (bike.getId() == id && !bike.isAvailable()) {
                bike.setAvailable(true);
                return bike;
            }
        }
        return null; // Bike was not rented
    }
}
