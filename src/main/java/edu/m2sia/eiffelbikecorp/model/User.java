package edu.m2sia.eiffelbikecorp.model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private int id;
    private String name;
    private String username; // New field for authentication
    private String password; // New field for authentication
    private static final List<Integer> rentedBikeIds = new ArrayList<>(); // List of rented bike IDs

    // Constructor
    public User(int id, String name, String username, String password) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Integer> getRentedBikeIds() {
        return rentedBikeIds;
    }

    public void rentBike(int bikeId) {
        rentedBikeIds.add(bikeId);
    }

    public void returnBike(int bikeId) {
        rentedBikeIds.remove(Integer.valueOf(bikeId));
    }
}