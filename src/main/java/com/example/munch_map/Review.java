package com.example.munch_map;

public class Review {


    String reviewID;
    String place;
    String username;
    double rating;
    String comment;

    Review(String reviewID, String place, double rating, String comment, String username) {
        this.reviewID = reviewID;
        this.place = place;
        this.rating = rating;
        this.comment = comment;
        this.username = username;
    }


    Review(String reviewID, String place, String username, float rating, String comment) {
        this.reviewID = reviewID;
        this.place = place;
        this.username = username;
        this.rating = rating;
        this.comment = comment;
    }

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

    public String getReviewID() { return reviewID;
    }
}
