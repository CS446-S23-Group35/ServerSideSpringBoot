package com.example.demo.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Service
public class UserServiceLayer {

    @Autowired
    UserDBLayer userDBLayer;

    @Autowired
    InventoryDBLayer inventoryDBLayer;

    @Autowired
    UserErrorLogger userErrorLogger;

    public User getUser(String userName) {
        User curUser = userDBLayer.getUser(userName);

        for(FoodItem foodItem : curUser.getShoppingList()){
            if(!foodItem.validateFoodItem()){
                userErrorLogger.addUserOfInterest(curUser.getId());
            }
        }

        return curUser;
    }

    public HashMap<Long, FoodItem> getUsersInventory(String id) {
        return userDBLayer.getUsersInventory(id);
    }

    public FoodItem getFoodItemForUser(String id, String foodItem) {
        return userDBLayer.getFoodItemForUser(id, foodItem);
    }

    public Boolean addItemToUserInventory(String id, FoodItem foodItem) {
        if (userDBLayer.getUser(id) == null)
            return false;
        return inventoryDBLayer.addItemForUser(id, foodItem) && userDBLayer.addItemToUserInventory(id, foodItem);
    }

    public Boolean deleteItemFromUserInventory(String id, Long foodItem) {
        return userDBLayer.deleteItemFromUserInventory(id, foodItem);
    }

    public List<FoodItem> getUserShoppingList(String id) {
        List<FoodItem> shoppingList = userDBLayer.getUserShoppingList(id);
        // ensure items in shopping list have an "NA" value of expiration date
        for (int i = 0; i < shoppingList.size(); i++) {
            shoppingList.get(i).setExpiryDate(new Date(0L));
        }
        return shoppingList;
    }

    public Boolean addItemToUserShoppingList(String id, String foodItem) {
        return userDBLayer.addItemToUserShoppingList(id, foodItem);
    }

    public Boolean removeItemFromUserShoppingList(String id, String foodItem) {
        return userDBLayer.removeItemFromUserShoppingList(id, foodItem);
    }

    public Boolean deleteUserShoppingList(String id) {
        return userDBLayer.deleteUserShoppingList(id);
    }

}
