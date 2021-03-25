package com.example.blewifiterm5project.WiFiScan;

import org.junit.Test;

public class WiFiScannerActivityTest {

    /**
     * Called before each test
     */
//    @Before
//    public void setUp() throws Exception {
//        mWifiInjector = mockWifiInjector();
//        mResource = mockResource();
//        mAlarmManager = new MockAlarmManager();
//        mContext = mockContext();
//        mWifiStateMachine = mockWifiStateMachine();
//        mWifiConfigManager = mockWifiConfigManager();
//        mWifiInfo = getWifiInfo();
//        mWifiScanner = mockWifiScanner();
//        mWifiQNS = mockWifiQualifiedNetworkSelector();
//        mWifiConnectivityManager = new WifiConnectivityManager(mContext, mWifiStateMachine,
//                mWifiScanner, mWifiConfigManager, mWifiInfo, mWifiQNS, mWifiInjector,
//                mLooper.getLooper());
//        mWifiConnectivityManager.setWifiEnabled(true);
//        when(mClock.elapsedRealtime()).thenReturn(SystemClock.elapsedRealtime());
//    }
    /**
     * Called after each test
     */
//    @After
//    public void cleanup() {
//        validateMockitoUsage();
//    }

    @Test
    public void onCreate() {
    }

    @Test
    public void scanWifi() {
    }

    @Test
    public void calculateDistance() {
    }

    @Test
    public void sortWiFiData() {
    }

    /**
     * Multiple back to back connection attempts after a user selection should not be rate limited.
     *
     * Expected behavior: WifiConnectivityManager calls WifiStateMachine.autoConnectToNetwork()
     * with the expected candidate network ID and BSSID for only the expected number of times within
     * the given interval.
     */
//    @Test
//    public void connectionAttemptNotRateLimitedWhenScreenOffAfterUserSelection() {
//        int maxAttemptRate = WifiConnectivityManager.MAX_CONNECTION_ATTEMPTS_RATE;
//        int timeInterval = WifiConnectivityManager.MAX_CONNECTION_ATTEMPTS_TIME_INTERVAL_MS;
//        int numAttempts = 0;
//        int connectionAttemptIntervals = timeInterval / maxAttemptRate;
//        mWifiConnectivityManager.handleScreenStateChanged(false);
//        // First attempt the max rate number of connections within the rate interval.
//        long currentTimeStamp = 0;
//        for (int attempt = 0; attempt < maxAttemptRate; attempt++) {
//            currentTimeStamp += connectionAttemptIntervals;
//            when(mClock.elapsedRealtime()).thenReturn(currentTimeStamp);
//            // Set WiFi to disconnected state to trigger PNO scan
//            mWifiConnectivityManager.handleConnectionStateChanged(
//                    WifiConnectivityManager.WIFI_STATE_DISCONNECTED);
//            numAttempts++;
//        }
//        mWifiConnectivityManager.connectToUserSelectNetwork(CANDIDATE_NETWORK_ID, false);
//        for (int attempt = 0; attempt < maxAttemptRate; attempt++) {
//            currentTimeStamp += connectionAttemptIntervals;
//            when(mClock.elapsedRealtime()).thenReturn(currentTimeStamp);
//            // Set WiFi to disconnected state to trigger PNO scan
//            mWifiConnectivityManager.handleConnectionStateChanged(
//                    WifiConnectivityManager.WIFI_STATE_DISCONNECTED);
//            numAttempts++;
//        }
//        // Verify that all the connection attempts went through
//        verify(mWifiStateMachine, times(numAttempts)).autoConnectToNetwork(
//                CANDIDATE_NETWORK_ID, CANDIDATE_BSSID);
//    }
}