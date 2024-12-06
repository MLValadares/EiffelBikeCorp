package edu.m2sia.eiffelbikecorp.service;

import edu.m2sia.eiffelbikecorp.model.Basket;
import edu.m2sia.eiffelbikecorp.model.Bike;
import edu.m2sia.eiffelbikecorp.model.User;

import java.util.*;


public class GustaveBikeService {
    private final BikeRentalService bikeRentalService;
    private final BankService bankService;
//    private final CurrencyExchangeService currencyExchangeService;
    public GustaveBikeService(BikeRentalService bikeRentalService, BankService bankService) {
        this.bikeRentalService = bikeRentalService;
        this.bankService = bankService;
//        this.currencyExchangeService = currencyExchangeService;
    }
    public List<Bike> getAvailableBikesForSale() {
        List<Bike> allBikes = bikeRentalService.getAllBikes();
        List<Bike> availableBikes = new ArrayList<>();
        for (Bike bike : allBikes) {
            if (bike.isAvailable() && !bike.getRatings().isEmpty()) {
                availableBikes.add(bike);
            }
        }
        return availableBikes;
    }

    public boolean addToBasket(int bikeId, User user) {
        Bike bike = bikeRentalService.getBikeById(bikeId);
        if (bike == null) {
            throw new IllegalArgumentException("Bike not found");
        }
        if (bike.isAvailable()) {
            bike.setAvailable(false);
            user.getBasket().addBike(bike);
            return true;
        }
        return false;
    }

//    public boolean addToBasket(int bikeId, String token) {
//        User user = bikeRentalService.getUserService().getUserByToken(token);
//        System.out.println("User: " + user);
//        if (user == null) {
//            throw new IllegalArgumentException("Invalid token");
//        }
//        Bike bike = bikeRentalService.getBikeById(bikeId);
//        if (bike == null) {
//            throw new IllegalArgumentException("Bike not found");
//        }
//        if (bike.isAvailable()) {
//            bike.setAvailable(false);
//            user.getBasket().addBike(bike);
//            return true;
//        }
//        return false;
//    }
//
//
//    public boolean buyBasket(String token) {
//        User user = bikeRentalService.getUserService().getUserByToken(token);
//        if (user == null) {
//            throw new IllegalArgumentException("Invalid token");
//        }
//        Basket basket = user.getBasket();
//        double totalPrice = basket.getTotalPrice();
//        if (bankService.checkFunds(user.getId(), totalPrice, "EUR")) {
//            bankService.processPayment(user.getId(), totalPrice, "EUR");
//            basket.clearBasket();
//            return true;
//        }
//        return false;
//    }
}