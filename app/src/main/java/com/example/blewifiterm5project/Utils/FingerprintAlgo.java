package com.example.blewifiterm5project.Utils;

import android.util.Pair;

import com.example.blewifiterm5project.Models.dbdatapoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class FingerprintAlgo {

    // Data in database
    // (ID, X, Y, MACk, AVGk, PAVGk, DEVk), k = 1, 2, …, n
    // (ID, X, Y) represents the location of one collecting point.
    // MACk stands for the physical address of the kth WiFi access point.
    // AVGk means the average value of the kth original WiFi signal strength.
    // PAVGk means the average value of the kth processed WiFi signal strength.
    //

    private ArrayList<dbdatapoint> dataSet = new ArrayList<>();
    private dbdatapoint wifiResults;

    public FingerprintAlgo(ArrayList dataSet, dbdatapoint wifiResults) {
        // from Firebase
        this.dataSet = dataSet;
        // from wifiResults
        this.wifiResults = wifiResults;
    }

//    private class dataPoint {
//        String ID;
//        double X;
//        double Y;
//        ArrayList<dataEntry> APdata;
//
//        dataPoint(String ID, double X, double Y, ArrayList<dataEntry> APdata){
//            this.ID = ID;
//            this.X = X;
//            this.Y = Y;
//            this.APdata = APdata;
//        }
//    }
//
//    private class dataEntry {
//        String ID;
////        double X;
////        double Y;
//        String MACk;
//        double AVGk;
//        double PAVGk;
//        double DEVk;
//
//        dataEntry(String ID, double X, double Y, String MACk, double AVGk, double PAVGk, double DEVk) {
//            this.ID = ID;
////            this.X = X;
////            this.Y = Y;
//            this.MACk = MACk;
//            this.AVGk = AVGk;
//            this.PAVGk = PAVGk;
//            this.DEVk = DEVk;
//        }
//
//    }

    // Data received:
    // (MACk, AVGk, PAVGk, DEVk) where k = 1, 2, …, m

    private String MACk;
    private double AVGk;
    private double DEVk;

    // needs a method to pull from database
    // placeholder method

    public double getFLAG() {
        // average RSSI value received at user's location
        double total = 0;
        double FLAG = 0;
        double RSSI_value = 0;
        for (HashMap.Entry<String, ArrayList<Double>> accessPoint : wifiResults.getAccesspoints().entrySet()) {
            RSSI_value = accessPoint.getValue().get(0);
            total += RSSI_value;
            FLAG = total/wifiResults.getAccesspoints().size();
        }
        return FLAG;
    }

    public ArrayList<String> sortMAC() {

        double FLAG = getFLAG();
        ArrayList<String> nearbyAPs = new ArrayList<>();

        // only include MAC address of APs near the user
        for (HashMap.Entry<String, ArrayList<Double>> accessPoint : wifiResults.getAccesspoints().entrySet()) {
            if (accessPoint.getValue().get(0) > FLAG) {
                nearbyAPs.add(accessPoint.getKey());
            }
        }
        return nearbyAPs;
    }

    public class positionCoordinates {
        //to interface with ZC's point setting
    }

    public double getEuclideanDistance(dbdatapoint dataPoint, dbdatapoint wifiResults) {
        // to be run on individual datapoints in the databased
        ArrayList<String> nearbyAPs = sortMAC(); //TODO: put into main function

//        ArrayList effectiveDataSet = new ArrayList();
//        List list = new ArrayList(Arrays.asList(effectiveDataSet)); //returns a list view of an array
//        //trim list of datapoints in dataset to only include those w MAC Address nearby
//        for (int i = 0; i < nearbyData.size(); i++){
//            list.addAll(Arrays.asList(dataSet).contains(nearbyData.get(i).ID));
//        }

        double total = 0;
        for (HashMap.Entry<String, ArrayList<Double>> dbaccessPoint : dataPoint.getAccesspoints().entrySet()) {
            for (int j = 0; j < nearbyAPs.size(); j++) {
                if (nearbyAPs.get(j).equals(dbaccessPoint.getKey())){
                    total += Math.pow(Math.abs(wifiResults.getAccesspoints().get(nearbyAPs.get(j)).get(0) - dbaccessPoint.getValue().get(0)), 2);
//                    total += Math.pow(Math.abs(wifiResults.getAccesspoints().get(nearbyAPs.get(j)).get(0) - dbaccessPoint.getValue().get(0)) + dataPoint.get(i).DEVk + nearbyData.get(j).DEVk, 2);
                    break;
                }
            }
        }
        return Math.sqrt(total);
    }

    public Pair<Double, Double> estimateCoordinates() {
        double X1 = 0;
        double Y1 = 0;

        double sum_wx = 0;
        double sum_wy = 0;
        double sum_w = 0;
        for (int i = 0; i < dataSet.size(); i++) {
            double di = getEuclideanDistance(dataSet.get(i), wifiResults);
            double w = 1/di;
            sum_wx += w*dataSet.get(i).getCoordinates().get(0);
            sum_wy += w*dataSet.get(i).getCoordinates().get(1);
        }

//        double wj = 1 / getEuclideanDistance();
//        for (int j = 0; j < dataSet.size(); j++) {
//            double Xj = dataSet.get(j).X;
//            double Yj = dataSet.get(j).Y;
//        }

        X1 = sum_wx / sum_w;
        Y1 = sum_wy / sum_w;

        Pair<Double, Double> coordinates = new Pair<>(X1, Y1);
        return coordinates;
    }

}
