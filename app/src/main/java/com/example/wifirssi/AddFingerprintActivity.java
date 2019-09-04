package com.example.wifirssi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AddFingerprintActivity extends AppCompatActivity {

    private EditText etAP0;
    private EditText etAP1;
    private EditText etAP2;
    private EditText etAP3;
    private EditText etRP;

    private TextView tvTestNo01;
    private TextView tvTestNo02;
    private TextView tvTestNo03;
    private TextView tvTestNo04;
    private TextView tvTestNo05;
    private TextView tvTestNo06;
    private TextView tvTestNo07;
    private TextView tvTestNo08;
    private TextView tvTestNo09;
    private TextView tvTestNo10;

    private TextView tvRP01;
    private TextView tvRP02;
    private TextView tvRP03;
    private TextView tvRP04;
    private TextView tvRP05;
    private TextView tvRP06;
    private TextView tvRP07;
    private TextView tvRP08;
    private TextView tvRP09;
    private TextView tvRP10;

    private TextView tvAP0Test01;
    private TextView tvAP0Test02;
    private TextView tvAP0Test03;
    private TextView tvAP0Test04;
    private TextView tvAP0Test05;
    private TextView tvAP0Test06;
    private TextView tvAP0Test07;
    private TextView tvAP0Test08;
    private TextView tvAP0Test09;
    private TextView tvAP0Test10;

    private TextView tvAP1Test01;
    private TextView tvAP1Test02;
    private TextView tvAP1Test03;
    private TextView tvAP1Test04;
    private TextView tvAP1Test05;
    private TextView tvAP1Test06;
    private TextView tvAP1Test07;
    private TextView tvAP1Test08;
    private TextView tvAP1Test09;
    private TextView tvAP1Test10;

    private TextView tvAP2Test01;
    private TextView tvAP2Test02;
    private TextView tvAP2Test03;
    private TextView tvAP2Test04;
    private TextView tvAP2Test05;
    private TextView tvAP2Test06;
    private TextView tvAP2Test07;
    private TextView tvAP2Test08;
    private TextView tvAP2Test09;
    private TextView tvAP2Test10;

    private TextView tvAP3Test01;
    private TextView tvAP3Test02;
    private TextView tvAP3Test03;
    private TextView tvAP3Test04;
    private TextView tvAP3Test05;
    private TextView tvAP3Test06;
    private TextView tvAP3Test07;
    private TextView tvAP3Test08;
    private TextView tvAP3Test09;
    private TextView tvAP3Test10;

    private Button btScanFingerprint;
    private Button btAddFingerprint;

    private WifiManager wifiManager;
    private ListView lvScanResults;
    private Button btScan;
    private List<ScanResult> scanResults;
    public ArrayList<AccessPoint> accessPoints;
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Add Fingerprint");
        setContentView(R.layout.activity_add_fingerprint);

        etAP0 = findViewById(R.id.etAP0);
        etAP1 = findViewById(R.id.etAP1);
        etAP2 = findViewById(R.id.etAP2);
        etAP3 = findViewById(R.id.etAP3);
        etRP = findViewById(R.id.etRP);

        tvTestNo01 = findViewById(R.id.tvTestNo01);
        tvTestNo02 = findViewById(R.id.tvTestNo02);
        tvTestNo03 = findViewById(R.id.tvTestNo03);
        tvTestNo04 = findViewById(R.id.tvTestNo04);
        tvTestNo05 = findViewById(R.id.tvTestNo05);
        tvTestNo06 = findViewById(R.id.tvTestNo06);
        tvTestNo07 = findViewById(R.id.tvTestNo07);
        tvTestNo08 = findViewById(R.id.tvTestNo08);
        tvTestNo09 = findViewById(R.id.tvTestNo09);
        tvTestNo10 = findViewById(R.id.tvTestNo10);

        tvRP01 = findViewById(R.id.tvRP01);
        tvRP02 = findViewById(R.id.tvRP02);
        tvRP03 = findViewById(R.id.tvRP03);
        tvRP04 = findViewById(R.id.tvRP04);
        tvRP05 = findViewById(R.id.tvRP05);
        tvRP06 = findViewById(R.id.tvRP06);
        tvRP07 = findViewById(R.id.tvRP07);
        tvRP08 = findViewById(R.id.tvRP08);
        tvRP09 = findViewById(R.id.tvRP09);
        tvRP10 = findViewById(R.id.tvRP10);

        tvAP0Test01 = findViewById(R.id.tvAP0Test01);
        tvAP0Test02 = findViewById(R.id.tvAP0Test02);
        tvAP0Test03 = findViewById(R.id.tvAP0Test03);
        tvAP0Test04 = findViewById(R.id.tvAP0Test04);
        tvAP0Test05 = findViewById(R.id.tvAP0Test05);
        tvAP0Test06 = findViewById(R.id.tvAP0Test06);
        tvAP0Test07 = findViewById(R.id.tvAP0Test07);
        tvAP0Test08 = findViewById(R.id.tvAP0Test08);
        tvAP0Test09 = findViewById(R.id.tvAP0Test09);
        tvAP0Test10 = findViewById(R.id.tvAP0Test10);

        tvAP1Test01 = findViewById(R.id.tvAP1Test01);
        tvAP1Test02 = findViewById(R.id.tvAP1Test02);
        tvAP1Test03 = findViewById(R.id.tvAP1Test03);
        tvAP1Test04 = findViewById(R.id.tvAP1Test04);
        tvAP1Test05 = findViewById(R.id.tvAP1Test05);
        tvAP1Test06 = findViewById(R.id.tvAP1Test06);
        tvAP1Test07 = findViewById(R.id.tvAP1Test07);
        tvAP1Test08 = findViewById(R.id.tvAP1Test08);
        tvAP1Test09 = findViewById(R.id.tvAP1Test09);
        tvAP1Test10 = findViewById(R.id.tvAP1Test10);

        tvAP2Test01 = findViewById(R.id.tvAP2Test01);
        tvAP2Test02 = findViewById(R.id.tvAP2Test02);
        tvAP2Test03 = findViewById(R.id.tvAP2Test03);
        tvAP2Test04 = findViewById(R.id.tvAP2Test04);
        tvAP2Test05 = findViewById(R.id.tvAP2Test05);
        tvAP2Test06 = findViewById(R.id.tvAP2Test06);
        tvAP2Test07 = findViewById(R.id.tvAP2Test07);
        tvAP2Test08 = findViewById(R.id.tvAP2Test08);
        tvAP2Test09 = findViewById(R.id.tvAP2Test09);
        tvAP2Test10 = findViewById(R.id.tvAP2Test10);

        tvAP3Test01 = findViewById(R.id.tvAP3Test01);
        tvAP3Test02 = findViewById(R.id.tvAP3Test02);
        tvAP3Test03 = findViewById(R.id.tvAP3Test03);
        tvAP3Test04 = findViewById(R.id.tvAP3Test04);
        tvAP3Test05 = findViewById(R.id.tvAP3Test05);
        tvAP3Test06 = findViewById(R.id.tvAP3Test06);
        tvAP3Test07 = findViewById(R.id.tvAP3Test07);
        tvAP3Test08 = findViewById(R.id.tvAP3Test08);
        tvAP3Test09 = findViewById(R.id.tvAP3Test09);
        tvAP3Test10 = findViewById(R.id.tvAP3Test10);

    }

    BroadcastReceiver rssiReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            scanResults = wifiManager.getScanResults();
            unregisterReceiver(this);

            for (ScanResult result : scanResults) {
                accessPoints.add(new AccessPoint(result.SSID, result.level));
                adapter.notifyDataSetChanged();
            }

            Toast.makeText(getApplicationContext(), "Scan complete", Toast.LENGTH_SHORT).show();
        }
    };

    public void scan() {
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
