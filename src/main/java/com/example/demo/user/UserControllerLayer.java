package com.example.demo.user;

import com.example.demo.recipe.OpenSearchImpl;
import com.example.demo.recipe.Recipe;
import com.example.demo.recipe.Searcher;
import com.example.demo.recipe.Recipe.DietartyMetadata;
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

    @JsonProperty("page_off")
    int page_off;

    @JsonProperty("page_size")
    int page_size;

    @JsonProperty("dietary_restrictions")
    List<String> dietary_restrictions;
}

class FoodItemListRequestBody {
    @JsonProperty("item_list")
    List<FoodItem> item_list;
}

class ShoppingListRequestBody {
    @JsonProperty
    List<String> item_list;
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
        // If page size is 0, use default page size
        Searcher.Page page = (body.page_size == 0) ? new Searcher.Page() : new Searcher.Page(body.page_off, body.page_size);

        HashMap<Long, FoodItem> inventory = getUser(principal).getInventory();
        List<FoodItem> inventory_list = new ArrayList<>(inventory.values());
        List<FoodItem> expiring_inventory_list = new ArrayList<>(inventory_list);

        expiring_inventory_list = expiring_inventory_list.stream()
            .filter(item -> TimeUnit.DAYS.convert(Math.abs(item.getExpiryDate().getTime() - new Date().getTime()), TimeUnit.MILLISECONDS) < 7)
            .toList();

        List<String> inventory_ingredients = new ArrayList<>(inventory_list.stream()
            .map(FoodItem::getName)
            .toList());
        if (body.includedIngredients != null) {
            inventory_ingredients.addAll(body.includedIngredients);
        }

        List<String> expiring_ingredients = new ArrayList<>(expiring_inventory_list.stream()
            .map(FoodItem::getName)
            .toList());


        List<String> excludedItems = (body.excludedIngredients == null) ? new ArrayList<>() : body.excludedIngredients;
        
        List<Searcher.DietaryRestrictions> dietary_restrictions = new ArrayList<>();
        if (body.dietary_restrictions != null) {
            for (String restriction : body.dietary_restrictions) {
                Searcher.DietaryRestrictions r = Searcher.DietaryRestrictions.fromString(restriction);
                if (r != null) dietary_restrictions.add(r);
            }
        }

        Searcher.Filters filters = Searcher.Filters.empty()
        .withExcludedIngredients(excludedItems)
        .withInventoryIngredients(inventory_ingredients)
        .withExpiringIngredients(expiring_ingredients)
        .withDietaryRestrictions(dietary_restrictions);

        String query = body.name.strip();
        if (query == "") {
            return searcher.SearchByInventory(filters,page);
        } else {
            return searcher.SearchByNameWithInventory(query, filters, page);
        }
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
    public @ResponseBody Map<Long, FoodItem> addItemToUserInventory(Principal principal, @RequestBody FoodItem foodItem) {
        return userServiceLayer.addItemToUserInventory(principal.getName(), foodItem);
    }

    @PostMapping("users/inventory/add-items")
    public @ResponseBody List<FoodItem> addItemsToUserInventory(Principal principal, @RequestBody FoodItemListRequestBody requestBody) {
        return userServiceLayer.addItemsToUserInventory(principal.getName(), requestBody.item_list);
    }

    @PostMapping("users/inventory/delete-items")
    public @ResponseBody Map<Long, FoodItem> deleteItemsFromUserInventory(Principal principal, @RequestBody FoodItemListRequestBody requestBody) {
        return userServiceLayer.deleteItemsFromUserInventory(principal.getName(), requestBody.item_list);
    }

    // Method to remove an item from a User's inventory
    @DeleteMapping("users/inventory/{foodItemId}")
    public @ResponseBody Map<Long, FoodItem> deleteItemFromUserInventory(Principal principal, @PathVariable Long foodItemId) {
        return userServiceLayer.deleteItemFromUserInventory(principal.getName(), foodItemId);
    }

    // Return the Shopping list data for the user with the associated username
    @GetMapping("users/shopping-list")
    public @ResponseBody List<FoodItem> getUserShoppingList(Principal principal) {
        return userServiceLayer.getUserShoppingList(principal.getName());
    }

    // Method to add an item to user's shopping list
    @GetMapping("users/shopping-list/{foodItem}")
    public @ResponseBody List<FoodItem> addItemToUserShoppingList(Principal principal, @PathVariable String foodItem) {
        return userServiceLayer.addItemToUserShoppingList(principal.getName(), foodItem);
    }

    // Method to add items to user's shopping list
    @PostMapping("users/shopping-list/add-items")
    public @ResponseBody List<FoodItem> addItemsToUserShoppingList(Principal principal, @RequestBody ShoppingListRequestBody requestBody) {
        return userServiceLayer.addItemsToUserShoppingList(principal.getName(), requestBody.item_list);
    }

    @PostMapping("users/shopping-list/delete-items")
    public @ResponseBody List<FoodItem> deleteItemsFromUserShoppingList(Principal principal, @RequestBody ShoppingListRequestBody requestBody) {
        return userServiceLayer.deleteItemsFromUserShoppingList(principal.getName(), requestBody.item_list);
    }

    // Method to remove an item from user's shopping list
    @DeleteMapping("users/shopping-list/{foodItem}")
    public @ResponseBody List<FoodItem> removeItemFromUserShoppingList(Principal principal, @PathVariable String foodItem) {
        return userServiceLayer.removeItemFromUserShoppingList(principal.getName(), foodItem);
    }

    // Method to clear shopping list
    @DeleteMapping("users/shopping-list")
    public @ResponseBody List<FoodItem> deleteUserShoppingList(Principal principal) {
        return userServiceLayer.deleteUserShoppingList(principal.getName());
    }
    // Method to return a user's analytics data
    @GetMapping("users/inventory/analytics")
    public @ResponseBody int getUserAnalytics(Principal principal) {
        return analyticsServiceLayer.getAnalytics(principal.getName());
    }

    // method for testing endpoint access from a client
    @GetMapping("sampleTest")
    public @ResponseBody User getSampleUsers() {
        return new User("sampleName");
    }
}
