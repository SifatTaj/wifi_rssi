package com.example.wifirssi.task;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.example.wifirssi.LocateUserActivity;
import com.example.wifirssi.model.FloorLayout;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class TcpTask extends AsyncTask<String, Void, Void> {

    String place;
    String service;
    String location = "Waiting for response...";
    String address = "192.168.0.112";

    public TcpTask(String place, String service) {
        this.place = place;
        this.service = service;
    }

    private String requestLocation(String observedRSSValues) {

        try {
            String myLocation;

            Socket socket = new Socket(address, 5000);
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

            String request = service + "/" + place + "/" + observedRSSValues;
            Log.d("TCPRequest", "requestLocation: " + request);

            try {
                dos.writeUTF(request);
                myLocation = dis.readUTF();
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        LocateUserActivity.tvLocation.setText(location);
    }

    @Override
    protected Void doInBackground(String... params) {
        String observedRSSValue = params[0];
        location = requestLocation(observedRSSValue);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        LocateUserActivity.tvLocation.setText(location);
        String[] locationCoordinates = location.split(" ");
        int x = (int) Math.round(Double.parseDouble(locationCoordinates[0]));
        int y = (int) Math.round(Double.parseDouble(locationCoordinates[1]));
        LocateUserActivity.mapView.setLocation(x,y);
    }
}
