package com.example.demo.user;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.HashMap;

@Entity
@Table(name="users")
public class User {

    @Id
    private String id;

    @JdbcTypeCode(SqlTypes.JSON)
    private HashMap<Long, FoodItem> inventory = new HashMap<Long, FoodItem>();

    @JdbcTypeCode(SqlTypes.JSON)
    private ArrayList<FoodItem> shopping_list = new ArrayList<FoodItem>();


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

    public ArrayList<FoodItem> getShoppingList() { return shopping_list; }

    public void setShoppingList(ArrayList<FoodItem> shoppingList) { this.shopping_list = shoppingList; }

    public User() { }

    public User(String id) {
        this.id = id;
    }

    public User(String userName, HashMap<Long, FoodItem> inventory, ArrayList<FoodItem> shoppingList) {
        this.id = userName;
        this.inventory = inventory;
        this.shopping_list = shoppingList;
    }
}
