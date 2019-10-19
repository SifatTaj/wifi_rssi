package com.example.wifirssi.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.RequiresApi;

import com.example.wifirssi.activity.LocateUserActivity;
import com.example.wifirssi.model.FloorLayout;
import com.example.wifirssi.model.Node;
import com.example.wifirssi.model.Path;

import java.util.ArrayList;
import java.util.List;

public class MapView extends View {

    Paint paint;
    int scale = 2;
    int height = 0;
    int width = 0;
    int[][] walls;
    float x = 99;
    float y = 99;
    int x0 = 99;
    int y0 = 99;
    int x1 = 99;
    int y1 = 99;
    int dotColor;
    List<Node> pathNodes = new ArrayList<>();

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

    public void setLocation(float x, float y) {
        this.x = (float) Math.round((x * scale));
        this.y = (float) Math.round((y * scale));
        postInvalidate();
    }

    public void drawNavigation(Path path) {
        pathNodes = path.getNodes();
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
        float unit = getWidth() / ((float) width - 1);
        unit = unit - (unit * .05f);

        float dotRadius = unit * .2f;
        float padding = unit * .3f;

        canvas.save();
        canvas.translate(0, getHeight());
        canvas.scale(1, -1);

        paint.setStrokeWidth(unit * .05f);
        for (int i = 0 ; i < pathNodes.size() - 1 ; ++i) {
            canvas.drawLine(padding + (unit * pathNodes.get(i).getX()), padding + (unit * pathNodes.get(i).getY()), padding + (unit * pathNodes.get(i + 1).getX()), padding + (unit * pathNodes.get(i + 1).getY()), paint);
        }

        dotColor = Color.GRAY;
        for (int j = 0; j < height; j++)
            for (int i = 0; i < width; i++) {
                paint.setColor(dotColor);
                canvas.drawCircle(padding + (unit * i), padding + (unit * j), dotRadius, paint);
            }

        paint.setColor(Color.GREEN);
        canvas.drawCircle(padding + (unit * x), padding + (unit * y), dotRadius, paint);

        if (walls != null) {
            for (int[] wall : walls) {
                paint.setColor(Color.GRAY);
                float top = padding + (unit * wall[1]) - unit * .5f;
                float left = padding + (unit * wall[0]) - unit * .5f;
                float right = left + unit;
                float bottom = top + unit;
                canvas.drawRect(left, top, right, bottom, paint);
            }
        }

        canvas.restore();
    }
}
