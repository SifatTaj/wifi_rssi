package com.example.wifirssi.model;

import java.util.ArrayList;

public class Location {

    private String location;
    private ArrayList<LocDistance> places;
    private float x, y;

    public Location(String location, ArrayList<LocDistance> places, float x, float y) {
        this.location = location;
        this.places = places;
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public String getLocation() {
        return location;
    }

    public ArrayList<LocDistance> getPlaces() {
        return places;
    }
}