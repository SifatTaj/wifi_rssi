package com.example.wifirssi.task;

import android.os.AsyncTask;
import android.util.Log;

import com.example.wifirssi.activity.LocateUserActivity;
import com.example.wifirssi.model.Path;
import com.google.gson.Gson;

import com.example.wifirssi.model.FloorLayout;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class TcpTask extends AsyncTask<String, Void, Void> {

    String place;
    String service;
    String location = "Waiting for response...";
    String address = "192.168.0.112";
    int floor;
    FloorLayout floorLayout;
    Path path;

    public TcpTask(String place, int floor, String service) {
        this.place = place;
        this.service = service;
        this.floor = floor;
    }

    private String requestLocation(String observedRSSValues) {

        try {
            String myLocation;

            Socket socket = new Socket(address, 5000);
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

            String request = service + "/" + place + "/" + floor + "/" + observedRSSValues;
            Log.d("TCPRequest", "requestLocation: " + request);

            try {
                oos.writeUTF(request);
                oos.flush();
                myLocation = ois.readUTF();
                return myLocation;

            } catch (Exception e) {
                e.printStackTrace();

            } finally {
                socket.close();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return "Server Down";
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

    @Override
    protected void onPreExecute() {
        if (service.equalsIgnoreCase("location")) {
            LocateUserActivity.tvLocation.setText(location);
        }
    }

    @Override
    protected Void doInBackground(String... params) {
        if (service.equalsIgnoreCase("location")) {
            String observedRSSValue = params[0];
            location = requestLocation(observedRSSValue);
        }
        else if (service.equalsIgnoreCase("loadmap"))
            floorLayout = requestMap();
        else if (service.equalsIgnoreCase("navigate")) {
            String navigation = params[0];
            requestNavigation(navigation);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {

        if (service.equalsIgnoreCase("location")) {
            LocateUserActivity.tvLocation.setText(location);
            String[] locationCoordinates = location.split(" ");
            try {
                float x = (Float.parseFloat(locationCoordinates[0]));
                float y = (Float.parseFloat(locationCoordinates[1]));
                LocateUserActivity.mapView.setLocation(x, y);
            } catch (NumberFormatException nfe) {
                nfe.printStackTrace();
            }
        }
        else if (service.equalsIgnoreCase("loadmap")) {
            LocateUserActivity.mapView.generateMap(floorLayout);
            LocateUserActivity.tvMapDescription.setText("Place: " + floorLayout.getPlace() + "\nFloor: " + floorLayout.getFloor());
        }

        else if (service.equalsIgnoreCase("navigate")) {
            LocateUserActivity.mapView.drawNavigation(path);
        }
    }
}
