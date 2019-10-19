package com.example.wifirssi.task;

import android.os.AsyncTask;
import android.util.Log;

import com.example.wifirssi.activity.LocateUserActivity;
import com.example.wifirssi.constant.Service;
import com.example.wifirssi.model.FloorLayout;
import com.example.wifirssi.model.Location;
import com.example.wifirssi.model.Path;
import com.google.gson.Gson;

import net.named_data.jndn.Data;
import net.named_data.jndn.Face;
import net.named_data.jndn.Interest;
import net.named_data.jndn.Name;
import net.named_data.jndn.OnData;
import net.named_data.jndn.OnTimeout;

public class NdnTask extends AsyncTask<String, Void, Void> {

    private String place;
    private Service service;
    private String prefix = "/ips/";
    private int detectedFloor;
    private int floor;
    private FloorLayout floorLayout;
    private Path path;
    private Location location;

    public NdnTask(String place, int floor, Service service) {
        this.place = place;
        this.service = service;
        this.floor = floor;
    }

    @Override
    protected Void doInBackground(String... params) {

        try {
            Face face = new Face("192.168.0.112");
            ReceiveData receiveData = new ReceiveData();
            String request = "";

            if (service == Service.LOCATE) {
                String observedRSSValues = params[0];
                request = service + "/" + place + "/" + floor + "/" + observedRSSValues;
                Log.d("NDNRequest", "requestLocation: " + request);
            }

            else if (service == Service.LOAD_MAP) {
                request =  service + "/" + place + "/" + floor;
                Log.d("NDNRequest", "requestMap: " + request);
            }

            else if (service == Service.NAVIGATE) {
                String navigation = params[0];
                request = service + "/" + place + "/" + floor + "/" + navigation;
                Log.d("NDNRequest", "requestNavigation: " + request);
            }

            else if (service == Service.DETECT_FLOOR) {
                float airPressure = Float.parseFloat(params[0]);
                request = service + "/" + place + "/" + floor + "/" + airPressure;
                Log.d("NDNRequest", "requestFloor: " + request);
            }

            Name name = new Name(prefix + request);
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

    class ReceiveData implements OnData, OnTimeout {
        int callbackCount = 0;

        @Override
        public void onData(Interest interest, Data data) {
            ++callbackCount;

            if (service == Service.LOCATE) {
                String json = data.getContent().toString();
                location = new Gson().fromJson(json, Location.class);
            }

            else if (service == Service.LOAD_MAP) {
                String json = data.getContent().toString();
                floorLayout = new Gson().fromJson(json, FloorLayout.class);
            }

            else if (service == Service.NAVIGATE) {
                String json = data.getContent().toString();
                path = new Gson().fromJson(json, Path.class);
            }

            else if (service == Service.DETECT_FLOOR) {
                detectedFloor = Integer.parseInt(data.getContent().toString());
                LocateUserActivity.etFloor.setText(detectedFloor + "");
                LocateUserActivity.floor = detectedFloor;
            }
        }

        @Override
        public void onTimeout(Interest interest) {
            ++callbackCount;
            System.out.println("Time out for interest " + interest.getName().toUri());
        }
    }
}
