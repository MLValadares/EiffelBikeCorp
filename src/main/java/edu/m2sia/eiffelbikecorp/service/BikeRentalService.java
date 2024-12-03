package edu.m2sia.eiffelbikecorp.service;

import edu.m2sia.eiffelbikecorp.model.Bike;
import edu.m2sia.eiffelbikecorp.model.Rating;
import edu.m2sia.eiffelbikecorp.model.User;

import java.util.*;

public class BikeRentalService {
    private static final Map<Integer, Bike> bikes = new HashMap<>();
    private static final Map<Integer, Integer> bikeRenters = new HashMap<>();
    private int nextBikeId = 1;
    private static final Map<Integer, Queue<String>> waitingList = new HashMap<>();

    public BikeRentalService() {
        // Initialize with some bikes
//        bikes.add(new Bike(1, "Mountain Bike", true, "Eiffel Bike Corp"));
//        bikes.add(new Bike(2, "Road Bike", true, "Eiffel Bike Corp"));
//        bikes.add(new Bike(3, "Hybrid Bike", false, "Eiffel Bike Corp"));
        addBike("Mountain Bike", "Eiffel Bike Corp");
        addBike("Road Bike", "Eiffel Bike Corp");
        addBike("Hybrid Bike", "Eiffel Bike Corp");
    }

    public Bike addBike(String model, String owner) {
        Bike bike = new Bike(nextBikeId++, model, true, owner);
        bikes.put(bike.getId(), bike);
        return bike;
    }

    public List<Bike> getAllBikes() {
        return new ArrayList<>(bikes.values());
    }

    public Bike rentBike(int id, int userId) {
        Bike bike = bikes.get(id);
        if (bike != null && bike.isAvailable()) {
            bike.setAvailable(false);
            bikeRenters.put(id, userId); // Store user ID as a string
            return bike;
        }
        return null; // Bike not available
    }


//    public boolean rentBike(int bikeId, String userToken) {
//        Bike bike = bikes.get(bikeId);
//        if (bike == null) {
//            throw new IllegalArgumentException("Bike not found");
//        }
//        if (!bike.isAvailable()) {
//            // Bike is unavailable, add the user to the waiting list
//            waitingList.putIfAbsent(bikeId, new LinkedList<>());
//            Queue<String> queue = waitingList.get(bikeId);
//            if (!queue.contains(userToken)) {
//                queue.offer(userToken);
//            }
//            return false; // Indicate that the bike is not currently rentable
//        }
//
//        // Rent the bike
//        bike.setAvailable(false);
//        bikeRenters.put(bikeId, userToken); // Track who is renting the bike
//        return true;
//    }

    public Bike returnBike(int id, int userId, int conditionRating, String conditionNotes) {
//        System.out.println("bikeRenters: " + bikeRenters);
        if (bikeRenters.containsKey(id) && bikeRenters.get(id).equals(userId)) {
            Bike bike = bikes.get(id);
            if (bike != null && !bike.isAvailable()) {
                // Update bike availability
                bike.setAvailable(true);
                bikeRenters.remove(id); // Unassign bike from user

                // Add a new Rating
                Rating rating = new Rating(conditionRating, conditionNotes, userId);
                bike.addRating(rating);

                return bike;
            }
        }
        return null; // Unauthorized or invalid return
    }



    public boolean removeBike(int bikeId, String owner) {
        Bike bike = bikes.get(bikeId);
        if (bike == null) {
            return false; // Bike does not exist
        }
        if (!bike.getOwner().equals(owner)) {
            return false; // Not the owner
        }
        if (!bike.isAvailable()) {
            return false; // Bike is currently rented
        }
        bikes.remove(bikeId); // Remove the bike from the system
        return true;
    }

    public List<Rating> getRatingsForBike(int bikeId) {
        Bike bike = bikes.get(bikeId);
        if (bike != null) {
            return bike.getRatings();
        }
        return null; // Bike not found
    }
}
