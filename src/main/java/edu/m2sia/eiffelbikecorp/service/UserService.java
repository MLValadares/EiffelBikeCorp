package edu.m2sia.eiffelbikecorp.service;

import edu.m2sia.eiffelbikecorp.model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class UserService {
    private static final Map<String, User> users = new HashMap<>(); // Keyed by username
    private static final Map<String, String> sessions = new HashMap<>(); // Keyed by token, maps to username


    public UserService() {
        // Predefined users for demo
        users.put("john_doe", new User(1, "John Doe", "john_doe", "password123"));
        users.put("jane_doe", new User(2, "Jane Doe", "jane_doe", "mypassword"));
    }

    public String login(String username, String password) {
        User user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            String token = "token-" + username + "-" + System.currentTimeMillis(); //substitur
            sessions.put(token, username);
//            System.out.println("Generated token: " + token);
//            System.out.println("Sessions map: " + sessions);
            return token;
        }
        return null; // Authentication failed
    }

    public User getUserByToken(String token) {
//        System.out.println(sessions);
        String username = sessions.get(token);
        return username != null ? users.get(username) : null;
    }

    public void notifyUser(int userId, String message) {
        User user = users.values().stream().filter(u -> u.getId() == userId).findFirst().orElse(null);
        if (user != null) {
            // Implement your notification logic here (e.g., send an email, SMS, etc.)
            System.out.println("Notifying user " + user.getUsername() + ": " + message);
        }
    }
}
