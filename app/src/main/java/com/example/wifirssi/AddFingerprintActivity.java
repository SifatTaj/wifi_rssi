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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wifirssi.Model.AccessPoint;

import java.util.ArrayList;
import java.util.List;

public class AddFingerprintActivity extends AppCompatActivity {

    int i = 0;
    int ap0sum = 0;
    int ap1sum = 0;
    int ap2sum = 0;
    int ap3sum = 0;

    double ap0mean = 0;
    double ap1mean = 0;
    double ap2mean = 0;
    double ap3mean = 0;

    private EditText etAP0;
    private EditText etAP1;
    private EditText etAP2;
    private EditText etAP3;
    private EditText etRP;

    private TextView[] tvTestNo = new TextView[11];

    private TextView[] tvRP = new TextView[11];

    private TextView[] tvAP0Test = new TextView[11];

    private TextView[] tvAP1Test = new TextView[11];

    private TextView[] tvAP2Test = new TextView[11];

    private TextView[] tvAP3Test = new TextView[11];

    private Button btScanFingerprint;
    private Button btAddFingerprint;

    private WifiManager wifiManager;
    private List<ScanResult> scanResults;
    public ArrayList<AccessPoint> accessPoints;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Add Fingerprint");
        setContentView(R.layout.activity_add_fingerprint);

        accessPoints = new ArrayList<>();

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        btScanFingerprint = findViewById(R.id.btScanFingerprint);
        btAddFingerprint = findViewById(R.id.btAddFingerprint);

        etAP0 = findViewById(R.id.etAP0);
        etAP1 = findViewById(R.id.etAP1);
        etAP2 = findViewById(R.id.etAP2);
        etAP3 = findViewById(R.id.etAP3);
        etRP = findViewById(R.id.etRP);

        tvTestNo[1] = findViewById(R.id.tvTestNo01);
        tvTestNo[2] = findViewById(R.id.tvTestNo02);
        tvTestNo[3] = findViewById(R.id.tvTestNo03);
        tvTestNo[4] = findViewById(R.id.tvTestNo04);
        tvTestNo[5] = findViewById(R.id.tvTestNo05);
        tvTestNo[6] = findViewById(R.id.tvTestNo06);
        tvTestNo[7] = findViewById(R.id.tvTestNo07);
        tvTestNo[8] = findViewById(R.id.tvTestNo08);
        tvTestNo[9] = findViewById(R.id.tvTestNo09);
        tvTestNo[10] = findViewById(R.id.tvTestNo10);

        tvRP[1] = findViewById(R.id.tvRP01);
        tvRP[2] = findViewById(R.id.tvRP02);
        tvRP[3] = findViewById(R.id.tvRP03);
        tvRP[4] = findViewById(R.id.tvRP04);
        tvRP[5] = findViewById(R.id.tvRP05);
        tvRP[6] = findViewById(R.id.tvRP06);
        tvRP[7] = findViewById(R.id.tvRP07);
        tvRP[8]= findViewById(R.id.tvRP08);
        tvRP[9] = findViewById(R.id.tvRP09);
        tvRP[10] = findViewById(R.id.tvRP10);

        tvAP0Test[1] = findViewById(R.id.tvAP0Test01);
        tvAP0Test[2] = findViewById(R.id.tvAP0Test02);
        tvAP0Test[3] = findViewById(R.id.tvAP0Test03);
        tvAP0Test[4] = findViewById(R.id.tvAP0Test04);
        tvAP0Test[5] = findViewById(R.id.tvAP0Test05);
        tvAP0Test[6] = findViewById(R.id.tvAP0Test06);
        tvAP0Test[7] = findViewById(R.id.tvAP0Test07);
        tvAP0Test[8] = findViewById(R.id.tvAP0Test08);
        tvAP0Test[9] = findViewById(R.id.tvAP0Test09);
        tvAP0Test[10] = findViewById(R.id.tvAP0Test10);

        tvAP1Test[1] = findViewById(R.id.tvAP1Test01);
        tvAP1Test[2] = findViewById(R.id.tvAP1Test02);
        tvAP1Test[3] = findViewById(R.id.tvAP1Test03);
        tvAP1Test[4] = findViewById(R.id.tvAP1Test04);
        tvAP1Test[5] = findViewById(R.id.tvAP1Test05);
        tvAP1Test[6] = findViewById(R.id.tvAP1Test06);
        tvAP1Test[7] = findViewById(R.id.tvAP1Test07);
        tvAP1Test[8] = findViewById(R.id.tvAP1Test08);
        tvAP1Test[9] = findViewById(R.id.tvAP1Test09);
        tvAP1Test[10] = findViewById(R.id.tvAP1Test10);

