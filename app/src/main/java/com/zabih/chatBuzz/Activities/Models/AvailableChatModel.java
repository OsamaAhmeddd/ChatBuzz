package com.zabih.chatBuzz.Activities.Models;

import java.io.Serializable;

public class AvailableChatModel implements Serializable {
    private String name;
    private String dateCreated;
    private String chatRole;

    public String getChatRole() {
        return chatRole;
    }

    public void setChatRole(String chatRole) {
        this.chatRole = chatRole;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }
}
