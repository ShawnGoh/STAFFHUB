package com.example.blewifiterm5project;

import com.example.blewifiterm5project.Models.dbdatapoint;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DataPointTest {
    dbdatapoint DataPoint;
    HashMap<String, ArrayList<Double>> accessPoints;
    ArrayList<Float> coordinates;

    @Before
    public void setUp(){
        ArrayList<Double> arrayList = new ArrayList<>();
        accessPoints = new HashMap<>();
        coordinates = new ArrayList<>();

        Random random = new Random();
        while(random.nextDouble()<0.1){
            arrayList.add(random.nextDouble());
        }
        accessPoints.put("Test",arrayList);
        while(random.nextFloat()<0.1f){
            coordinates.add(random.nextFloat());
        }
    }

    @Test
    public void testGetter(){
        DataPoint = new dbdatapoint(accessPoints,coordinates);
        assertEquals(DataPoint.getAccesspoints(),accessPoints);
        assertEquals(DataPoint.getCoordinates(),coordinates);
    }

    @Test
    public void testEmptyConstructor(){
        DataPoint = new dbdatapoint();
        assertNull(DataPoint.getCoordinates());
        assertNull(DataPoint.getAccesspoints());
    }
}
