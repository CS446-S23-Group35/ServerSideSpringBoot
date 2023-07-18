package com.example.demo.user;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.HashMap;

@Entity
@Table(name="users")
public class User {

    @Id
    private String id;

    @JdbcTypeCode(SqlTypes.JSON)
    private HashMap<Long, FoodItem> inventory = new HashMap<Long, FoodItem>();

    @JdbcTypeCode(SqlTypes.JSON)
    private HashMap<Long, FoodItem> shoppingList = new HashMap<Long, FoodItem>();


    public String getId() {
        return id;
    }

    public void setId(String userName) {
        this.id = userName;
    }

    public HashMap<Long, FoodItem> getInventory() {
        return inventory;
    }

    public void setInventory(HashMap<Long, FoodItem> inventory) {
        this.inventory = inventory;
    }

    public HashMap<Long, FoodItem> getShoppingList() { return shoppingList; }

    public void setShoppingList(HashMap<Long, FoodItem> shoppingList) { this.shoppingList = shoppingList; }

    public User() { }

    public User(String userName) {
        this.id = userName;
        this.inventory = new HashMap<Long, FoodItem>();
        this.shoppingList = new HashMap<Long, FoodItem>();
    }

    public User(String userName, HashMap<Long, FoodItem> inventory, HashMap<Long, FoodItem> shoppingList) {
        this.id = userName;
        this.inventory = inventory;
        this.shoppingList = shoppingList;
    }

}
