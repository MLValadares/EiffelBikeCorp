package edu.m2sia.eiffelbikecorp.service;

import edu.m2sia.eiffelbikecorp.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UserService {
    private static final Map<String, User> users = new HashMap<>(); // Keyed by username
    private static final Map<String, String> sessions = new HashMap<>();// Keyed by token, maps to username
    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);


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
//            String token = "token-" + username + "-" + System.currentTimeMillis(); //substitur
//            sessions.put(token, username);
//            return token;

            String token = createToken(user);
            sessions.put(token, username);
            return token;
        }
        return null; // Authentication failed
    }

    private String createToken(User user) {
        long nowMillis = System.currentTimeMillis();
        long expMillis = nowMillis + 3600000; // Token valid for 1 hour
        Date now = new Date(nowMillis);
        Date exp = new Date(expMillis);

        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(key)
                .compact();
    }

    public User getUserByToken(String token) {
        String username = sessions.get(token);
        return username != null ? users.get(username) : null;
    }

    public void notifyUser(int userId, String message) {
        User user = users.values().stream().filter(u -> u.getId() == userId).findFirst().orElse(null);
        if (user != null) {
            // Implement notification logic here
            System.out.println("Notifying user " + user.getUsername() + ": " + message);
        }
    }
}
