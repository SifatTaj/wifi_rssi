package com.example.wifirssi.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.RequiresApi;

import model.FloorLayout;

public class MapView extends View {

    Paint paint;
    int scale = 2;
    int height = 0;
    int width = 0;
    int[][] walls;
    int x = 99;
    int y = 99;
    int x0 = 99;
    int y0 = 99;
    int x1 = 99;
    int y1 = 99;
    int dotColor;

    public MapView(Context context) {
        super(context);
        init(null);
    }

    public MapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public MapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MapView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        paint = new Paint();
        dotColor = Color.GRAY;
    }

    public void setLocation(int x, int y) {
        this.x = x * scale;
        this.y = y * scale;
        postInvalidate();
    }

    public void drawNavigation(int x0, int y0, int x1, int y1) {
        this.x0 = x0;
        this.y0 = y0;
        this.x1 = x1;
        this.y1 = y1;
        postInvalidate();
    }

    public void generateMap(FloorLayout floorLayout) {
        height = floorLayout.getHeight();
        width = floorLayout.getWidth();
        walls = floorLayout.getWalls();
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float unit = getWidth() / ((float) width - 1) - 8;

        canvas.save();
        canvas.translate(0, getHeight());
        canvas.scale(1, -1);

        paint.setStrokeWidth(10);
        canvas.drawLine(30 + (unit * x0), 30 + (unit * y0), 30 + (unit * x1), 30 + (unit * y1), paint);

        for (int j = 0; j < height; j++)
            for (int i = 0; i < width; i++) {
                dotColor = (i == x & j == y) ? Color.GREEN : Color.GRAY;
                paint.setColor(dotColor);
                canvas.drawCircle(30 + (unit * i), 30 + (unit * j), 20, paint);
            }

        if (walls != null) {
            for (int[] wall : walls) {
                paint.setColor(Color.GRAY);
//                canvas.drawCircle(30 + (unit * wall[0]), 30 + (unit * wall[1]), 20, paint);
                float top = 30 + (unit * wall[1]) - 40;
                float left = 30 + (unit * wall[0]) - 40;
                float right = left + 120;
                float bottom = top + 120;
                canvas.drawRect(left, top, right, bottom, paint);
            }
        }

        canvas.restore();
    }
}
