package com.example.wifirssi.task;

import android.os.AsyncTask;
import android.util.Log;

import com.example.wifirssi.activity.LocateUserActivity;
import com.example.wifirssi.constant.Service;
import com.example.wifirssi.model.Location;
import com.example.wifirssi.model.Path;
import com.google.gson.Gson;

import com.example.wifirssi.model.FloorLayout;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class TcpTask extends AsyncTask<String, Void, Void> {

    private String place;
    private Service service;
    private String address = "192.168.0.112";
    private int floor;
    private int detectedFloor;
    private FloorLayout floorLayout;
    private Path path;
    private Location location;

    public TcpTask(String place, int floor, Service service) {
        this.place = place;
        this.service = service;
        this.floor = floor;
    }

    private Location requestLocation(String observedRSSValues) {

        try {
            Socket socket = new Socket(address, 5000);
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

            String request = service + "/" + place + "/" + floor + "/" + observedRSSValues;
            Log.d("TCPRequest", "requestLocation: " + request);

            try {
                oos.writeUTF(request);
                oos.flush();
                String json = (String) ois.readObject();
                location = new Gson().fromJson(json, Location.class);
                return location;

            } catch (Exception e) {
                e.printStackTrace();

            } finally {
                socket.close();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return null;
    }

    private FloorLayout requestMap() {
        FloorLayout floorLayout;
        try {
            Socket socket = new Socket(address, 5000);
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            String request = service + "/" + place + "/" + floor;
            Log.d("TCPRequest", "requestMap: " + request);
            oos.writeUTF(request);
            oos.flush();
            String json = (String) ois.readObject();
            floorLayout = new Gson().fromJson(json, FloorLayout.class);
            return floorLayout;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Path requestNavigation(String navigation) {
        try {
            Socket socket = new Socket(address, 5000);
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            String request = service + "/" + place + "/" + floor + "/" + navigation;
            Log.d("TCPRequest", "requestMap: " + request);
            oos.writeUTF(request);
            oos.flush();
            String json = (String) ois.readObject();
            path = new Gson().fromJson(json, Path.class);
            return path;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private int requestFloor(float airPressure) {
        try {
            Socket socket = new Socket(address, 5000);
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            String request = service + "/" + place + "/" + floor + "/" + airPressure;
            Log.d("TCPRequest", "requestFloor: " + request);
            oos.writeUTF(request);
            oos.flush();
            int floor = (int) ois.readObject();
            return floor;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    protected void onPreExecute() {
        if (service == Service.LOCATE) {
            LocateUserActivity.tvLocation.setText("Waiting for response...");
        }
    }

    @Override
    protected Void doInBackground(String... params) {
        if (service == Service.LOCATE) {
            String observedRSSValue = params[0];
            location = requestLocation(observedRSSValue);
        }
        else if (service == Service.LOAD_MAP)
            floorLayout = requestMap();
        else if (service == Service.NAVIGATE) {
            String navigation = params[0];
            requestNavigation(navigation);
        }
        else if (service == Service.DETECT_FLOOR) {
            float airPressure = Float.parseFloat(params[0]);
            detectedFloor = requestFloor(airPressure);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {

        if (service == Service.LOCATE) {
            LocateUserActivity.tvLocation.setText(location.getLocation());
            LocateUserActivity.mapView.setLocation(location.getX(), location.getY());
            int x = Math.round(location.getX());
            int y = Math.round(location.getY());
            LocateUserActivity.currentLocation = x + "_" + y + "_" + floor;
        }

        else if (service == Service.LOAD_MAP) {
            LocateUserActivity.mapView.generateMap(floorLayout);
            LocateUserActivity.tvMapDescription.setText("Place: " + floorLayout.getPlace() + "\nFloor: " + floorLayout.getFloor());
        }

        else if (service == Service.NAVIGATE) {
            LocateUserActivity.mapView.drawNavigation(path);
        }

        else if (service == Service.DETECT_FLOOR) {
            LocateUserActivity.etFloor.setText(detectedFloor + "");
            LocateUserActivity.floor = detectedFloor;
        }
    }
}
