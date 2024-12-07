package edu.m2sia.eiffelbikecorp.service;

import edu.m2sia.eiffelbikecorp.model.Bike;
import edu.m2sia.eiffelbikecorp.model.Rating;

import java.util.*;

public class BikeRentalService {
    private static final Map<Integer, Bike> bikes = new HashMap<>();
    private static final Map<Integer, Integer> bikeRenters = new HashMap<>();
    private int nextBikeId = 1;
    private final UserService userService;

    public BikeRentalService(UserService userService) {
        this.userService = userService;
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

    public Bike getBikeById(int bikeId) {
        return bikes.get(bikeId);
    }

    public enum RentBikeResult {
        SUCCESS,
        NOT_AVAILABLE,
        ADDED_TO_WAITING_LIST
    }

    public RentBikeResult rentBike(int id, int userId, boolean waitingList) {
        Bike bike = bikes.get(id);
        if (bike != null && bike.isAvailable()) {
            bike.setAvailable(false);
            bikeRenters.put(id, userId);
            return RentBikeResult.SUCCESS;
        } else if (bike != null && waitingList) {
            boolean success = bike.addWaitingList(userId); //ver repeticao de users
            if(success){
                return RentBikeResult.ADDED_TO_WAITING_LIST;
            }else {
                return RentBikeResult.NOT_AVAILABLE;
            }
        }
        return RentBikeResult.NOT_AVAILABLE;
    }

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

                //notify the next user in the waiting list
                Integer nextUser = bike.getNextUserInWaitingList();
                if (nextUser != null) {
                    userService.notifyUser(nextUser, "The bike with ID " + id + " is now available for rent.");
                }
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
