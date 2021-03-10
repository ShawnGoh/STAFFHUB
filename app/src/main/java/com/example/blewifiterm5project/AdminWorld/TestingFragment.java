package com.example.blewifiterm5project.AdminWorld;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.blewifiterm5project.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class TestingFragment extends Fragment {

    private WifiManager wifiManager;
    private ListView listView;
    private Button buttonScan;
    private int size = 0;
    private List<ScanResult> results;
    private ArrayList<String> arrayList = new ArrayList<>();
    private ArrayAdapter adapter;
    private HashMap<String, Double> wifiDataAPs = new HashMap<>();
    private Context mcontext;

    @SuppressLint("WifiManagerPotentialLeak")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_testing, container, false);
        listView = view.findViewById(R.id.wifiList);
        buttonScan = view.findViewById(R.id.scanButton);
        wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        mcontext = getActivity();

        buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanWifi();
            }
        });

        // if WiFi is not already enabled, turn it on and inform admin/user
        if (!wifiManager.isWifiEnabled()) {
            Toast.makeText(mcontext,"WiFi is disabled ... enabling it now", Toast.LENGTH_LONG).show();
            wifiManager.setWifiEnabled(true);
        }

        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(adapter);
        scanWifi();
        return view;
    }


        // scan for available wifi points, clearing old results before the scan
    public void scanWifi() {
        arrayList.clear();
        wifiDataAPs.clear();
        mcontext.registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifiManager.startScan();
        Toast.makeText(mcontext,"Scanning WiFi ...", Toast.LENGTH_SHORT).show();
    }

    final BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            results = wifiManager.getScanResults();
            mcontext.unregisterReceiver(this);

            // for every result found via scanWifi, store the wifi SSID and approximate distance to it in an array
            for (ScanResult scanResult : results) {
                //int signalLevel = wifiManager.calculateSignalLevel(scanResult.level);
                double signalLevel = scanResult.level;
                double frequency = scanResult.frequency;
                double distanceInM = calculateDistance(signalLevel, frequency);
                //arrayList.add(scanResult.SSID + " - " + scanResult.capabilities);
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

        // function to return WiFi APs based on descending distance to admin/user
    public HashMap<String, Double> sortWiFiData() {

        HashMap<String, Double> sortedWiFiData = new LinkedHashMap<>();

        // create a list "wifiDataList" from elements of HashMap "wifiDataAPs"
        List<Map.Entry<String, Double>> wifiDataList = new LinkedList<>(wifiDataAPs.entrySet());

        // sort the list "wifiDataList"
        Collections.sort(wifiDataList, new Comparator<Map.Entry<String, Double>>() {
            @Override
            public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                return (o1.getValue().compareTo(o2.getValue()));
            }
        });

        // put data from sorted list to new HashMap "sortedWiFiData"
        for (Map.Entry<String, Double> dataPoint : wifiDataList) {
            sortedWiFiData.put(dataPoint.getKey(), dataPoint.getValue());
        }
        return sortedWiFiData;
    }
}