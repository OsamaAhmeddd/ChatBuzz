package com.zabih.chatBuzz.Activities.Models;

import java.io.Serializable;

public class AdvertistmentModel implements Serializable {
    String name;
    String createdBy;
    String imgPath;

    public AdvertistmentModel() {

    }

    public AdvertistmentModel(String name, String createdBy, String imgPath) {
        this.name = name;
        this.createdBy = createdBy;
        this.imgPath = imgPath;
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

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }
}
