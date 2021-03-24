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

import android.util.Log;
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

import com.example.blewifiterm5project.Utils.WifiScanner;


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
    private WifiScanner wifiScanner;

    @SuppressLint("WifiManagerPotentialLeak")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_testing, container, false);
        listView = view.findViewById(R.id.wifiList);
        buttonScan = view.findViewById(R.id.scanButton);
        mcontext = getActivity();
        wifiScanner = new WifiScanner(mcontext);

        buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Testing", "94554278 button PRESSED");
                wifiScanner.scanWifi();
            }
        });

        listView.setAdapter(wifiScanner.getWifiAdapter());
//        scanWifi();

        return view;
    }

//        // scan for available wifi points, clearing old results before the scan
//    public void scanWifi() {
//        arrayList.clear();
//        wifiDataAPs.clear();
//        mcontext.registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
//        wifiManager.startScan();
//        Log.i("Testing", "94554278 scanning for WIFI!");
//        Toast.makeText(mcontext,"Scanning WiFi ...", Toast.LENGTH_SHORT).show();
//    }
//
//    final BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            results = wifiManager.getScanResults();
//            context.unregisterReceiver(this);
//            Log.i("Testing", "94554278 wifiReceiver !!!");
//
//            // for every result found via scanWifi, store the wifi SSID and approximate distance to it in an array
//            for (ScanResult scanResult : results) {
//                //int signalLevel = wifiManager.calculateSignalLevel(scanResult.level);
//                double signalLevel = scanResult.level; //RSSI value of wifi
//                double frequency = scanResult.frequency;
//                double distanceInM = calculateDistance(signalLevel, frequency);
//                //arrayList.add(scanResult.SSID + " - " + scanResult.capabilities);
//                Log.i("Testing", scanResult.SSID);
//                arrayList.add(scanResult.SSID + " (" + scanResult.BSSID + ") - " + distanceInM + "m");
//                wifiDataAPs.put(scanResult.SSID + " (" + scanResult.BSSID + ")", distanceInM);
//                adapter.notifyDataSetChanged();
//            }
//        }
//    };
//
//    // function for calculating rough approximation of current distance to nearby wifi access points
//    public double calculateDistance(double signalLevel, double frequency) {
//        double exp = (27.55 - (20 * Math.log10(frequency)) + Math.abs(signalLevel)) / 20.0;
//        return (double) Math.round(Math.pow(10.0, exp) * 1000d / 1000d);
//    }

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

    // attach x-y coordinate values this singular mapping

    // repeat mapping process 20 times?

    // when in testing -
    // calculate difference in RSSI with previous RSSI stored for each AP in database

    // estimate position by finding centroid?

    // OR second filtering
    // find average variance in diff in RSSI vals of RP to current reading
    // if varies too much, remove that RP (outlier)

    // find estimated position using formula

}