package com.example.wifirssi.Model;

public class AccessPoint {
    public String ssid;
    public String bssid;
    public int freq;
    public int rssi;

    public AccessPoint(String ssid, int rssi, String bssid, int freq) {
        this.ssid = ssid;
        this.rssi = rssi;
        this.bssid = bssid;
        this.freq = freq;
    }
}
