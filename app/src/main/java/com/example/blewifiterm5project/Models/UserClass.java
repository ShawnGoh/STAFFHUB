package com.example.blewifiterm5project.Models;

import java.util.ArrayList;
import java.util.Date;

public class UserClass {
    private String name;
    private String profile_picture;
    private String status="offline";
    private String admin = "N";
    private String email;
    private String statusmessage;
    private int paid_leave;
    private int sick_leave;
    private ArrayList<String> Activitylist = new ArrayList<>();
    private ArrayList<String> Activitydatelist = new ArrayList<>();

    public UserClass(String name, String profile_picture, String email) {
        this.name = name;
        this.profile_picture = profile_picture;
        this.email = email;
    }

    public UserClass() {
    }

    public UserClass(UserClass userClass){
        this.name = userClass.getName();
        this.profile_picture = userClass.getProfile_picture();
        this.email = userClass.getEmail();
        this.admin = userClass.getAdmin();
    }

    public void setActivitylist(ArrayList<String> activitylist) {
        Activitylist = activitylist;
    }

    public void setActivitydatelist(ArrayList<String> activitydatelist) {
        Activitydatelist = activitydatelist;
    }

    public ArrayList<String> getActivitydatelist() {
        return Activitydatelist;
    }



    public ArrayList<String> getActivitylist() {
        return Activitylist;
    }



    public String getStatusmessage() {
        return statusmessage;
    }

    public void setStatusmessage(String statusmessage) {
        this.statusmessage = statusmessage;
    }

    public int getPaid_leave() {
        return paid_leave;
    }

    public void setPaid_leave(int paid_leave) {
        this.paid_leave = paid_leave;
    }

    public int getSick_leave() {
        return sick_leave;
    }

    public void setSick_leave(int sick_leave) {
        this.sick_leave = sick_leave;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAdmin() {
        return admin;
    }
}
