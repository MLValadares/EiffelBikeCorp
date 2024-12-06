package edu.m2sia.eiffelbikecorp.service;


import edu.m2sia.eiffelbikecorp.model.Bike;
import edu.m2sia.eiffelbikecorp.model.Rating;

import java.util.*;

public class GustaveBikeService {
    private final BikeRentalService bikeRentalService;
    private final BankService bankService;
    private final CurrencyExchangeService currencyExchangeService;

    public GustaveBikeService(BikeRentalService bikeRentalService, BankService bankService, CurrencyExchangeService currencyExchangeService) {
        this.bikeRentalService = bikeRentalService;
        this.bankService = bankService;
        this.currencyExchangeService = currencyExchangeService;
    }

    public List<Bike> getAvailableBikesForSale() {
        List<Bike> allBikes = bikeRentalService.getAllBikes();
        List<Bike> availableBikes = new ArrayList<>();
        for (Bike bike : allBikes) {
            if (!bike.isAvailable() && !bike.getRatings().isEmpty()) {
                availableBikes.add(bike);
            }
        }
        return availableBikes;
    }

    public double getBikePriceInCurrency(int bikeId, String currency) {
        Bike bike = bikeRentalService.getBikeById(bikeId);
        if (bike == null) {
            throw new IllegalArgumentException("Bike not found");
        }
        double priceInEuros = bike.getPrice();
        double exchangeRate = currencyExchangeService.getExchangeRate("EUR", currency);
        return priceInEuros * exchangeRate;
    }

    public boolean purchaseBike(int bikeId, String token, String currency) {
        User user = bikeRentalService.getUserService().getUserByToken(token);
        if (user == null) {
            throw new IllegalArgumentException("Invalid token");
        }
        double priceInCurrency = getBikePriceInCurrency(bikeId, currency);
        boolean fundsAvailable = bankService.checkFunds(user.getId(), priceInCurrency, currency);
        if (fundsAvailable) {
            bankService.processPayment(user.getId(), priceInCurrency, currency);
            bikeRentalService.removeBike(bikeId, user.getUsername());
            return true;
        }
        return false;
    }
}
