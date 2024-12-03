package edu.m2sia.eiffelbikecorp.model;

public class Bike {
    private int id;
    private String model;
    private boolean isAvailable;

    // Constructor
    public Bike(int id, String model, boolean isAvailable) {
        this.id = id;
        this.model = model;
        this.isAvailable = isAvailable;
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
}