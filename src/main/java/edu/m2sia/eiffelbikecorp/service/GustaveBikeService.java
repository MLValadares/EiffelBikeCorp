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

    public boolean buyBasket(User user, String currency) {
        Basket basket = user.getBasket();
        double totalPrice = basket.getTotalPrice();
        if (totalPrice == 0.0) {
            return false;
        }
        if(bankService.checkFunds(user.getId(), totalPrice, currency)) {
            for (Bike bike : basket.getBikes()) {
                bike.setOwner(user.getName());
                bike.setAvailable(true);
            }
            basket.clearBasket();
            bankService.processPayment(user.getId(), totalPrice, currency);
            return true;
        }
        return false;
    }


}