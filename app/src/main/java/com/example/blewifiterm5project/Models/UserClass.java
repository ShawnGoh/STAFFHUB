package com.example.blewifiterm5project.Models;

import java.util.ArrayList;

public class UserClass {
    private String name;
    private String profile_picture;
    private String status="offline";
    private String admin = "N";
    private String email;

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
