package com.example.blewifiterm5project.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WifiScanner {

    private WifiManager wifiManager;
    private List<ScanResult> results;
    private ArrayList<String> arrayList = new ArrayList<>();
    private ArrayAdapter adapter;
    private HashMap<String, Double> wifiDataAPs = new HashMap<>();
    private HashMap<String, ArrayList<Double>> macRssi = new HashMap<>();
    private Context mcontext;

    public WifiScanner(Context context){
        mcontext = context;
        wifiManager = (WifiManager) mcontext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }

    public ListAdapter getWifiAdapter(){
        return adapter;
    }

    // executes a single wifi scan
    public void scanWifi() {

        // turn on wifi on phone if disabled, no need to connect to any wifi to work
        if (!wifiManager.isWifiEnabled()) {
            Toast.makeText(mcontext,"WiFi is disabled ... enabling it now", Toast.LENGTH_LONG).show();
            wifiManager.setWifiEnabled(true);
        }

        arrayList.clear();
        wifiDataAPs.clear();
        macRssi.clear();
        mcontext.registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifiManager.startScan();
        Log.i("Testing", "scanning for WIFI!");
        Toast.makeText(mcontext,"Scanning WiFi ...", Toast.LENGTH_SHORT).show();
    }

    BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            results = wifiManager.getScanResults();
            context.unregisterReceiver(this);
            Log.i("Testing", "wifiReceiver !!!");

            // for every result found via scanWifi, store the wifi SSID and approximate distance to it in an array
            for (ScanResult scanResult : results) {
                double signalLevel = scanResult.level; //RSSI value of wifi
                double frequency = scanResult.frequency;
                double distanceInM = calculateDistance(signalLevel, frequency);
                ArrayList<Double> rssiValue = new ArrayList<>();
                rssiValue.add(signalLevel);
                rssiValue.add(distanceInM);
                Log.i("Testing", scanResult.SSID);
                arrayList.add(scanResult.SSID + " (" + scanResult.BSSID + ") - " + distanceInM + "m");
                wifiDataAPs.put(scanResult.SSID + " (" + scanResult.BSSID + ")", distanceInM);
                macRssi.put(scanResult.SSID + " (" + scanResult.BSSID + ")", rssiValue);
            }

            Toast.makeText(mcontext, "Wifi list populated", Toast.LENGTH_SHORT).show();
        }
    };

    // function for calculating rough approximation of current distance to nearby wifi access points
    public double calculateDistance(double signalLevel, double frequency) {
        double exp = (27.55 - (20 * Math.log10(frequency)) + Math.abs(signalLevel)) / 20.0;
        return (double) Math.round(Math.pow(10.0, exp) * 1000d / 1000d);
    }

    public HashMap<String, Double> getWifiDataAPs() {
        return wifiDataAPs;
    }

    public HashMap<String, ArrayList<Double>> getMacRssi() {
        return macRssi;
    }

}
