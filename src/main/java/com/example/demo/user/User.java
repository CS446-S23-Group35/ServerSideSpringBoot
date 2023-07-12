package com.example.demo.user;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.ArrayList;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name="")
public class User {

    @Id
    private String userName;

    @JdbcTypeCode(SqlTypes.JSON)
    private ArrayList<FoodItem> inventory;
    
    @JdbcTypeCode(SqlTypes.JSON)
    private ArrayList<FoodItem> shoppingList;


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

    public ArrayList<FoodItem> getShoppingList() { return shoppingList;}

    public void setShoppingList(ArrayList<FoodItem> shoppingList) {this.shoppingList = shoppingList;}

    public User() {
    }

    public User(String userName){
        this.userName = userName;
        this.inventory = new ArrayList<FoodItem>();
        this.shoppingList = new ArrayList<FoodItem>();

    }
    public User(String userName, ArrayList<FoodItem> inventory,  ArrayList<FoodItem> shoppingList) {
        this.userName = userName;
        this.inventory = inventory;
        this.shoppingList = shoppingList;

    }




}
