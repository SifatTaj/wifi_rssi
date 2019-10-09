package com.example.wifirssi.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wifirssi.model.AccessPoint;

import com.example.wifirssi.R;
import com.example.wifirssi.task.TcpTask;
import com.example.wifirssi.view.MapView;

import java.util.List;
import java.util.Set;

public class LocateUserActivity extends AppCompatActivity {

    public static MapView mapView;
    Button btLocateUser, btLoadMap, btNavigate;
    public static TextView tvLocation, tvMapDescription;
    EditText etPlace, etFloor, etDest;
    Set<AccessPoint> selectedAccessPoints;
    WifiManager wifiManager;

    String place;
    String currentLocation = "1_1_3";
    int floor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locate_user);
        setTitle("Locate User");

        selectedAccessPoints = MainActivity.selectedAccessPoints;
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        btLocateUser = findViewById(R.id.btLocateUser);
        btLoadMap = findViewById(R.id.btLoadMap);
        btNavigate = findViewById(R.id.btNavigate);
        tvLocation = findViewById(R.id.tvLocation);
        tvMapDescription = findViewById(R.id.tvMapDescription);
        mapView = findViewById(R.id.mapView);
        etPlace = findViewById(R.id.etPlace);
        etFloor = findViewById(R.id.etFloor);
        etDest = findViewById(R.id.etDest);

        btLocateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                place = etPlace.getText().toString();
                floor = Integer.parseInt(etFloor.getText().toString());
                scan();
            }
        });

        btLoadMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                place = etPlace.getText().toString();
                floor = Integer.parseInt(etFloor.getText().toString());
                new TcpTask(place, floor,"loadmap").execute();
            }
        });

        btNavigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                place = etPlace.getText().toString();
                floor = Integer.parseInt(etFloor.getText().toString());
                String navigation = currentLocation + "_" + etDest.getText().toString();
                new TcpTask(place, floor,"navigate").execute(navigation);
            }
        });
    }

    BroadcastReceiver rssiReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String observedRssValue = "";
            List<ScanResult> scanResults = wifiManager.getScanResults();
            unregisterReceiver(this);

            for (ScanResult result : scanResults) {
                for(AccessPoint ap : selectedAccessPoints) {
                    if(ap.getMac().equals(result.BSSID))
                        ap.setRssi(result.level);
                }
            }

            for(AccessPoint ap : selectedAccessPoints)
                observedRssValue = observedRssValue + ap.getRssi() + "_";

            if (observedRssValue.equalsIgnoreCase(""))
                observedRssValue = "-44_-62_-73_-60_";

            Toast.makeText(getApplicationContext(), observedRssValue, Toast.LENGTH_SHORT).show();
            new TcpTask(place, floor, "location").execute(observedRssValue);
        }
    };

    private void scan() {
        if (wifiManager.isWifiEnabled()) {
            registerReceiver(rssiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
            wifiManager.startScan();
            Toast.makeText(this, "Scanning...", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(this, "Please enable Wifi", Toast.LENGTH_SHORT);
    }
}
