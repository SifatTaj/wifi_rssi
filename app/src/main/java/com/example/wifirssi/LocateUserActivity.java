package com.example.wifirssi;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.wifirssi.model.AccessPoint;
import com.example.wifirssi.task.TcpTask;
import com.example.wifirssi.view.MapView;

import java.util.List;
import java.util.Set;

public class LocateUserActivity extends AppCompatActivity {

    MapView mapView;
    Button btLocateUser;
    TextView tvLocation;
    Set<AccessPoint> selectedAccessPoints;
    WifiManager wifiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locate_user);
        setTitle("Locate User");

        selectedAccessPoints = MainActivity.selectedAccessPoints;
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        btLocateUser = findViewById(R.id.btLocateUser);
        tvLocation = findViewById(R.id.tvLocation);
        mapView = findViewById(R.id.mapView);

        btLocateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scan();
                mapView.setLocation(2, 3);
                mapView.drawNavigation(2, 3, 3,3);
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

            Toast.makeText(getApplicationContext(), observedRssValue, Toast.LENGTH_SHORT).show();
            new TcpTask(tvLocation).execute(observedRssValue);
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
