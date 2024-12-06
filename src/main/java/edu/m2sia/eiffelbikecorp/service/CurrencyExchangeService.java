package edu.m2sia.eiffelbikecorp.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class CurrencyExchangeService {
    private static final String API_URL = "https://cdn.jsdelivr.net/npm/@fawazahmed0/currency-api@latest/v1/currencies/eur.json";

    public double getExchangeRate(String fromCurrency, String toCurrency) {
        if (!"EUR".equalsIgnoreCase(fromCurrency)) {
            throw new IllegalArgumentException("Only EUR as fromCurrency is supported");
        }

        try {
            URL url = new URL(API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                JSONObject jsonResponse = new JSONObject(response.toString());
                return jsonResponse.getDouble(toCurrency.toLowerCase());
            } else {
                throw new RuntimeException("Failed to get exchange rate: HTTP error code : " + responseCode);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to get exchange rate", e);
        }
    }
}
