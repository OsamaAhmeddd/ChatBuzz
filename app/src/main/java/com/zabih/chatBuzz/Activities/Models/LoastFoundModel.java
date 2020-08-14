package com.zabih.chatBuzz.Activities.Models;

import java.io.Serializable;

public class LoastFoundModel implements Serializable {
    String name, imagePath, createdBy,role;
String desc;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public LoastFoundModel() {

    }

    public LoastFoundModel(String name, String imagePath, String createdBy, String role) {
        this.name = name;
        this.imagePath = imagePath;
        this.createdBy = createdBy;
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public LoastFoundModel(String name, String imagePath, String createdBy) {
        this.name = name;
        this.imagePath = imagePath;
        this.createdBy = createdBy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
