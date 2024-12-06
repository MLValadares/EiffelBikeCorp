package edu.m2sia.eiffelbikecorp.service;

import java.util.HashMap;
import java.util.Map;

public class BankService {
    private final Map<Integer, Double> userBalances = new HashMap<>();
    private final CurrencyExchangeService currencyExchangeService;

    public BankService(CurrencyExchangeService currencyExchangeService) {
        // Initialize with some dummy data
        userBalances.put(1, 5000.0);
        userBalances.put(2, 3000.0);
        userBalances.put(3, 10000.0);
        this.currencyExchangeService = currencyExchangeService;
    }

    public boolean checkFunds(int userId, double amount, String currency) {
        Double balance = userBalances.get(userId);
        if (balance == null) {
            return false;
        }
        if (!currency.equals("EUR") && currencyExchangeService != null) {
            amount = currencyExchangeService.convert(amount, currency);
        }
        return balance >= amount;
    }

    public void processPayment(int userId, double amount, String currency) {
        Double balance = userBalances.get(userId);
        double amountInEur = (currency == null) ? amount : currencyExchangeService.convert(amount, currency);
        if (balance >= amountInEur) {
            userBalances.put(userId, balance - amountInEur);
        } else {
            throw new IllegalArgumentException("Insufficient funds");
        }
    }
}