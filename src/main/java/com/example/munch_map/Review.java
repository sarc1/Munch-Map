package com.example.munch_map;

public class Review {
    private final double rating;
    private final String comment;
    private final String username;

    Review(double rating, String comment, String username) {
        this.rating = rating;
        this.comment = comment;
        this.username = username;
    }

    public double getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public String getUsername() {
        return username;
    }
}
