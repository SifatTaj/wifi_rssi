package com.example.wifirssi.model;

public class FloorLayout {
    private int height;
    private int width;
    private int[][] walls;

    public FloorLayout(int height, int width, int[][] walls) {
        this.height = height;
        this.width = width;
        this.walls = walls;
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
