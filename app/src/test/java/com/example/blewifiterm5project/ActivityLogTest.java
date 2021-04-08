package com.example.blewifiterm5project;

import com.example.blewifiterm5project.Models.ActivityLog;

import org.junit.*;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ActivityLogTest {
    static ActivityLog activityLog;
    static Date date;
    static String message;

    @Before
    public void setUp(){
        date = new Date();
        message = "test";
    }

    @Test
    public void testDefaultUse(){
        activityLog = new ActivityLog(date,message);
        assertEquals(activityLog.getTime(),date);
        assertEquals(activityLog.getMessage(),message);
    }

    @Test
    public void testNullDate(){
        activityLog = new ActivityLog(null, message);
        assertNull(activityLog.getTime());
    }

    @Test
    public void testNullMessage(){
        activityLog = new ActivityLog(date,null);
        assertNull(activityLog.getMessage());
    }

    @Test
    public void testEmptyString(){
        activityLog = new ActivityLog(date,"");
        assertNotNull(activityLog.getMessage());
    }

}
