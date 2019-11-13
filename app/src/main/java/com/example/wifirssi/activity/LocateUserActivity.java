package com.example.wifirssi.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wifirssi.constant.Service;
import com.example.wifirssi.model.AccessPoint;

import com.example.wifirssi.R;
import com.example.wifirssi.task.NdnTask;
import com.example.wifirssi.task.TcpTask;
import com.example.wifirssi.view.MapView;

import java.util.List;
import java.util.Set;

public class LocateUserActivity extends AppCompatActivity {

    public static MapView mapView;
    Button btLocateUser, btLoadMap, btNavigate, btDetectFloor;
    public static TextView tvLocation, tvMapDescription, tvTime;
    public static EditText etPlace, etFloor, etDest;
    RadioGroup rgArchitecture;
    RadioButton rbChoice;


    public static List<AccessPoint> selectedAccessPoints;
    WifiManager wifiManager;
    boolean isTcpSelected = true;
    boolean isDetectingFloor = false;

    SensorManager sensorManager;
    Sensor barometerSensor;

    String place;
    public static String currentLocation = "1_1_1";
    public static int floor;

    public static String tcpServerAddress = "192.168.0.120";
    public static int tcpServerPort = 5000;
    public static String ndnPrefix = "/ips/";
    public static String nfdAddress = "192.168.0.120";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_locate_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.btSettingsActivity:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locate_user);
        setTitle("Locate User");

//        selectedAccessPoints = MainActivity.selectedAccessPoints;
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        sensorManager = (SensorManager) getApplicationContext().getSystemService(Context.SENSOR_SERVICE);
        barometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        btLocateUser = findViewById(R.id.btLocateUser);
        btLoadMap = findViewById(R.id.btLoadMap);
        btDetectFloor = findViewById(R.id.btDetectFloor);
        btNavigate = findViewById(R.id.btNavigate);
        tvLocation = findViewById(R.id.tvLocation);
        tvMapDescription = findViewById(R.id.tvMapDescription);
        tvTime = findViewById(R.id.tvTime);
        mapView = findViewById(R.id.mapView);
        etPlace = findViewById(R.id.etPlace);
        etFloor = findViewById(R.id.etFloor);
        etDest = findViewById(R.id.etDest);
        rgArchitecture = findViewById(R.id.rgArchitecture);

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
                if (isTcpSelected) {
                    new TcpTask(place, floor, Service.LOAD_MAP).execute();
                } else {
                    new NdnTask(place, floor, Service.LOAD_MAP).execute();
                }
            }
        });

        btNavigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                place = etPlace.getText().toString();
                floor = Integer.parseInt(etFloor.getText().toString());
                String navigation = currentLocation + "_" + etDest.getText().toString();
                if (isTcpSelected) {
                    new TcpTask(place, floor, Service.NAVIGATE).execute(navigation);
                } else {
                    new NdnTask(place, floor, Service.NAVIGATE).execute(navigation);
                }
            }
        });

        btDetectFloor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                place = etPlace.getText().toString();
                detectPressure();
            }
        });
    }

    public void selectArchitecture(View view) {
        int selectedRadioId = rgArchitecture.getCheckedRadioButtonId();
        rbChoice = findViewById(selectedRadioId);
        isTcpSelected = R.id.rbTcp == selectedRadioId;
        Toast.makeText(this, rbChoice.getText().toString() + " is selected", Toast.LENGTH_SHORT).show();
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
            if (isTcpSelected) {
                new TcpTask(place, floor, Service.LOCATE).execute(observedRssValue);
            } else {
                new NdnTask(place, floor, Service.LOCATE).execute(observedRssValue);
            }
        }
    };

    SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            float[] values = sensorEvent.values;
            String airPressure = values[0] + "";
            if (isDetectingFloor) {
                if (isTcpSelected) {
                    new TcpTask(place, floor, Service.DETECT_FLOOR).execute(airPressure);
                } else {
                    new NdnTask(place, floor, Service.DETECT_FLOOR).execute(airPressure);
                }
                isDetectingFloor = false;
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    private void detectPressure() {
        isDetectingFloor = true;
        sensorManager.registerListener(sensorEventListener, barometerSensor, SensorManager.SENSOR_DELAY_UI);
    }

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
