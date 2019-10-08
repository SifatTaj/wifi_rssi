package model;

import java.io.Serializable;

public class FloorLayout implements Serializable {
    private static final long serialVersionUID = 898557456258L;
    private final int height;
    private final int width;
    private final int[][] walls;

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
