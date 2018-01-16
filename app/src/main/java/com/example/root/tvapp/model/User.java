package com.example.root.tvapp.model;

/**
 * Created by root on 14/01/18.
 */

public class User {

    private String userName;
    private String language;
    private String displayMode;

    public User(String userName, String language, String displayMode){
        this.userName = userName;
        this.language = language;
        this.displayMode = displayMode;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDisplayMode() {
        return displayMode;
    }

    public void setDisplayMode(String displayMode) {
        this.displayMode = displayMode;
    }

    public String toString(){
        return "Username: " + this.userName + ", language: " + this.language + ", display mode: " + this.displayMode;
    }
}
