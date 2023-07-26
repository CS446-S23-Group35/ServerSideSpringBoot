package com.example.demo.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;

@Repository
public class UserDBLayer {

    @Autowired
    UserRepository repository;

    public User getUser(String id) {
        return repository.findById(id).orElse(null);
    }

    public User createUser(String id) {
        return repository.save(new User(id));
    }

    public HashMap<Long, FoodItem> getUsersInventory(String id) {
        User user = getUser(id);
        if (getUser(id) == null) return null;
        return user.getInventory();
    }

    public FoodItem getFoodItemForUser(String id, String foodItem) {
        return new FoodItem();
    }

    public User addItemToUserInventory(String id, FoodItem foodItem) {
        User user = getUser(id);
        if (getUser(id) == null) return new User();
        user.getInventory().put(foodItem.getId(), foodItem);
        return repository.save(user);
    }

    public Boolean deleteItemFromUserInventory(String id, Long foodItemId) {
        User user = getUser(id);
        if (getUser(id) == null) return false;
        user.getInventory().remove(foodItemId);
        repository.save(user);
        return true;
    }

    public ArrayList<FoodItem> getUserShoppingList(String id) {
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
