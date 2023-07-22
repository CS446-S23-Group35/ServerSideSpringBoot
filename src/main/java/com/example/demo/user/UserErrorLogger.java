package com.example.demo.user;


import java.util.ArrayList;

public class UserErrorLogger {

    private ArrayList<String> userIDsOfInterest;
    public void addUserOfInterest(String id) {
        this.userIDsOfInterest.add(id);
    }

    public void setUserIDsOfInterest(ArrayList<String> userIDsOfInterest) {
        this.userIDsOfInterest = userIDsOfInterest;
    }
    public ArrayList<String> getUserIDsOfInterest() {
       return this.userIDsOfInterest;
    }
}
