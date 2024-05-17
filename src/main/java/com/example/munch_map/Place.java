package com.example.munch_map;

public class Place {

    private int placeId;
    private String placeName;
    private int barangayId; // Foreign key referencing barangay table

    // Getters and Setters
    public int getPlaceId() {
        return placeId;
    }

    public void setPlaceId(int placeId) {
        this.placeId = placeId;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public int getBarangayId() {
        return barangayId;
    }

    public void setBarangayId(int barangayId) {
        this.barangayId = barangayId;
    }

    // Optional Constructor (can be added if needed)
    public Place(int placeId, String placeName, int barangayId) {
        this.placeId = placeId;
        this.placeName = placeName;
        this.barangayId = barangayId;
    }
}