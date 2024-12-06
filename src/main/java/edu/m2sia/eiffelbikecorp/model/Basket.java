package edu.m2sia.eiffelbikecorp.model;

import java.util.ArrayList;
import java.util.List;

public class Basket {
    private List<Bike> bikes;

    public Basket() {
        this.bikes = new ArrayList<>();
    }

    public List<Bike> getBikes() {
        return bikes;
    }

    public void addBike(Bike bike) {
        bikes.add(bike);
    }

    public void removeBike(Bike bike) {
        bikes.remove(bike);
    }

    public void clearBasket() {
        bikes.clear();
    }

    public double getTotalPrice() {
        double total = 0.0;
        for (Bike bike : bikes) {
            total += bike.getPrice();
        }
        return total;
    }
}
