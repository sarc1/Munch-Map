package com.example.munch_map;

public class BarangayItem {
    private String name;

    public BarangayItem(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
