package edu.m2sia.eiffelbikecorp.service;

import edu.m2sia.eiffelbikecorp.model.Bike;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BikeRentalService {
    private static final List<Bike> bikes = new ArrayList<>();
    private static final Map<Integer, String> bikeRenters = new HashMap<>();

    public BikeRentalService() {
        // Initialize with some bikes
        bikes.add(new Bike(1, "Mountain Bike", true));
        bikes.add(new Bike(2, "Road Bike", true));
        bikes.add(new Bike(3, "Hybrid Bike", false));
    }

    public List<Bike> getAllBikes() {
        return bikes;
    }

    public Bike rentBike(int id, String username) {
        for (Bike bike : bikes) {
            if (bike.getId() == id && bike.isAvailable()) {
                bike.setAvailable(false);
                bikeRenters.put(id, username); // Assign bike to user
                return bike;
            }
        }
        return null; // Bike not available
    }

    public Bike returnBike(int id, String username) {
        if (bikeRenters.containsKey(id) && bikeRenters.get(id).equals(username)) {
            for (Bike bike : bikes) {
                if (bike.getId() == id && !bike.isAvailable()) {
                    bike.setAvailable(true);
                    bikeRenters.remove(id); // Unassign bike from user
                    return bike;
                }
            }
        }
        return null; // Unauthorized or invalid return
    }
}
