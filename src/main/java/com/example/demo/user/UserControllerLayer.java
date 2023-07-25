package com.example.demo.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;

@RestController
public class UserControllerLayer {

    @Autowired
    UserServiceLayer userServiceLayer;

    // Return the User data for the user with the associated username as per the incoming request
    // User data includes username plus their entire inventory list
    @GetMapping("users")
    public @ResponseBody User getUser(Principal principal) {
        return userServiceLayer.getUser(principal.getName());
    }

    @GetMapping("/heartbeat")
    public @ResponseBody Boolean heartbeat() { return true; }

    // Return the Inventory data for the user with the associated username
    @GetMapping("users/inventory")
    public @ResponseBody HashMap<Long, FoodItem> getUsersInventory(Principal principal) {
        return userServiceLayer.getUsersInventory(principal.getName());
    }

    // Return the food item data for the user with the associated username and associated food item name
    @GetMapping("users/inventory/{foodItem}")
    public @ResponseBody FoodItem getFoodItemForUser(Principal principal, @PathVariable String foodItem) {
        return userServiceLayer.getFoodItemForUser(principal.getName(), foodItem);
    }

    // Method to add an item to a user's inventory
    @PostMapping("users/inventory")
    public @ResponseBody Boolean addItemToUserInventory(Principal principal, @RequestBody FoodItem foodItem) {
        return userServiceLayer.addItemToUserInventory(principal.getName(), foodItem);
    }

    // Method to remove an item from a User's inventory
    @DeleteMapping("users/inventory/{foodItemId}")
    public @ResponseBody Boolean deleteItemFromUserInventory(Principal principal, @PathVariable Long foodItemId) {
        return userServiceLayer.deleteItemFromUserInventory(principal.getName(), foodItemId);
    }

    // Return the Shopping list data for the user with the associated username
    @GetMapping("users/shopping-list")
    public @ResponseBody List<FoodItem> getUserShoppingList(Principal principal) {
        return userServiceLayer.getUserShoppingList(principal.getName());
    }

    // Method to add an item to user's shopping list
    @GetMapping("users/shopping-list/{foodItem}")
    public @ResponseBody Boolean addItemToUserShoppingList(Principal principal, @PathVariable String foodItem) {
        return userServiceLayer.addItemToUserShoppingList(principal.getName(), foodItem);
    }

    // Method to remove an item from user's shopping list
    @DeleteMapping("users/shopping-list/{foodItem}")
    public @ResponseBody Boolean removeItemFromUserShoppingList(Principal principal, @PathVariable String foodItem) {
        return userServiceLayer.removeItemFromUserShoppingList(principal.getName(), foodItem);
    }

    // Method to clear shopping list
    @DeleteMapping("users/shopping-list")
    public @ResponseBody Boolean deleteUserShoppingList(Principal principal) {
        return userServiceLayer.deleteUserShoppingList(principal.getName());
    }

    // method for testing endpoint access from a client
    @GetMapping("sampleTest")
    public @ResponseBody User getSampleUsers() {
        return new User("sampleName");
    }
}
