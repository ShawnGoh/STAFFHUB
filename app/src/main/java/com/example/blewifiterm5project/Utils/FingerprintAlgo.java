package com.example.blewifiterm5project.Utils;

import android.util.Pair;

import java.util.ArrayList;

public class FingerprintAlgo {

    // Data in database
    // (ID, X, Y, MACk, AVGk, PAVGk, DEVk), k = 1, 2, …, n
    // (ID, X, Y) represents the location of one collecting point.
    // MACk stands for the physical address of the kth WiFi access point.
    // AVGk means the average value of the kth original WiFi signal strength.
    // PAVGk means the average value of the kth processed WiFi signal strength.
    // DEVk means the standard deviation of the kth original WiFi signal strength.

    public FingerprintAlgo() {
        //empty constructor
    }

    private class dataEntry {
        String ID;
        double X;
        double Y;
        String MACk;
        double AVGk;
        double PAVGk;
        double DEVk;

        dataEntry(String ID, double X, double Y, String MACk, double AVGk, double PAVGk, double DEVk) {
            this.ID = ID;
            this.X = X;
            this.Y = Y;
            this.MACk = MACk;
            this.AVGk = AVGk;
            this.PAVGk = PAVGk;
            this.DEVk = DEVk;
        }

    }

    private ArrayList<dataEntry> dataSet = new ArrayList<>();
    private ArrayList<dataEntry> wifiResults = new ArrayList<>();

    // Data received:
    // (MACk, AVGk, PAVGk, DEVk) where k = 1, 2, …, m

    private String MACk;
    private double AVGk;
    private double DEVk;

    // needs a method to pull from database
    // placeholder method

    public double getFLAG() {
        double total = 0;
        double FLAG = 0;
        for (int i = 0; i < wifiResults.size(); i++) {
            total += wifiResults.get(i).PAVGk;
            FLAG = total/wifiResults.size();
        }
        return FLAG;
    }

    public ArrayList<dataEntry> sortMAC() {

        double FLAG = getFLAG();
        ArrayList<dataEntry> nearbyData = new ArrayList<>();

        for (int i = 0; i < wifiResults.size(); i++) {
            if (wifiResults.get(i).PAVGk > FLAG) {
                nearbyData.add(wifiResults.get(i));
            }
        }
        return nearbyData;
    }

    public class positionCoordinates {

    }

    public double getEuclideanDistance() {
        ArrayList<dataEntry> nearbyData = sortMAC();
        double total = 0;
        for (int i = 0; i < dataSet.size(); i++) {
            for (int j = 0; j < nearbyData.size(); j++) {
                total += Math.pow(Math.abs(dataSet.get(i).PAVGk - nearbyData.get(j).PAVGk) + dataSet.get(i).DEVk + nearbyData.get(j).DEVk, 2);
            }
        }
        return Math.sqrt(total);
    }

    public Pair<Double, Double> estimateCoordinates() {
        double X1 = 0;
        double Y1 = 0;

        double wj = 1 / getEuclideanDistance();
        for (int j = 0; j < dataSet.size(); j++) {
            double Xj = dataSet.get(j).X;
            double Yj = dataSet.get(j).Y;
        }
        Pair<Double, Double> coordinates = new Pair<>(X1, Y1);
        return coordinates;
    }

}
