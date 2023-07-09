package com.example.demo.user;
import java.util.ArrayList;

public class User {

    private String userName;
    private ArrayList<FoodItem> inventory;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public ArrayList<FoodItem> getInventory() {
        return inventory;
    }

    public void setInventory(ArrayList<FoodItem> inventory) {
        this.inventory = inventory;
    }

    public User(String userName, ArrayList<FoodItem> inventory) {
        this.userName = userName;
        this.inventory = inventory;
    }

    public User() {
    }
}
