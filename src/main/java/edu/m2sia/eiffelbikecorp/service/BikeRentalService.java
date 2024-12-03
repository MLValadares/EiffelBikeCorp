package edu.m2sia.eiffelbikecorp.service;

import edu.m2sia.eiffelbikecorp.model.Bike;

import java.util.ArrayList;
import java.util.List;

public class BikeRentalService {
    private final List<Bike> bikes = new ArrayList<>();

    public BikeRentalService() {
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
