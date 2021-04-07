package com.example.blewifiterm5project.Models;

import java.util.Date;

public class ActivityLog {
    Date time;
    String message;

    public ActivityLog(Date time, String message) {
        this.time = time;
        this.message = message;
    }

    public Date getTime() {
        return time;
    }

    public String getMessage() {
        return message;
    }
}
