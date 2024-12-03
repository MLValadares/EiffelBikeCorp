package edu.m2sia.eiffelbikecorp.model;

import java.time.LocalDateTime;
import java.util.Date;

public class Rating {
    private int score;
    private String comment;
    private int userId;
    private Date timestamp;

    // Constructor
    public Rating(int score, String comment, int userId) {
        this.score = score;
        this.comment = comment;
        this.userId = userId;
        this.timestamp = new Date();
    }

    // Getters and Setters
    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
    

    @Override
    public String toString() {
        return "Rating{" +
                "score=" + score +
                ", comment='" + comment + '\'' +
                ", userId=" + userId +
                ", timestamp=" + timestamp +
                '}';
    }
}
