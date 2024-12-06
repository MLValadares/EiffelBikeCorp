package edu.m2sia.eiffelbikecorp.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Bike {
    private int id;
    private String model;
    private boolean isAvailable;
    private String owner;
    private final List<Rating> ratings = new ArrayList<>(); //não por static
    private final Queue<Integer> waitingList = new LinkedList<>();

    // Constructor
    public Bike(int id, String model, boolean isAvailable, String owner) {
        this.id = id;
        this.model = model;
        this.isAvailable = isAvailable;
        this.owner = owner;
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
        ratings.add(rating);
    }

    public boolean addWaitingList(int userId) {
            if (!waitingList.contains(userId)) {
                waitingList.offer(userId);
                return true;
            }
            return false;
    }



    public void removeWaitingList(int userId) {
        waitingList.remove(userId);
    }

    public Integer getNextUserInWaitingList() {
        return waitingList.poll();
    }
}