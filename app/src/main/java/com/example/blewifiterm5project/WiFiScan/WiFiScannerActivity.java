package com.example.blewifiterm5project.WiFiScan;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.blewifiterm5project.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class WiFiScannerActivity extends AppCompatActivity {

    private WifiManager wifiManager;
    private ListView listView;
    private Button buttonScan;
    private int size = 0;
    private List<ScanResult> results;
    private ArrayList<String> arrayList = new ArrayList<>();
    private ArrayAdapter adapter;
    private HashMap<String, Double> wifiDataAPs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifiscan);
        buttonScan = findViewById(R.id.scanButton);
        buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanWifi();
            }
        });

        listView = findViewById(R.id.wifiList);
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        // if WiFi is not already enabled, turn it on and inform admin/user
        if (!wifiManager.isWifiEnabled()) {
            Toast.makeText(this,"WiFi is disabled ... enabling it now", Toast.LENGTH_LONG).show();
            wifiManager.setWifiEnabled(true);
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(adapter);
        scanWifi();
    }

    // scan for available wifi points, clearing old results before the scan
    public void scanWifi() {
        arrayList.clear();
        wifiDataAPs.clear();
        registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifiManager.startScan();
        Toast.makeText(this,"Scanning WiFi ...", Toast.LENGTH_SHORT).show();
    }

    final BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            results = wifiManager.getScanResults();
            unregisterReceiver(this);

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

    // Attempt 2

//        WifiManager wifi;
//        ListView lv;
//        TextView textStatus;
//        Button buttonScan;
//        int size = 0;
//        List<ScanResult> results;
//
//        String ITEM_KEY = "key";
//        ArrayList<HashMap<String, String>> arraylist = new ArrayList<HashMap<String, String>>();
//        SimpleAdapter adapter;
//
//        /* Called when the activity is first created. */
//        @Override
//        public void onCreate(Bundle savedInstanceState)
//        {
//            super.onCreate(savedInstanceState);
//            setContentView(R.layout.wifidemo);
//
//            textStatus = (TextView) findViewById(R.id.textStatus);
//            buttonScan = (Button) findViewById(R.id.buttonScan);
//            buttonScan.setOnClickListener(this);
//            lv = (ListView)findViewById(R.id.list);
//
//            wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//            if (wifi.isWifiEnabled() == false)
//            {
//                Toast.makeText(getApplicationContext(), "wifi is disabled..making it enabled", Toast.LENGTH_LONG).show();
//                wifi.setWifiEnabled(true);
//            }
//            this.adapter = new SimpleAdapter(WiFiScannerActivity.this, arraylist, R.layout.row, new String[] { ITEM_KEY }, new int[] { R.id.list_value });
//            lv.setAdapter(this.adapter);
//
//            registerReceiver(new BroadcastReceiver()
//            {
//                @Override
//                public void onReceive(Context c, Intent intent)
//                {
//                    results = wifi.getScanResults();
//                    size = results.size();
//                }
//            }, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
//        }
//
//        public void onClick(View view)
//        {
//            arraylist.clear();
//            wifi.startScan();
//
//            Toast.makeText(this, "Scanning...." + size, Toast.LENGTH_SHORT).show();
//            try
//            {
//                size = size - 1;
//                while (size >= 0)
//                {
//                    HashMap<String, String> item = new HashMap<String, String>();
//                    item.put(ITEM_KEY, results.get(size).SSID + "  " + results.get(size).capabilities);
//
//                    arraylist.add(item);
//                    size--;
//                    adapter.notifyDataSetChanged();
//                }
//            }
//            catch (Exception e)
//            { }
//        }

    //Attempt 3
