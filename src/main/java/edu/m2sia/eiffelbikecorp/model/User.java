package edu.m2sia.eiffelbikecorp.model;

import edu.m2sia.eiffelbikecorp.service.BikeRentalService;

import java.util.ArrayList;
import java.util.List;

public class User {
    private int id;
    private String name;
    private String username; // New field for authentication
    private String password; // New field for authentication
    private boolean isEmployee;
    private final List<Integer> rentedBikeIds = new ArrayList<>(); // List of rented bike IDs
    private final Basket basket = new Basket(); // New field for shopping basket

    // Constructor
    public User(int id, String name, String username, String password,  boolean isEmployee) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.isEmployee = isEmployee;
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

    public boolean isEmployee() {
        return isEmployee;
    }

    public void setEmployee(boolean employee) {
        isEmployee = employee;
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

    public Basket getBasket() {
        return basket;
    }
}