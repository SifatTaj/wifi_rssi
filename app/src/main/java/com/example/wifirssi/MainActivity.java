package com.example.wifirssi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

class AccessPoint {
    String ssid;
    int rssi;

    public AccessPoint(String ssid, int rssi) {
        this.ssid = ssid;
        this.rssi = rssi;
    }
}

public class MainActivity extends AppCompatActivity {

    private WifiManager wifiManager;
    private ListView lvScanResults;
    private Button btScan;
    private List<ScanResult> scanResults;
    private ArrayList<AccessPoint> accessPoints;
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvScanResults = findViewById(R.id.lvScanResults);
        btScan = findViewById(R.id.btScan);
        accessPoints = new ArrayList<>();
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_2, accessPoints);
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_2, android.R.id.text1, accessPoints) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                text1.setText(accessPoints.get(position).ssid);
                text2.setText(accessPoints.get(position).rssi + " dbm");
                return view;
            }
        };
        lvScanResults.setAdapter(adapter);

        btScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scan();
            }
        });
        scan();
    }

    BroadcastReceiver rssiReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            scanResults = wifiManager.getScanResults();
            unregisterReceiver(this);

            Log.d("scan: ", "" + scanResults.size());

            for (ScanResult result : scanResults) {
                accessPoints.add(new AccessPoint(result.SSID, result.level));
                adapter.notifyDataSetChanged();
            }

            Toast.makeText(getApplicationContext(), "Scan complete", Toast.LENGTH_SHORT).show();
        }
    };

    private void scan() {
        if (wifiManager.isWifiEnabled()) {
            accessPoints.clear();
            registerReceiver(rssiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
            wifiManager.startScan();
            Toast.makeText(this, "Scanning...", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(this, "Please enable Wifi", Toast.LENGTH_SHORT);
    }
}
