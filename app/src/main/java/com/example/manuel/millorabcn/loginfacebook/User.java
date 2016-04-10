package com.example.manuel.millorabcn.loginfacebook;

import java.io.Serializable;

public class User implements Serializable{

    public String name;
    public String email;
    public String facebookID;
    public String gender;
    public String pictureUrl;

    public User() {
    }

    public String getPictureUrl() {
        return pictureUrl;
    }
    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getFacebookID() {
        return facebookID;
    }
    public void setFacebookID(String facebookID) {
        this.facebookID = facebookID;
    }
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }

}
