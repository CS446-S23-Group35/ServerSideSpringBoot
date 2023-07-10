package com.example.demo.user;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserDBLayer {

    public User getUser(String userName){
        return new User(userName +"_dataAccessLayerUser");
    }

    public List<FoodItem> getUsersInventory(String id) {
        return new ArrayList<FoodItem>();
    }

    public FoodItem getFoodItemForUser(String id, String foodItem) {
        return new FoodItem();
    }

    public Boolean addItemToUserInventory(String id, String foodItem) {
        return true;
    }

    public Boolean deleteItemFromUserInventory(String id, String foodItem) {
        return true;

    }

    public List<FoodItem> getUserShoppingList(String id) {
        return new ArrayList<FoodItem>();
    }

    public Boolean addItemToUserShoppingList(String id, String foodItem) {
        return true;
    }

    public Boolean removeItemFromUserShoppingList(String id, String foodItem) {
        return true;
    }

    public Boolean deleteUserShoppingList(String id) {
        return true;
    }
}
