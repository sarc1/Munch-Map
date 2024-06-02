package com.example.munch_map;

public class BarangayItem {
    private int id;

    public int getId() {
        return id;
    }

    private String name;

    public BarangayItem(int id, String name) {
        this.id = id;
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
