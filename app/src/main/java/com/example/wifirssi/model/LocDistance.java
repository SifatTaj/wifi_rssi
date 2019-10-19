package com.example.wifirssi.model;

public class LocDistance implements Comparable<LocDistance> {
    private double distance;
    private String location;
    private String name;

    public LocDistance(double distance, String location, String name) {
        this.distance = distance;
        this.location = location;
        this.name = name;
    }

    public double getDistance() {
        return distance;
    }

    public String getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    @Override
    public int compareTo(LocDistance obj) {
        //ascending
        if (distance == obj.getDistance())
            return 0;
        else if (distance > obj.getDistance())
            return 1;
        else
            return -1;
    }
}