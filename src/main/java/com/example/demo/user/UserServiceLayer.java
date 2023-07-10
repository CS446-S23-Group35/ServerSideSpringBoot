package com.example.demo.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceLayer {

    @Autowired
    UserDBLayer userDBLayer;
    public User getUser(String userName){
        return userDBLayer.getUser(userName);
    }

    public List<FoodItem> getUsersInventory(String id) {
        return userDBLayer.getUsersInventory(id);

    }
    public FoodItem getFoodItemForUser(String id, String foodItem) {
        return userDBLayer.getFoodItemForUser(id, foodItem);
    }

    public Boolean addItemToUserInventory(String id, String foodItem) {
        return userDBLayer.addItemToUserInventory(id, foodItem);
    }

    public Boolean deleteItemFromUserInventory(String id, String foodItem) {
        return userDBLayer.deleteItemFromUserInventory(id, foodItem);
    }

    public List<FoodItem> getUserShoppingList(String id) {
        List<FoodItem> shoppingList = userDBLayer.getUserShoppingList(id);
        // ensure items in shopping list have an "NA" value of expiration date
        for (int i = 0; i < shoppingList.size(); i++) {
            shoppingList.get(i).setTimeTillExpiration(-1);
        }
        return shoppingList;
    }

    public Boolean addItemToUserShoppingList(String id, String foodItem) {
        return userDBLayer.addItemToUserShoppingList(id,foodItem);
    }

    public Boolean removeItemFromUserShoppingList(String id, String foodItem) {
        return userDBLayer.removeItemFromUserShoppingList(id,foodItem);
    }

    public Boolean deleteUserShoppingList(String id) {
        return userDBLayer.deleteUserShoppingList(id);
    }


}
