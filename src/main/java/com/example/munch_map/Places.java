package com.example.munch_map;

public class Places {


    String placeID;
    String placeName;
    String username; // Added username
    String type;
    String about;
    String barangay_name;

    public Places(String barangay_name, String placeID, String placeName, String username, String about) {
        this.placeID = placeID;
        this.placeName = placeName;
        this.username = username;
        this.about = about;
        this.barangay_name = barangay_name;
    }

    public String getPlaceId() { return placeID; }
    public String getplaceName() { return placeName; }
    public String getUsername() { return username; }
    public String getAbout() { return about; }
    public String getType() { return type; }
    public String getBarangay_name() { return barangay_name; }
}
