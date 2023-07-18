package com.example.demo.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
public class UserControllerLayer {

    @Autowired
    UserServiceLayer userServiceLayer;

    // Return the User data for the user with the associated username as per the incoming request
    // User data includes username plus their entire inventory list
    @GetMapping("users/{id}")
    public @ResponseBody User getUser(@PathVariable String id) {
        return userServiceLayer.getUser(id);
    }

    // Return the Inventory data for the user with the associated username
    @GetMapping("users/{id}/inventory")
    public @ResponseBody HashMap<Long, FoodItem> getUsersInventory(@PathVariable String id) {
        return userServiceLayer.getUsersInventory(id);
    }

    // Return the food item data for the user with the associated username and associated food item name
    @GetMapping("users/{id}/inventory/{foodItem}")
    public @ResponseBody FoodItem getFoodItemForUser(@PathVariable String id, @PathVariable String foodItem) {
        return userServiceLayer.getFoodItemForUser(id, foodItem);
    }

    // Method to add an item to a user's inventory
    @PostMapping("users/{id}/inventory")
    public @ResponseBody Boolean addItemToUserInventory(@PathVariable String id, @RequestBody FoodItem foodItem) {
        return userServiceLayer.addItemToUserInventory(id, foodItem);
    }

    // Method to remove an item from a User's inventory
    @DeleteMapping("users/{id}/inventory/{foodItemId}")
    public @ResponseBody Boolean deleteItemFromUserInventory(@PathVariable String id, @PathVariable Long foodItemId) {
        return userServiceLayer.deleteItemFromUserInventory(id, foodItemId);
    }

    // Return the Shopping list data for the user with the associated username
    @GetMapping("users/{id}/shopping-list")
    public @ResponseBody List<FoodItem> getUserShoppingList(@PathVariable String id) {
        return userServiceLayer.getUserShoppingList(id);
    }

    // Method to add an item to user's shopping list
    @GetMapping("users/{id}/shopping-list/{foodItem}")
    public @ResponseBody Boolean addItemToUserShoppingList(@PathVariable String id, @PathVariable String foodItem) {
        return userServiceLayer.addItemToUserShoppingList(id, foodItem);
    }

    // Method to remove an item from user's shopping list
    @DeleteMapping("users/{id}/shopping-list/{foodItem}")
    public @ResponseBody Boolean removeItemFromUserShoppingList(@PathVariable String id, @PathVariable String foodItem) {
        return userServiceLayer.removeItemFromUserShoppingList(id, foodItem);
    }

    // Method to clear shopping list
    @DeleteMapping("users/{id}/shopping-list")
    public @ResponseBody Boolean deleteUserShoppingList(@PathVariable String id) {
        return userServiceLayer.deleteUserShoppingList(id);
    }

    // method for testing endpoint access from a client
    @GetMapping("sampleTest")
    public @ResponseBody User getSampleUsers() {
        return new User("sampleName");
    }
}
