package com.example.wifirssi.model;

public class FloorLayout {
    private final String place;
    private final int floor;
    private final int height;
    private final int width;

    public FloorLayout(String place, int floor, int height, int width, int[][] walls) {
        this.place = place;
        this.floor = floor;
        this.height = height;
        this.width = width;
        this.walls = walls;
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
}
