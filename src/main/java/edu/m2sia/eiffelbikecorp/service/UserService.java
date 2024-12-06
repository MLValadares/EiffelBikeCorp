package edu.m2sia.eiffelbikecorp.service;

import edu.m2sia.eiffelbikecorp.model.User;

import java.util.HashMap;
import java.util.Map;

public class UserService {
    private static final Map<String, User> users = new HashMap<>(); // Keyed by username
    private static final Map<String, String> sessions = new HashMap<>(); // Keyed by token, maps to username


    public UserService() {
        // Predefined users for demo
        users.put("john_doe", new User(1, "John Doe", "john_doe", "password123"));
        users.put("jane_doe", new User(2, "Jane Doe", "jane_doe", "mypassword"));
    }


    public boolean addUser(String name, String username, String password) {
        if (users.containsKey(username)) {
            return false; // Username already exists
        }
        User user = new User(users.size() + 1, name, username, password);
        users.put(username, user);
        return true; // User added successfully
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

    public Integer getUserIdByToken(String token) {
        String username = sessions.get(token);
        User user = username != null ? users.get(username) : null;
        return user != null ? user.getId() : null;
    }

    public void notifyUser(int userId, String message) {
        User user = users.values().stream().filter(u -> u.getId() == userId).findFirst().orElse(null);
        if (user != null) {
            // Implement your notification logic here (e.g., send an email, SMS, etc.)
            System.out.println("Notifying user " + user.getUsername() + ": " + message);
        }
    }

    public String getUsernameById(Integer userId) {
        return users.values().stream().filter(u -> u.getId() == userId).findFirst().map(User::getUsername).orElse(null);
    }
}
