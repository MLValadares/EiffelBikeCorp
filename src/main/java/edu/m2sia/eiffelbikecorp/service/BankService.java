package edu.m2sia.eiffelbikecorp.service;

import java.util.HashMap;
import java.util.Map;

public class BankService {
    private final Map<Integer, Double> userBalances = new HashMap<>();

    public BankService() {
        // Initialize with some dummy data
        userBalances.put(1, 5000.0);
        userBalances.put(2, 3000.0);
        userBalances.put(3, 10000.0);
    }

    public boolean checkFunds(int userId, double amount, String currency) {
        Double balance = userBalances.get(userId);
        if (balance == null) {
            return false;
        }
        return balance >= amount;
    }

    public void processPayment(int userId, double amount, String currency) {
        Double balance = userBalances.get(userId);
        if (balance != null && balance >= amount) {
            userBalances.put(userId, balance - amount);
        } else {
            throw new IllegalArgumentException("Insufficient funds");
        }
    }
}