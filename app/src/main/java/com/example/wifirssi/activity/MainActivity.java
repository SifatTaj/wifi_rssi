package com.example.wifirssi.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wifirssi.R;

import com.example.wifirssi.model.AccessPoint;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private WifiManager wifiManager;
    private ListView lvScanResults;
    private Button btScan;
    private List<ScanResult> scanResults;
    private ArrayList<AccessPoint> accessPoints;
    public static Set<AccessPoint> selectedAccessPoints;
    private ArrayAdapter adapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.btLocateActivity:
                Intent intent = new Intent(this, LocateUserActivity.class);
                startActivity(intent);
                return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvScanResults = findViewById(R.id.lvScanResults);
        btScan = findViewById(R.id.btScan);
        accessPoints = new ArrayList<>();
        selectedAccessPoints = new HashSet<>();
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_2, android.R.id.text1, accessPoints) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                text1.setText(accessPoints.get(position).getSsid());
                text2.setText(accessPoints.get(position).getRssi() + "dbm");

                for(AccessPoint ap : selectedAccessPoints) {
                    if (ap.getMac().equals(accessPoints.get(position).getMac())) {
                        text1.setTextColor(Color.GREEN);
                        text2.setTextColor(Color.GREEN);
                    }
                }

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

        lvScanResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedAccessPoints.add(accessPoints.get(i));
                Toast.makeText(getApplicationContext(), accessPoints.get(i).getSsid() + " selected as AP", Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
            }
        });

    }

    BroadcastReceiver rssiReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            scanResults = wifiManager.getScanResults();
            unregisterReceiver(this);

            for (ScanResult result : scanResults) {
                accessPoints.add(new AccessPoint(result.SSID, result.BSSID, result.level));
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
