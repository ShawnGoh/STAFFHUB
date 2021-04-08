package com.example.blewifiterm5project.Models;

import java.util.ArrayList;
import java.util.HashMap;

public class dbdatapoint {

    HashMap<String, ArrayList<Double>> accesspoints;
    ArrayList<Float> coordinates;

    public dbdatapoint() {
    }

    public dbdatapoint(HashMap<String, ArrayList<Double>> accesspoints, ArrayList<Float> coordinates) {
        this.accesspoints = accesspoints;
        this.coordinates = coordinates;
    }

    public dbdatapoint(dbdatapoint dbdatapoint) {
        this.accesspoints = dbdatapoint.getAccesspoints();
        this.coordinates = dbdatapoint.getCoordinates();
    }

    public HashMap<String, ArrayList<Double>> getAccesspoints() {
        return accesspoints;
    }

    public void setAccesspoints(HashMap<String, ArrayList<Double>> accesspoints) {
        this.accesspoints = accesspoints;
    }

    public ArrayList<Float> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(ArrayList<Float> coordinates) {
        this.coordinates = coordinates;
    }
}
