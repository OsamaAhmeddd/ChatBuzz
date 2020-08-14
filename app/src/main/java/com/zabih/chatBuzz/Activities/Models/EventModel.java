package com.zabih.chatBuzz.Activities.Models;

import java.io.Serializable;

public class EventModel implements Serializable {
    String name, Date;

    public EventModel(String name, String date, String createdBy, String imgPath, String location, String description) {
        this.name = name;
        this.Date = date;
        this.createdBy = createdBy;
        this.imgPath = imgPath;
        this.location = location;
        this.description = description;
    }

    String createdBy;

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    String imgPath;


    String location,description;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EventModel(String name, String createdBy, String Date) {
        this.name = name;
        this.Date = Date;
        this.createdBy = createdBy;

    }

    public EventModel() {
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
