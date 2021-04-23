package com.example.blewifiterm5project.Models;

import java.util.ArrayList;
import java.util.HashMap;

// class to store wifi scanning results and their respective coordinates
public class dbdatapoint {

    HashMap<String, ArrayList<Double>> accesspoints;
    ArrayList<Float> coordinates;
    String docid = "None";

    public dbdatapoint() {
    }

    // constructor
    public dbdatapoint(HashMap<String, ArrayList<Double>> accesspoints, ArrayList<Float> coordinates) {
        this.accesspoints = accesspoints;
        this.coordinates = coordinates;
    }

    public dbdatapoint(dbdatapoint dbdatapoint) {
        this.accesspoints = dbdatapoint.getAccesspoints();
        this.coordinates = dbdatapoint.getCoordinates();
    }

    // getter method for accesspoints
    public HashMap<String, ArrayList<Double>> getAccesspoints() {
        return accesspoints;
    }

    // setter method for accesspoints
    public void setAccesspoints(HashMap<String, ArrayList<Double>> accesspoints) {
        this.accesspoints = accesspoints;
    }

    // getter method for coordinates
    public ArrayList<Float> getCoordinates() {
        return coordinates;
    }

    // setter method for coordinates
    public void setCoordinates(ArrayList<Float> coordinates) {
        this.coordinates = coordinates;
    }

    // getter method for document id
    public String getDocid() {
        return docid;
    }

    // setter method for document id
    public void setDocid(String docid) {
        this.docid = docid;
    }
}