//    private static final String LOG_TAG = "AndroidExample";
//
//    private static final int MY_REQUEST_CODE = 123;
//
//    private WifiManager wifiManager;
//
//    private Button buttonState;
//    private Button buttonScan;
//
//    private EditText editTextPassword;
//    private LinearLayout linearLayoutScanResults;
//    private TextView textViewScanResults;
//
//    private WifiBroadcastReceiver wifiReceiver;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        this.wifiManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//
//        // Instantiate broadcast receiver
//        this.wifiReceiver = new WifiBroadcastReceiver();
//
//        // Register the receiver
//        registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
//
//        //
//        this.buttonState = (Button) this.findViewById(R.id.button_state);
//        this.buttonScan = (Button) this.findViewById(R.id.button_scan);
//
//        this.editTextPassword = (EditText) this.findViewById(R.id.editText_password);
//        this.textViewScanResults = (TextView) this.findViewById(R.id.textView_scanResults);
//        this.linearLayoutScanResults = (LinearLayout) this.findViewById(R.id.linearLayout_scanResults);
//
//        this.buttonState.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View v) {
//                showWifiState();
//            }
//        });
//
//        this.buttonScan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                askAndStartScanWifi();
//            }
//        });
//    }
//
//
//    private void askAndStartScanWifi()  {
//
//        // With Android Level >= 23, you have to ask the user
//        // for permission to Call.
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) { // 23
//            int permission1 = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
//
//            // Check for permissions
//            if (permission1 != PackageManager.PERMISSION_GRANTED) {
//
//                Log.d(LOG_TAG, "Requesting Permissions");
//
//                // Request permissions
//                ActivityCompat.requestPermissions(this,
//                        new String[] {
//                                Manifest.permission.ACCESS_COARSE_LOCATION,
//                                Manifest.permission.ACCESS_FINE_LOCATION,
//                                Manifest.permission.ACCESS_WIFI_STATE,
//                                Manifest.permission.ACCESS_NETWORK_STATE
//                        }, MY_REQUEST_CODE);
//                return;
//            }
//            Log.d(LOG_TAG, "Permissions Already Granted");
//        }
//        this.doStartScanWifi();
//    }
//
//    private void doStartScanWifi()  {
//        this.wifiManager.startScan();
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)  {
//        Log.d(LOG_TAG, "onRequestPermissionsResult");
//
//        switch (requestCode)  {
//            case MY_REQUEST_CODE:  {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)  {
//                    // permission was granted
//                    Log.d(LOG_TAG, "Permission Granted: " + permissions[0]);
//
//                    // Start Scan Wifi.
//                    this.doStartScanWifi();
//                }  else   {
//                    // Permission denied, boo! Disable the
//                    // functionality that depends on this permission.
//                    Log.d(LOG_TAG, "Permission Denied: " + permissions[0]);
//                }
//                break;
//            }
//            // Other 'case' lines to check for other
//            // permissions this app might request.
//        }
//    }
//
//    private void showWifiState()  {
//        int state = this.wifiManager.getWifiState();
//        String statusInfo = "Unknown";
//
//        switch (state)  {
//            case WifiManager.WIFI_STATE_DISABLING:
//                statusInfo = "Disabling";
//                break;
//            case WifiManager.WIFI_STATE_DISABLED:
//                statusInfo = "Disabled";
//                break;
//            case WifiManager.WIFI_STATE_ENABLING:
//                statusInfo = "Enabling";
//                break;
//            case WifiManager.WIFI_STATE_ENABLED:
//                statusInfo = "Enabled";
//                break;
//            case WifiManager.WIFI_STATE_UNKNOWN:
//                statusInfo = "Unknown";
//                break;
//            default:
//                statusInfo = "Unknown";
//                break;
//        }
//        Toast.makeText(this, "Wifi Status: " + statusInfo, Toast.LENGTH_LONG).show();
//    }
//
//    @Override
//    protected void onStop()  {
//        this.unregisterReceiver(this.wifiReceiver);
//        super.onStop();
//    }
//
//
//    // Define class to listen to broadcasts
//    class WifiBroadcastReceiver extends BroadcastReceiver  {
//        @Override
//        public void onReceive(Context context, Intent intent)   {
//            Log.d(LOG_TAG, "onReceive()");
//
//            Toast.makeText(WiFiScannerActivity.this, "Scan Complete!", Toast.LENGTH_SHORT).show();
//
//            boolean ok = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false);
//
//            if (ok)  {
//                Log.d(LOG_TAG, "Scan OK");
//
//                List<ScanResult> list = wifiManager.getScanResults();
//                for (ScanResult result: list) {
//                    Log.i("Wifi Results",  result.toString());
//                }
//
//                WiFiScannerActivity.this.showNetworks(list);
//                WiFiScannerActivity.this.showNetworksDetails(list);
//            }  else {
//                Log.d(LOG_TAG, "Scan not OK");
//            }
//
//        }
//    }
//
//    private void showNetworks(List<ScanResult> results) {
//        this.linearLayoutScanResults.removeAllViews();
//
//        for( final ScanResult result: results)  {
//            final String networkCapabilities = result.capabilities;
//            final String networkSSID = result.SSID; // Network Name.
//            //
//            Button button = new Button(this );
//
//            button.setText(networkSSID + " ("+networkCapabilities+")");
//            this.linearLayoutScanResults.addView(button);
//
//            button.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    String networkCapabilities = result.capabilities;
//                    connectToNetwork(networkCapabilities, networkSSID);
//                }
//            });
//        }
//    }
//
//    private void showNetworksDetails(List<ScanResult> results)  {
//
//        this.textViewScanResults.setText("");
//        StringBuilder sb = new StringBuilder();
//        sb.append("Result Count: " + results.size());
//
//        for(int i = 0; i < results.size(); i++ )  {
//            ScanResult result = results.get(i);
//            sb.append("\n\n  --------- Network " + i + "/" + results.size() + " ---------");
//
//            sb.append("\n result.capabilities: " + result.capabilities);
//            sb.append("\n result.SSID: " + result.SSID); // Network Name.
//
//            sb.append("\n result.BSSID: " + result.BSSID);
//            sb.append("\n result.frequency: " + result.frequency);
//            sb.append("\n result.level: " + result.level);
//
//            sb.append("\n result.describeContents(): " + result.describeContents());
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) { // Level 17, Android 4.2
//                sb.append("\n result.timestamp: " + result.timestamp);
//            }
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // Level 23, Android 6.0
//                sb.append("\n result.centerFreq0: " + result.centerFreq0);
//                sb.append("\n result.centerFreq1: " + result.centerFreq1);
//                sb.append("\n result.venueName: " + result.venueName);
//                sb.append("\n result.operatorFriendlyName: " + result.operatorFriendlyName);
//                sb.append("\n result.channelWidth: " + result.channelWidth);
//                sb.append("\n result.is80211mcResponder(): " + result.is80211mcResponder());
//                sb.append("\n result.isPasspointNetwork(): " + result.isPasspointNetwork() );
//            }
//        }
//        this.textViewScanResults.setText(sb.toString());
//    }
//
//    private void connectToNetwork(String networkCapabilities, String networkSSID)  {
//        Toast.makeText(this, "Connecting to network: "+ networkSSID, Toast.LENGTH_SHORT).show();
//
//        String networkPass = this.editTextPassword.getText().toString();
//        //
//        WifiConfiguration wifiConfig = new WifiConfiguration();
//        wifiConfig.SSID =  "\"" + networkSSID + "\"";
//
//        if(networkCapabilities.toUpperCase().contains("WEP")) { // WEP Network.
//            Toast.makeText(this, "WEP Network", Toast.LENGTH_SHORT).show();
//
//            wifiConfig.wepKeys[0] = "\"" + networkPass + "\"";
//            wifiConfig.wepTxKeyIndex = 0;
//            wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
//            wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
//        } else if(networkCapabilities.toUpperCase().contains("WPA")) { // WPA Network
//            Toast.makeText(this, "WPA Network", Toast.LENGTH_SHORT).show();
//            wifiConfig.preSharedKey = "\""+ networkPass +"\"";
//        } else  { // OPEN Network.
//            Toast.makeText(this, "OPEN Network", Toast.LENGTH_SHORT).show();
//            wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
//        }
//
//        this.wifiManager.addNetwork(wifiConfig);
//
//        @SuppressLint("MissingPermission") List<WifiConfiguration> list = this.wifiManager.getConfiguredNetworks();
//        for( WifiConfiguration config : list ) {
//            if(config.SSID != null && config.SSID.equals("\"" + networkSSID + "\"")) {
//                this.wifiManager.disconnect();
//                this. wifiManager.enableNetwork(config.networkId, true);
//                this.wifiManager.reconnect();
//                break;
//            }
//        }
//    }
//}