        tvAP2Test[1] = findViewById(R.id.tvAP2Test01);
        tvAP2Test[2] = findViewById(R.id.tvAP2Test02);
        tvAP2Test[3] = findViewById(R.id.tvAP2Test03);
        tvAP2Test[4] = findViewById(R.id.tvAP2Test04);
        tvAP2Test[5] = findViewById(R.id.tvAP2Test05);
        tvAP2Test[6] = findViewById(R.id.tvAP2Test06);
        tvAP2Test[7] = findViewById(R.id.tvAP2Test07);
        tvAP2Test[8] = findViewById(R.id.tvAP2Test08);
        tvAP2Test[9] = findViewById(R.id.tvAP2Test09);
        tvAP2Test[10] = findViewById(R.id.tvAP2Test10);

        tvAP3Test[1] = findViewById(R.id.tvAP3Test01);
        tvAP3Test[2] = findViewById(R.id.tvAP3Test02);
        tvAP3Test[3] = findViewById(R.id.tvAP3Test03);
        tvAP3Test[4] = findViewById(R.id.tvAP3Test04);
        tvAP3Test[5] = findViewById(R.id.tvAP3Test05);
        tvAP3Test[6] = findViewById(R.id.tvAP3Test06);
        tvAP3Test[7] = findViewById(R.id.tvAP3Test07);
        tvAP3Test[8] = findViewById(R.id.tvAP3Test08);
        tvAP3Test[9] = findViewById(R.id.tvAP3Test09);
        tvAP3Test[10] = findViewById(R.id.tvAP3Test10);

        btScanFingerprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(i < 10) {
                    ++i;
                    tvTestNo[i].setText("     " + i + "  ");
                    tvRP[i].setText("     " + etRP.getText().toString() + " ");
                    scan();
                }
                else {
                    ap0mean = (double)ap0sum / 10;
                    ap1mean = (double)ap1sum / 10;
                    ap2mean = (double)ap2sum / 10;
                    ap3mean = (double)ap3sum / 10;
                    ((TextView)findViewById(R.id.tvMeanAP0)).setText(ap0mean + "");
                    ((TextView)findViewById(R.id.tvMeanAP1)).setText(ap1mean + "");
                    ((TextView)findViewById(R.id.tvMeanAP2)).setText(ap2mean + "");
                    ((TextView)findViewById(R.id.tvMeanAP3)).setText(ap3mean + "");
                }
            }
        });

    }

    BroadcastReceiver rssiReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            scanResults = wifiManager.getScanResults();
            unregisterReceiver(this);

            for (ScanResult result : scanResults) {
                accessPoints.add(new AccessPoint(result.SSID, result.level, result.BSSID, result.frequency));
            }

            record();

            Toast.makeText(getApplicationContext(), "Scan complete", Toast.LENGTH_SHORT).show();
        }
    };

    void scan() {
        if (wifiManager.isWifiEnabled()) {
            accessPoints.clear();
            registerReceiver(rssiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
            wifiManager.startScan();
            Toast.makeText(this, "Scanning...", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(this, "Please enable Wifi", Toast.LENGTH_SHORT);
    }

    void record() {
        for(AccessPoint ap : accessPoints) {
            if((ap.ssid).equalsIgnoreCase(etAP0.getText().toString())) {
                tvAP0Test[i].setText(ap.rssi + "");
                ap0sum = ap0sum + ap.rssi;
            }
            if((ap.ssid).equalsIgnoreCase(etAP1.getText().toString())) {
                tvAP1Test[i].setText(ap.rssi + "");
                ap1sum = ap1sum + ap.rssi;
            }
            if((ap.ssid).equalsIgnoreCase(etAP2.getText().toString())) {
                tvAP2Test[i].setText(ap.rssi + "");
                ap2sum = ap2sum + ap.rssi;
            }
            if((ap.ssid).equalsIgnoreCase(etAP3.getText().toString())) {
                tvAP3Test[i].setText(ap.rssi + "");
                ap3sum = ap3sum + ap.rssi;
            }
        }
    }
}
