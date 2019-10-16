package com.example.wifirssi.task;

import android.os.AsyncTask;
import android.util.Log;

import com.example.wifirssi.activity.LocateUserActivity;
import com.example.wifirssi.model.FloorLayout;
import com.example.wifirssi.model.Path;
import com.google.gson.Gson;

import net.named_data.jndn.Data;
import net.named_data.jndn.Face;
import net.named_data.jndn.Interest;
import net.named_data.jndn.Name;
import net.named_data.jndn.OnData;
import net.named_data.jndn.OnTimeout;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class NdnTask extends AsyncTask<String, Void, Void> {

    String place;
    String service;
    String location = "Waiting for response...";
    String address = "192.168.0.112";
    int floor;
    FloorLayout floorLayout;
    Path path;

    public NdnTask(String place, int floor, String service) {
        this.place = place;
        this.service = service;
        this.floor = floor;
    }

    @Override
    protected Void doInBackground(String... params) {

        try {
            Face face = new Face();
            ReceiveData receiveData = new ReceiveData();
            String request = "";

            if (service.equalsIgnoreCase("location")) {
                String observedRSSValues = params[0];
                request = service + "/" + place + "/" + floor + "/" + observedRSSValues;
                Log.d("NDNRequest", "requestLocation: " + request);
            }

            else if (service.equalsIgnoreCase("loadmap")) {
                request = service + "/" + place + "/" + floor;
                Log.d("NDNRequest", "requestMap: " + request);
            }

            else if (service.equalsIgnoreCase("navigate")) {
                String navigation = params[0];
                request = service + "/" + place + "/" + floor + "/" + navigation;
            }

            Name name = new Name("/ips/" + request);
            face.expressInterest(name, receiveData, receiveData);

            while (receiveData.callbackCount < 1) {
                face.processEvents();
                Thread.sleep(5);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (service.equalsIgnoreCase("location")) {
            LocateUserActivity.tvLocation.setText(location);
        }

        else if (service.equalsIgnoreCase("loadmap")) {
            LocateUserActivity.mapView.generateMap(floorLayout);
        }

        else if (service.equalsIgnoreCase("navigate")) {
            LocateUserActivity.mapView.drawNavigation(path);
        }
    }

    class ReceiveData implements OnData, OnTimeout {
        int callbackCount = 0;

        @Override
        public void onData(Interest interest, Data data) {
            ++callbackCount;

            if (service.equalsIgnoreCase("location")) {
                location = data.getContent().toString();
            }
            else if (service.equalsIgnoreCase("loadmap")) {
                String json = data.getContent().toString();
                floorLayout = new Gson().fromJson(json, FloorLayout.class);
            }
            else if (service.equalsIgnoreCase("navigate")) {
                String json = data.getContent().toString();
                path = new Gson().fromJson(json, Path.class);
            }
        }

        @Override
        public void onTimeout(Interest interest) {
            ++callbackCount;
            System.out.println("Time out for interest " + interest.getName().toUri());
        }
    }
}
