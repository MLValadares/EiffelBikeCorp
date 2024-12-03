package edu.m2sia.eiffelbikecorp.model;

import java.util.ArrayList;
import java.util.List;

public class Bike {
    private int id;
    private String model;
    private boolean isAvailable;
    private String owner;
    private final List<Rating> ratings;

    // Constructor
    public Bike(int id, String model, boolean isAvailable, String owner) {
        this.id = id;
        this.model = model;
        this.isAvailable = isAvailable;
        this.owner = owner;
        this.ratings = new ArrayList<>(); // Initialize the ratings list
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
    public List<Rating> getRatings() {
        return ratings;
    }

    public void addRating(Rating rating) {
        this.ratings.add(rating);
    }
}