package com.example.wifirssi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.wifirssi.model.AccessPoint;

import java.util.Set;

public class LocateUserActivity extends AppCompatActivity {

    Set<AccessPoint> selectedAccessPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locate_user);
        setTitle("Locate User");
        selectedAccessPoints = MainActivity.selectedAccessPoints;
    }
}
