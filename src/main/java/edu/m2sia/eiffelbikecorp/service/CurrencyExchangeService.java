package edu.m2sia.eiffelbikecorp.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CurrencyExchangeService {
    public double convert(double totalPrice, String currency) {
        try {
            String apiUrl = "https://cdn.jsdelivr.net/npm/@fawazahmed0/currency-api@latest/v1/currencies/eur.json";
            URL url = new URL(apiUrl);
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

                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonResponse = objectMapper.readTree(response.toString());
                double exchangeRate = jsonResponse.path("eur").path(currency.toLowerCase()).asDouble();

                return totalPrice * exchangeRate;
            } else {
                throw new RuntimeException("Failed to get exchange rate: HTTP response code " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error occurred while converting currency", e);
        }
    }
}
