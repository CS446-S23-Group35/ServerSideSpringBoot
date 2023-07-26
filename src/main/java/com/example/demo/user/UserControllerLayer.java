package com.example.demo.user;

import com.example.demo.recipe.OpenSearchImpl;
import com.example.demo.recipe.Recipe;
import com.example.demo.recipe.Searcher;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;
import java.util.concurrent.TimeUnit;

class RecipeQuery {
    @JsonProperty("name")
    String name;

    @JsonProperty("includedIngredients")
    List<String> includedIngredients;

    @JsonProperty("excludedIngredients")
    List<String> excludedIngredients;
}

@RestController
public class UserControllerLayer {
    Searcher searcher = new OpenSearchImpl(System.getenv("OPENSEARCH_ADDRESS"));

    @Autowired
    UserServiceLayer userServiceLayer;

    @Autowired
    AnalyticsServiceLayer analyticsServiceLayer;


    // Return the User data for the user with the associated username as per the incoming request
    // User data includes username plus their entire inventory list
    @GetMapping("users")
    public @ResponseBody User getUser(Principal principal) {
        return userServiceLayer.getUser(principal.getName());
    }

    @GetMapping("register")
    public @ResponseBody User register(Principal principal) {
        return userServiceLayer.register(principal.getName());
    }

    @PostMapping(value = "/recipes", consumes = {"application/json"})
    public @ResponseBody List<Recipe> getRecipes(Principal principal, @RequestBody RecipeQuery body) {
        HashMap<Long, FoodItem> inventory = getUser(principal).getInventory();
        List<FoodItem> inventory_list = new ArrayList<>(inventory.values());
        List<FoodItem> expiring_inventory_list = new ArrayList<>(inventory_list);

        expiring_inventory_list = expiring_inventory_list.stream()
            .filter(item -> TimeUnit.DAYS.convert(Math.abs(item.getExpiryDate().getTime() - new Date().getTime()), TimeUnit.MILLISECONDS) < 7)
            .toList();

        List<String> inventory_ingredients = new ArrayList<>(inventory_list.stream()
            .map(FoodItem::getName)
            .toList());
        inventory_ingredients.addAll(body.includedIngredients);

        List<String> expiring_ingredients = new ArrayList<>(expiring_inventory_list.stream()
            .map(FoodItem::getName)
            .toList());

        return searcher.SearchByInventory(
            Searcher.Filters.empty().withExcludedIngredients(body.excludedIngredients)
            .withInventoryIngredients(inventory_ingredients)
            .withExpiringIngredients(expiring_ingredients)
        );
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
    // Method to return a user's analytics data
    @GetMapping("users/inventory/analytics")
    public @ResponseBody String getUserAnalytics(Principal principal) {
        return analyticsServiceLayer.getAnalytics(principal.getName());
    }

    // method for testing endpoint access from a client
    @GetMapping("sampleTest")
    public @ResponseBody User getSampleUsers() {
        return new User("sampleName");
    }
}
