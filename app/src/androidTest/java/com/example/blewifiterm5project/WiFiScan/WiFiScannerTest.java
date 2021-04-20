package com.example.blewifiterm5project.WiFiScan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.blewifiterm5project.AdminWorld.AdminHome;
import com.example.blewifiterm5project.SignInSignup.SignIn;
import com.example.blewifiterm5project.Utils.WifiScanner;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class WiFiScannerTest {

    @Rule
    public ActivityScenarioRule<AdminHome> wifiTestRule = new ActivityScenarioRule<>(AdminHome.class);

    @Test
    public void testScanWifi() throws InterruptedException {
        final WifiScanner[] wifiScanner = new WifiScanner[1];
        ActivityScenario scenario = wifiTestRule.getScenario();
        scenario.onActivity(new ActivityScenario.ActivityAction() {
            @Override
            public void perform(Activity activity) {
                wifiScanner[0] = new WifiScanner(activity);
                wifiScanner[0].scanWifi();
            }
        });
        Thread.sleep(5000);
        assertTrue(!wifiScanner[0].getMacRssi().isEmpty());
        assertTrue(!wifiScanner[0].getWifiDataAPs().isEmpty());
    }

    @Test
    public void testScanWifiSwitchActivity() throws InterruptedException {
        final WifiScanner[] wifiScanner = new WifiScanner[1];
        ActivityScenario scenario = wifiTestRule.getScenario();
        scenario.onActivity(new ActivityScenario.ActivityAction() {
            @Override
            public void perform(Activity activity) {
                wifiScanner[0] = new WifiScanner(activity);
                wifiScanner[0].scanWifi();
                activity.startActivity(new Intent(activity,SignIn.class));
            }
        });
        Thread.sleep(5000);
        assertTrue(!wifiScanner[0].getMacRssi().isEmpty());
        assertTrue(!wifiScanner[0].getWifiDataAPs().isEmpty());
    }

    @Test
    public void testScanWifiDisabledWifi() throws InterruptedException {
        final WifiScanner[] wifiScanner = new WifiScanner[1];
        ActivityScenario scenario = wifiTestRule.getScenario();
        scenario.onActivity(new ActivityScenario.ActivityAction() {
            @Override
            public void perform(Activity activity) {
                WifiManager wifi = (WifiManager) activity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                wifi.setWifiEnabled(false);
                wifiScanner[0] = new WifiScanner(activity);
                wifiScanner[0].scanWifi();
            }
        });
        Thread.sleep(5000);
        assertTrue(!wifiScanner[0].getMacRssi().isEmpty());
        assertTrue(!wifiScanner[0].getWifiDataAPs().isEmpty());
    }

    @Test
    public void testScanWifiInSignInPage() throws InterruptedException {
        final WifiScanner[] wifiScanner = new WifiScanner[1];
        ActivityScenario<SignIn> scenario = ActivityScenario.launch(SignIn.class);
        scenario.onActivity(new ActivityScenario.ActivityAction() {
            @Override
            public void perform(Activity activity) {
                wifiScanner[0] = new WifiScanner(activity);
                wifiScanner[0].scanWifi();
            }
        });
        Thread.sleep(5000);
        assertTrue(!wifiScanner[0].getMacRssi().isEmpty());
        assertTrue(!wifiScanner[0].getWifiDataAPs().isEmpty());
    }

}