package com.example.wifirssi.task;

import android.os.AsyncTask;
import android.util.Log;

import com.example.wifirssi.LocateUserActivity;
import model.FloorLayout;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class TcpTask extends AsyncTask<String, Void, Void> {

    String place;
    String service;
    String location = "Waiting for response...";
    String address = "192.168.0.112";
    FloorLayout floorLayout;

    public TcpTask(String place, String service) {
        this.place = place;
        this.service = service;
    }

    private String requestLocation(String observedRSSValues) {

        try {
            String myLocation;

            Socket socket = new Socket(address, 5000);
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

            String request = service + "/" + place + "/" + observedRSSValues;
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
            String request = service + "/" + place;
            Log.d("TCPRequest", "requestMap: " + request);
            oos.writeUTF(request);
            oos.flush();
            floorLayout = (FloorLayout) ois.readObject();
            return floorLayout;

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
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {

        if (service.equalsIgnoreCase("location")) {
            LocateUserActivity.tvLocation.setText(location);
            String[] locationCoordinates = location.split(" ");
            try {
                int x = (int) Math.round(Double.parseDouble(locationCoordinates[0]));
                int y = (int) Math.round(Double.parseDouble(locationCoordinates[1]));
                LocateUserActivity.mapView.setLocation(x, y);
            } catch (NumberFormatException nfe) {
                nfe.printStackTrace();
            }
        }
        else if (service.equalsIgnoreCase("loadmap"))
            LocateUserActivity.mapView.generateMap(floorLayout);
    }
}
