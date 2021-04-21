package com.example.blewifiterm5project.Utils;

import android.util.Pair;

import com.example.blewifiterm5project.Models.dbdatapoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class FingerprintAlgo {

    // dataset refers to document
    private ArrayList<dbdatapoint> dataSet = new ArrayList<>();
    private dbdatapoint wifiResults;
    private double rssiThreshold = 5;
    private double flagCap = -70;
    private int k = 5;
    private int c = 3;
    private ArrayList<String> nearbyAPs;

    public FingerprintAlgo(ArrayList dataSet, dbdatapoint wifiResults) {
        // from Firebase
        this.dataSet = dataSet;
        // from wifiResults
        this.wifiResults = wifiResults;
    }

    public double getFLAG() {
        // average RSSI value received at user's location
        double total = 0;
        double FLAG = 0;
        double RSSI_value = 0;
        int flagCounter = 0;
        for (HashMap.Entry<String, ArrayList<Double>> accessPoint : wifiResults.getAccesspoints().entrySet()) {
            RSSI_value = accessPoint.getValue().get(0);
            if (RSSI_value > flagCap) {
                total += RSSI_value;
                flagCounter++;
            }
            //FLAG = total/wifiResults.getAccesspoints().size();
            FLAG = total/flagCounter;
        }
        System.out.println("FLAG value: " + FLAG);
        return FLAG;
    }

    public ArrayList<String> sortMAC() {

        double FLAG = getFLAG();

        nearbyAPs = new ArrayList<>();

        // only include MAC address of APs near the user
        for (HashMap.Entry<String, ArrayList<Double>> accessPoint : wifiResults.getAccesspoints().entrySet()) {
            if (accessPoint.getValue().get(0) > FLAG) {
                nearbyAPs.add(accessPoint.getKey());
            }
        }

        System.out.println("Nearby APs are: " + nearbyAPs);
        return nearbyAPs;
    }

    public double percentageScore(dbdatapoint dataPoint, dbdatapoint wifiResults) {

        System.out.println("Datapoint being evaluated: "+ dataPoint.getCoordinates());
        ArrayList<String> nearbyAPs = sortMAC();

        if (nearbyAPs.size() == 0) {
            return 0.0;
        }

        int counter  = 0;
        HashMap<String, ArrayList<Double>> floorMacAdd = new HashMap<>();

        for (HashMap.Entry<String, ArrayList<Double>> dbaccessPoint : dataPoint.getAccesspoints().entrySet()) {
            floorMacAdd.put(dbaccessPoint.getKey(), dbaccessPoint.getValue());
        }

        ArrayList<Double> nearbyAPsRSSI = new ArrayList<>();

        for (int j = 0; j < nearbyAPs.size(); j++) {
            nearbyAPsRSSI.add(wifiResults.getAccesspoints().get(nearbyAPs.get(j)).get(0));
        }

        for (int i = 0; i < nearbyAPs.size(); i++) {
            if (floorMacAdd.containsKey(nearbyAPs.get(i))) {
                System.out.println("Difference in RSSI values: " + (Math.abs(floorMacAdd.get(nearbyAPs.get(i)).get(0) - nearbyAPsRSSI.get(i))));
                if (Math.abs(floorMacAdd.get(nearbyAPs.get(i)).get(0) - nearbyAPsRSSI.get(i)) <= rssiThreshold) {
                    counter++;
//                    System.out.println("Difference in RSSI values: " + (Math.abs(floorMacAdd.get(nearbyAPs.get(i)).get(0) - nearbyAPsRSSI.get(i)));
                    System.out.println("Adding counter to value: " + counter);
                }
            }
        }
        System.out.println("Final counter value: " + counter);

        System.out.println("Ravv Score: "+((double) counter/nearbyAPs.size()));
        return (double) counter/nearbyAPs.size();
    }

    public ArrayList<dbdatapoint> topKPercentage() {

        double eachScore = 0;

        HashMap<dbdatapoint, Double> dataScore = new HashMap<>();

        System.out.println("DataSet: " + dataSet);
        System.out.println("DataSet size: " + dataSet.size());

        for (int i = 0; i < dataSet.size(); i++) {
            eachScore = percentageScore(dataSet.get(i), wifiResults);
            System.out.println("eachScore: " + eachScore);
            dataScore.put(dataSet.get(i), eachScore);
        }

        System.out.println("datascore: "+dataScore);

        LinkedHashMap<dbdatapoint, Double> sortedDataScore = sortByValues(dataScore, "descending");
        System.out.println("sortedDataScore: " + sortedDataScore);

//        TreeMap<Double, dbdatapoint> treeSortedScore = new TreeMap<>(sortedDataScore);
//        sortedDataScore.clear();
//        sortedDataScore.putAll(treeSortedScore.descendingMap());

        ArrayList<dbdatapoint> topKScores = new ArrayList<>();
        System.out.println("sortedDataScore: " + sortedDataScore);

        for (dbdatapoint i : sortedDataScore.keySet()) {
            if (topKScores.size() < k) {
                topKScores.add(i);
            }
        }

        System.out.println("Database BSSIDs used: " + topKScores);
        return topKScores;
    }

    public double getEuclideanDistance(dbdatapoint dataPoint, dbdatapoint wifiResults) {
        // to be run on individual datapoints in the database
//        ArrayList<String> nearbyAPs = sortMAC();

        // for distance
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


        System.out.println("Finishing getting Euclidean Distance!");
        return Math.sqrt(total);
    }

    public Pair<Double, Double> estimateCoordinates() {
        double X1 = 0;
        double Y1 = 0;

        double sum_wx = 0;
        double sum_wy = 0;
        double sum_w = 0;
//        System.out.println(dataSet.size());

        ArrayList<dbdatapoint> clearedPercentagePoints = topKPercentage();
        HashMap<dbdatapoint, Double> euclidFilteredData = new HashMap<>();

        for (int i = 0; i < clearedPercentagePoints.size(); i++) {
            double di = getEuclideanDistance(clearedPercentagePoints.get(i), wifiResults);
            euclidFilteredData.put(clearedPercentagePoints.get(i), di);
        }
        System.out.println("Euclidean Distance Pre-Filtered Hashmap: " + euclidFilteredData);

        LinkedHashMap<dbdatapoint, Double> sortedEuclidFilteredData = sortByValues(euclidFilteredData, "ascending");
        System.out.println("Sorted Euclidean Filtered Hashmap: " + sortedEuclidFilteredData);
        ArrayList<dbdatapoint> topCFilteredDist = new ArrayList<>();

        for (dbdatapoint i : sortedEuclidFilteredData.keySet()) {
            if (topCFilteredDist.size() < c) {
                topCFilteredDist.add(i);
            }
        }
        System.out.println("Euclidean Distance Post-Filtered List: " + topCFilteredDist);

        for (int i = 0; i < topCFilteredDist.size(); i++) {
            //System.out.println(dataSet.get(i));
            double di = getEuclideanDistance(topCFilteredDist.get(i), wifiResults);
            System.out.println("Coordinates of point being used: "+ topCFilteredDist.get(i).getCoordinates());
            //System.out.println(wifiResults.getCoordinates());
            System.out.println("EuclideanDistance is: " + di);
            double w = 0;
            if (di > 0) {
                w = 1 / di;
            }
            sum_w += w;
            sum_wx += w*topCFilteredDist.get(i).getCoordinates().get(0);
            sum_wy += w*topCFilteredDist.get(i).getCoordinates().get(1);
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

    public static LinkedHashMap sortByValues(HashMap map, String type) {
        List list = new LinkedList(map.entrySet());

        if (type == "descending") {
            Collections.sort(list, new Comparator() {
                public int compare(Object o1, Object o2) {
                    return ((Comparable) ((Map.Entry) (o2)).getValue())
                            .compareTo(((Map.Entry) (o1)).getValue());
                }
            });
        }
        else if (type == "ascending") {
            Collections.sort(list, new Comparator() {
                public int compare(Object o1, Object o2) {
                    return ((Comparable) ((Map.Entry) (o1)).getValue())
                            .compareTo(((Map.Entry) (o2)).getValue());
                }
            });
        }

        LinkedHashMap sortedHashMap = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }
        return sortedHashMap;
    }

}
