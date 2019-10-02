package com.example.wifirssi.model;

public class AccessPoint {
    private String ssid;
    private String mac;
    private int rssi;

    public AccessPoint(String ssid, String mac, int rssi) {
        this.ssid = ssid;
        this.mac = mac;
        this.rssi = rssi;
    }

    public String getSsid() {
        return ssid;
    }

    public String getMac() {
        return mac;
    }

    public int getRssi() {
        return rssi;
    }
}