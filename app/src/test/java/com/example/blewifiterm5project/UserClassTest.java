package com.example.blewifiterm5project;

import android.app.Activity;

import com.example.blewifiterm5project.Models.UserClass;
import com.example.blewifiterm5project.Models.dbdatapoint;
import com.google.firebase.firestore.auth.User;

import org.junit.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class UserClassTest {
    UserClass userClass;
    ArrayList<String> ActivityList;
    ArrayList<String> ActivityDateList;
    String name;
    String profilePicture;
    String email;

    @Before
    public void setUp(){
        ActivityList = new ArrayList<>();
        ActivityDateList = new ArrayList<>();

        String test1 = "Test1";
        String test2 = "Test2";
        Random random = new Random();
        for(int i=0;i<50;i++){
            if(random.nextBoolean()){
                ActivityList.add(test1);
            }else{
                ActivityList.add(test2);
            }

            if(random.nextBoolean()){
                ActivityDateList.add(test1);
            }else{
                ActivityDateList.add(test2);
            }
        }

        name = "John";
        profilePicture = "Test";
        email = "user@blewifi.com";
    }

    @Test
    public void testDefaultConstructor(){
        userClass = new UserClass(name,profilePicture,email);
        assertEquals(name,userClass.getName());
        assertEquals(profilePicture,userClass.getProfile_picture());
        assertEquals(email,userClass.getEmail());
    }

    @Test
    public void testInvalidEmail(){
        email = "1234567890";
        userClass = new UserClass(name,profilePicture,email);
        assertEquals(email,userClass.getEmail());
    }

    @Test
    public void testEmptyConstructor(){
        userClass = new UserClass();
        assertNull(userClass.getName());
        assertNull(userClass.getProfile_picture());
        assertNull(userClass.getEmail());
    }

    @Test
    public void testDefaultValue(){
        userClass = new UserClass();
        assertEquals("offline",userClass.getStatus());
        assertEquals("N",userClass.getAdmin());
    }

    @Test
    public void testUserClassConstructor() {
        userClass = new UserClass(new UserClass(name,profilePicture,email));
        assertEquals(name,userClass.getName());
        assertEquals(profilePicture,userClass.getProfile_picture());
        assertEquals(email,userClass.getEmail());
    }

    @Test
    public void testSetter(){
        userClass = new UserClass();

        userClass.setActivitylist(ActivityList);
        userClass.setActivitydatelist(ActivityDateList);
        userClass.setEmail(email);
        userClass.setName(name);
        userClass.setProfile_picture(profilePicture);
        userClass.setPaid_leave(10);
        userClass.setSick_leave(20);
        userClass.setStatus("online");
        userClass.setStatusmessage("Testing");

        assertEquals(ActivityList,userClass.getActivitylist());
        assertEquals(ActivityDateList,userClass.getActivitydatelist());
        assertEquals(email,userClass.getEmail());
        assertEquals(name,userClass.getName());
        assertEquals(profilePicture,userClass.getProfile_picture());
        assertEquals(10,userClass.getPaid_leave());
        assertEquals(20,userClass.getSick_leave());
        assertEquals("online",userClass.getStatus());
        assertEquals("Testing",userClass.getStatusmessage());
    }
}
