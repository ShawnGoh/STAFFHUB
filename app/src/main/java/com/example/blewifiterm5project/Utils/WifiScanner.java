package com.example.blewifiterm5project.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WifiScanner {

    private WifiManager wifiManager;
    private List<ScanResult> results;
    private ArrayList<String> arrayList = new ArrayList<>();
    private ArrayAdapter adapter;
    private HashMap<String, Double> wifiDataAPs = new HashMap<>();
    private Context mcontext;

    public WifiScanner(Context context){
        mcontext = context;
        wifiManager = (WifiManager) mcontext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        adapter = new ArrayAdapter<>(mcontext, android.R.layout.simple_list_item_1, arrayList);
    }

    public ListAdapter getWifiAdapter(){
        return adapter;
    }

    public void scanWifi() {

        if (!wifiManager.isWifiEnabled()) {
            Toast.makeText(mcontext,"WiFi is disabled ... enabling it now", Toast.LENGTH_LONG).show();
            wifiManager.setWifiEnabled(true);
        }

        arrayList.clear();
        wifiDataAPs.clear();
        mcontext.registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifiManager.startScan();
        Log.i("Testing", "94554278 scanning for WIFI!");
        Toast.makeText(mcontext,"Scanning WiFi ...", Toast.LENGTH_SHORT).show();
    }

    final BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            results = wifiManager.getScanResults();
            context.unregisterReceiver(this);
            Log.i("Testing", "94554278 wifiReceiver !!!");

            // for every result found via scanWifi, store the wifi SSID and approximate distance to it in an array
            for (ScanResult scanResult : results) {
                //int signalLevel = wifiManager.calculateSignalLevel(scanResult.level);
                double signalLevel = scanResult.level; //RSSI value of wifi
                double frequency = scanResult.frequency;
                double distanceInM = calculateDistance(signalLevel, frequency);
                //arrayList.add(scanResult.SSID + " - " + scanResult.capabilities);
                Log.i("Testing", scanResult.SSID);
                arrayList.add(scanResult.SSID + " (" + scanResult.BSSID + ") - " + distanceInM + "m");
                wifiDataAPs.put(scanResult.SSID + " (" + scanResult.BSSID + ")", distanceInM);
                adapter.notifyDataSetChanged();
            }
        }
    };

    // function for calculating rough approximation of current distance to nearby wifi access points
    public double calculateDistance(double signalLevel, double frequency) {
        double exp = (27.55 - (20 * Math.log10(frequency)) + Math.abs(signalLevel)) / 20.0;
        return (double) Math.round(Math.pow(10.0, exp) * 1000d / 1000d);
    }

}
