package com.zabih.chatBuzz.Activities.Models;

import java.io.Serializable;

public class AnnouncementModel implements Serializable {

    String creater, Name;
    String date,time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public AnnouncementModel(String creater, String name, String role) {
        this.creater = creater;
        Name = name;
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    String role;
    public AnnouncementModel() {


    }

    public AnnouncementModel(String creater, String name) {
        this.creater = creater;
        Name = name;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }
}

