package com.example.munch_map;

public class PlaceDetails {
    private final String name;
    private final String type;
    private final String address;
    private final String landmark;
    private final String about;
    private final String avgRating;

    PlaceDetails(String name,  String avgRating, String type, String address, String landmark, String about) {
        this.name = name;
        this.type = type;
        this.address = address;
        this.landmark = landmark;
        this.about = about;
        this.avgRating = avgRating;
    }

    public String getName() {
        return name;
    }

    public String getAvgRating() {
        return avgRating;
    }

    public String getType() {
        return type;
    }

    public String getAddress() {
        return address;
    }

    public String getLandmark() {
        return landmark;
    }

    public String getAbout() {
        return about;
    }
}
