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

    // dataset is the array of documents within a collection in the firestore database
    private ArrayList<dbdatapoint> dataSet;

    // wifiResults refer to the results of a single wifi scan
    private dbdatapoint wifiResults;

    // changeable variables to relax or tighten conditions of the algorithm.
    private double rssiThreshold = 5;
    private double flagCap = -70;
    private int k = 5;
    private int c = 3;

    // nearbyAPs is an array of nearby wifi mac addresses (BSSID) that have passed the FLAG check
    private ArrayList<String> nearbyAPs;

    // constructor for class
    public FingerprintAlgo(ArrayList dataSet, dbdatapoint wifiResults) {
        this.dataSet = dataSet;
        this.wifiResults = wifiResults;
    }

    // getFLAG returns a FLAG value based on a wifi scan at the current location, which is used to determine which wifi APs detected are appropriate for consideration in positioning
    public double getFLAG() {
        double total = 0;
        double FLAG = 0;
        double RSSI_value = 0;
        int flagCounter = 0;
        for (HashMap.Entry<String, ArrayList<Double>> accessPoint : wifiResults.getAccesspoints().entrySet()) {
            RSSI_value = accessPoint.getValue().get(0);

            // additional restraint added for better accuracy: only consider wifi APs near enough, determined by flagCap
            if (RSSI_value > flagCap) {
                total += RSSI_value;
                flagCounter++;
            }
            FLAG = total/flagCounter;
        }
        //System.out.println("FLAG value: " + FLAG);
        return FLAG;
    }

    // only include MAC address of APs near the user, which are those APs that have RSSI higher than FLAG
    public ArrayList<String> sortMAC() {
        double FLAG = getFLAG();
        nearbyAPs = new ArrayList<>();
        for (HashMap.Entry<String, ArrayList<Double>> accessPoint : wifiResults.getAccesspoints().entrySet()) {
            if (accessPoint.getValue().get(0) > FLAG) {
                nearbyAPs.add(accessPoint.getKey());
            }
        }
        //System.out.println("Nearby APs are: " + nearbyAPs);
        return nearbyAPs;
    }

    // percentageScore checks which wifi APs in dataSet matches that of the current wifi scan
    // it also considers if the RSSIs of each point are "good" enough based on a set difference threshold
    // returns a score from 0 to 1. The higher the score, the more weightage the AP should be accorded
    public double percentageScore(dbdatapoint dataPoint, dbdatapoint wifiResults) {
        //System.out.println("Datapoint being evaluated: "+ dataPoint.getCoordinates());
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
                //System.out.println("Difference in RSSI values: " + (Math.abs(floorMacAdd.get(nearbyAPs.get(i)).get(0) - nearbyAPsRSSI.get(i))));
                if (Math.abs(floorMacAdd.get(nearbyAPs.get(i)).get(0) - nearbyAPsRSSI.get(i)) <= rssiThreshold) {
                    counter++;
                    //System.out.println("Adding counter to value: " + counter);
                }
            }
        }
        //System.out.println("Final counter value: " + counter);
        //System.out.println("Raw Score: "+((double) counter/nearbyAPs.size()));
        return (double) counter/nearbyAPs.size();
    }

    // topKPercentage compares the top scoring wifi APs in the dataSet and returns the top k results as an array
    public ArrayList<dbdatapoint> topKPercentage() {

        double eachScore = 0;

        HashMap<dbdatapoint, Double> dataScore = new HashMap<>();

        //System.out.println("DataSet: " + dataSet);
        //System.out.println("DataSet size: " + dataSet.size());

        for (int i = 0; i < dataSet.size(); i++) {
            eachScore = percentageScore(dataSet.get(i), wifiResults);
            //System.out.println("eachScore: " + eachScore);
            dataScore.put(dataSet.get(i), eachScore);
        }

        //System.out.println("datascore: "+dataScore);

        LinkedHashMap<dbdatapoint, Double> sortedDataScore = sortByValues(dataScore, "descending");
        //System.out.println("sortedDataScore: " + sortedDataScore);

        ArrayList<dbdatapoint> topKScores = new ArrayList<>();
        //System.out.println("sortedDataScore: " + sortedDataScore);

        for (dbdatapoint i : sortedDataScore.keySet()) {
            if (topKScores.size() < k) {
                topKScores.add(i);
            }
        }

        //System.out.println("Database BSSIDs used: " + topKScores);
        return topKScores;
    }

    // getEuclideanDistance attempts to estimate a distance between a point in nearbyAPs with that of the filtered array from topKPercentage by comparing difference in RSSI values
    public double getEuclideanDistance(dbdatapoint dataPoint, dbdatapoint wifiResults) {
        double total = 0;
        for (HashMap.Entry<String, ArrayList<Double>> dbaccessPoint : dataPoint.getAccesspoints().entrySet()) {
            for (int j = 0; j < nearbyAPs.size(); j++) {
                if (nearbyAPs.get(j).equals(dbaccessPoint.getKey())){
                    total += Math.pow(Math.abs(wifiResults.getAccesspoints().get(nearbyAPs.get(j)).get(0) - dbaccessPoint.getValue().get(0)), 2);
                    break;
                }
            }
        }

        //System.out.println("Finishing getting Euclidean Distance!");
        return Math.sqrt(total);
    }

    // estimateCoordinates uses the euclidean distance as a weightage for estimating coordinates, returning a predicted coordinate of where current position is
    // there is an additional restraint to improve accuracy, which is taking the top c results from the topKPercentage
    // taking top c results rather than setting an absolute distance threshold is preferable in ensuring prediction of position still occurs even with spare wifi mappings
    public Pair<Double, Double> estimateCoordinates() {
        double X1 = 0;
        double Y1 = 0;

        double sum_wx = 0;
        double sum_wy = 0;
        double sum_w = 0;

        ArrayList<dbdatapoint> clearedPercentagePoints = topKPercentage();
        HashMap<dbdatapoint, Double> euclidFilteredData = new HashMap<>();

        for (int i = 0; i < clearedPercentagePoints.size(); i++) {
            double di = getEuclideanDistance(clearedPercentagePoints.get(i), wifiResults);
            euclidFilteredData.put(clearedPercentagePoints.get(i), di);
        }
        //System.out.println("Euclidean Distance Pre-Filtered Hashmap: " + euclidFilteredData);

        LinkedHashMap<dbdatapoint, Double> sortedEuclidFilteredData = sortByValues(euclidFilteredData, "ascending");
        //System.out.println("Sorted Euclidean Filtered Hashmap: " + sortedEuclidFilteredData);
        ArrayList<dbdatapoint> topCFilteredDist = new ArrayList<>();

        for (dbdatapoint i : sortedEuclidFilteredData.keySet()) {
            if (topCFilteredDist.size() < c) {
                topCFilteredDist.add(i);
            }
        }
        //System.out.println("Euclidean Distance Post-Filtered List: " + topCFilteredDist);
        for (int i = 0; i < topCFilteredDist.size(); i++) {
            double di = getEuclideanDistance(topCFilteredDist.get(i), wifiResults);
            //System.out.println("Coordinates of point being used: "+ topCFilteredDist.get(i).getCoordinates());
            //System.out.println(wifiResults.getCoordinates());
            //System.out.println("EuclideanDistance is: " + di);
            double w = 0;
            if (di > 0) {
                w = 1 / di;
            }
            sum_w += w;
            sum_wx += w*topCFilteredDist.get(i).getCoordinates().get(0);
            sum_wy += w*topCFilteredDist.get(i).getCoordinates().get(1);
        }

        X1 = sum_wx / sum_w;
        Y1 = sum_wy / sum_w;

        Pair<Double, Double> coordinates = new Pair<>(X1, Y1);
        return coordinates;
    }

    // method to sort a hashmap by values
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
