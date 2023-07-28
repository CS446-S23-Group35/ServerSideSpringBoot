package com.example.demo.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceLayer {

    @Autowired
    UserDBLayer userDBLayer;

    @Autowired
    InventoryDBLayer inventoryDBLayer;

    public User getUser(String userName) {
        return userDBLayer.getUser(userName);
    }

    public HashMap<Long, FoodItem> getUsersInventory(String id) {
        return userDBLayer.getUsersInventory(id);
    }

    public FoodItem getFoodItemForUser(String id, String foodItem) {
        return userDBLayer.getFoodItemForUser(id, foodItem);
    }

    public FoodItem addItemToUserInventory(String id, FoodItem foodItem) {
        if (userDBLayer.getUser(id) == null)
            return new FoodItem();
        FoodItem actualFoodItem = inventoryDBLayer.addItemForUser(id, foodItem);
        userDBLayer.addItemToUserInventory(id, actualFoodItem);
        return actualFoodItem;
    }

    public Map<Long, FoodItem> deleteItemFromUserInventory(String id, Long foodItem) {
        inventoryDBLayer.removeItemForUser(id, foodItem);
        return userDBLayer.deleteItemFromUserInventory(id, foodItem);
    }

    public List<FoodItem> getUserShoppingList(String id) {
        return userDBLayer.getUserShoppingList(id);
    }

    public List<FoodItem> addItemToUserShoppingList(String id, String foodItem) {
        return userDBLayer.addItemToUserShoppingList(id, foodItem);
    }

    public List<FoodItem> removeItemFromUserShoppingList(String id, String foodItem) {
        return userDBLayer.removeItemFromUserShoppingList(id, foodItem);
    }

    public List<FoodItem> deleteUserShoppingList(String id) {
        return userDBLayer.deleteUserShoppingList(id);
    }

    public User register(String id) {
        return userDBLayer.createUser(id);
    }

    public List<FoodItem> addItemsToUserInventory(String name, List<FoodItem> itemList) {
        List<FoodItem> actualItemList = inventoryDBLayer.addItemsForUser(name, itemList);
        return userDBLayer.addItemsToUserInventory(name, actualItemList);
    }

    public List<FoodItem> addItemsToUserShoppingList(String name, List<String> itemList) {
        return userDBLayer.addItemsToUserShoppingList(name, itemList);
    }

    public List<FoodItem> deleteItemsFromUserShoppingList(String name, List<String> itemList) {
        return userDBLayer.deleteItemsFromUserShoppingList(name, itemList);
    }

    public Map<Long, FoodItem> deleteItemsFromUserInventory(String name, List<FoodItem> itemList) {
        return userDBLayer.deleteItemsFromUserInventory(name, itemList);
    }
}
