package com.example.wifirssi.model;

import java.util.List;

public class FloorLayout {
    private String place;
    private int floor;
    private int height;
    private int width;
    private int exitx;
    private int exity;
    private List<AccessPoint> aps;

    public FloorLayout(String place, int floor, int height, int width, int exitx, int exity, int[][] walls, List<AccessPoint> aps) {
        this.place = place;
        this.floor = floor;
        this.height = height;
        this.width = width;
        this.exitx = exitx;
        this.exity = exity;
        this.walls = walls;
        this.aps = aps;
    }

    private final int[][] walls;

    public String getPlace() {
        return place;
    }

    public int getFloor() {
        return floor;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int[][] getWalls() {
        return walls;
    }

    public int getExitx() {
        return exitx;
    }

    public int getExity() {
        return exity;
    }

    public List<AccessPoint> getAps() {
        return aps;
    }
}
