package com.example.blewifiterm5project.Utils;

import com.example.blewifiterm5project.Models.dbdatapoint;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static junit.framework.TestCase.assertEquals;

public class FingerprintAlgoTest {

    FingerprintAlgo fingerprintAlgo;
    ArrayList<dbdatapoint> dataSet = new ArrayList<>();
    dbdatapoint wifiResults;

    // Database dataSet 1
    ArrayList<Double> rssiDistPair11 = new ArrayList<>();
    ArrayList<Double> rssiDistPair12 = new ArrayList<>();
    ArrayList<Double> rssiDistPair13 = new ArrayList<>();
    HashMap<String, ArrayList<Double>> accesspoint1 = new HashMap<>();
    ArrayList<Float> coordinates1 = new ArrayList<>();

    // Database dataSet 2
    ArrayList<Double> rssiDistPair21 = new ArrayList<>();
    ArrayList<Double> rssiDistPair22 = new ArrayList<>();
    ArrayList<Double> rssiDistPair23 = new ArrayList<>();
    HashMap<String, ArrayList<Double>> accesspoint2 = new HashMap<>();
    ArrayList<Float> coordinates2 = new ArrayList<>();

    // wifiResults
    ArrayList<Double> rssiDistPair1 = new ArrayList<>();
    ArrayList<Double> rssiDistPair2 = new ArrayList<>();
    ArrayList<Double> rssiDistPair3 = new ArrayList<>();
    HashMap<String, ArrayList<Double>> accesspoint = new HashMap<>();
    ArrayList<Float> coordinates = new ArrayList<>();

    @Before
    public void setup() {
        // db dataset 1
        rssiDistPair11.add(-61.0);
        rssiDistPair11.add(5.0);
        rssiDistPair12.add(-65.0);
        rssiDistPair12.add(8.0);
        rssiDistPair13.add(-72.0);
        rssiDistPair13.add(16.0);
        accesspoint1.put("SUTD_Wifi", rssiDistPair11);
        accesspoint1.put("SUTD_Guest", rssiDistPair12);
        accesspoint1.put("SUTD_Test", rssiDistPair13);
        coordinates1.add(2f);
        coordinates1.add(3f);
        dbdatapoint dbdatapoint1 = new dbdatapoint(accesspoint1,coordinates1);

        // db dataset 2
        rssiDistPair21.add(-57.0);
        rssiDistPair21.add(3.0);
        rssiDistPair22.add(-62.0);
        rssiDistPair22.add(5.0);
        rssiDistPair23.add(-71.0);
        rssiDistPair23.add(16.0);
        accesspoint2.put("SUTD_Wifi", rssiDistPair21);
        accesspoint2.put("SUTD_Guest", rssiDistPair22);
        accesspoint2.put("SUTD_Test", rssiDistPair23);
        coordinates2.add(6f);
        coordinates2.add(9f);
        dbdatapoint dbdatapoint2 = new dbdatapoint(accesspoint2,coordinates2);

        // db dataset compiled
        dataSet.add(dbdatapoint1);
        dataSet.add(dbdatapoint2);

        // wifiresults
        rssiDistPair1.add(-49.0);
        rssiDistPair1.add(3.0);
        rssiDistPair2.add(-61.0);
        rssiDistPair2.add(4.0);
        rssiDistPair3.add(-79.0);
        rssiDistPair3.add(37.0);
        accesspoint.put("SUTD_Wifi", rssiDistPair1);
        accesspoint.put("SUTD_Guest", rssiDistPair2);
        accesspoint.put("SUTD_Test", rssiDistPair3);
        coordinates.add(5f);
        coordinates.add(7f);
        wifiResults = new dbdatapoint(accesspoint,coordinates);
    }

    @Test
    public void getFLAG() {
        fingerprintAlgo = new FingerprintAlgo(dataSet, wifiResults);
        assertEquals(-55.0, fingerprintAlgo.getFLAG());
    }

    @Test
    public void sortMAC() {
        fingerprintAlgo = new FingerprintAlgo(dataSet, wifiResults);
        ArrayList<String> nearbyAPs = new ArrayList<>();
        nearbyAPs.add("SUTD_Wifi");
        assertEquals(nearbyAPs, fingerprintAlgo.sortMAC());
    }

//    @Test
//    public void getEuclideanDistance() {
//        FingerprintAlgo fingerprintAlgo = new FingerprintAlgo(dataSet, wifiResults);
//        assertEquals(23, fingerprintAlgo.getEuclideanDistance());
//    }

//    @Test
//    public void estimateCoordinates() {
//        fingerprintAlgo = new FingerprintAlgo(dataSet, wifiResults);
//        Pair<Double, Double> coord = new Pair<>(5.0, 7.0);
//        assertEquals(coord, fingerprintAlgo.estimateCoordinates());
//    }
}