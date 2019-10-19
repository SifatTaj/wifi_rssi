package com.example.wifirssi.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.wifirssi.R;

public class SettingsActivity extends AppCompatActivity {

    EditText etTcpServerAddress, etTcpServerPort, etNdnPrefix, etNfdAddress;
    Button btApplySettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle("Settings");

        etTcpServerAddress = findViewById(R.id.etTcpServerAddress);
        etTcpServerPort = findViewById(R.id.etTcpServerPort);
        etNdnPrefix = findViewById(R.id.etNdnPrefix);
        etNfdAddress = findViewById(R.id.etNfdAddress);
        btApplySettings = findViewById(R.id.btApplySettings);

        etTcpServerAddress.setText(LocateUserActivity.tcpServerAddress);
        etTcpServerPort.setText(LocateUserActivity.tcpServerPort + "");
        etNdnPrefix.setText(LocateUserActivity.ndnPrefix);
        etNfdAddress.setText(LocateUserActivity.nfdAddress);

        btApplySettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocateUserActivity.tcpServerAddress = etTcpServerAddress.getText().toString();
                LocateUserActivity.ndnPrefix = etNdnPrefix.getText().toString();
                LocateUserActivity.nfdAddress = etNfdAddress.getText().toString();
            }
        });
    }
}
