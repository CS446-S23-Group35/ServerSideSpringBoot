package com.example.demo.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        if (user == null) return null;
        return user.getInventory();
    }

    public FoodItem getFoodItemForUser(String id, String foodItem) {
        return new FoodItem();
    }

    public Map<Long, FoodItem> addItemToUserInventory(String id, FoodItem foodItem) {
        User user = getUser(id);
        if (user == null) return new HashMap<>();
        user.getInventory().put(foodItem.getId(), foodItem);
        return repository.save(user).getInventory();
    }

    public Map<Long, FoodItem> deleteItemFromUserInventory(String id, Long foodItemId) {
        User user = getUser(id);
        if (user == null) return new HashMap<>();
        user.getInventory().remove(foodItemId);
        repository.save(user);
        return user.getInventory();
    }

    public ArrayList<FoodItem> getUserShoppingList(String id) {
        User user = getUser(id);
        if (user == null) return new ArrayList<>();
        return user.getShoppingList();
    }

    public List<FoodItem> addItemToUserShoppingList(String id, String foodItem) {
        User user = getUser(id);
        if (user == null) return new ArrayList<>();
        user.getShoppingList().add(new FoodItem(foodItem));
        repository.save(user);
        return user.getShoppingList();
    }

    public List<FoodItem> removeItemFromUserShoppingList(String id, String foodItem) {
        User user = getUser(id);
        if (user == null) return new ArrayList<>();
        user.getShoppingList().removeIf(it -> it.getName().equalsIgnoreCase(foodItem));
        repository.save(user);
        return user.getShoppingList();
    }

    public List<FoodItem> deleteUserShoppingList(String id) {
        User user = getUser(id);
        if (user == null) return new ArrayList<>();
        user.getShoppingList().clear();
        repository.save(user);
        return user.getShoppingList();
    }

    public List<FoodItem> addItemsToUserInventory(String name, List<FoodItem> itemList) {
        User user = getUser(name);
        if (user == null) return new ArrayList<>();
        for (FoodItem it : itemList)
            user.getInventory().put(it.getId(), it);
        repository.save(user);
        return user.getInventory().values().stream().toList();
    }

    public List<FoodItem> addItemsToUserShoppingList(String name, List<String> itemList) {
        User user = getUser(name);
        if (user == null) return new ArrayList<>();
        for (String it : itemList) user.getShoppingList().add(new FoodItem(it));
        repository.save(user);
        return user.getShoppingList();
    }

    public List<FoodItem> deleteItemsFromUserShoppingList(String name, List<String> itemList) {
        User user = getUser(name);
        if (user == null) return new ArrayList<>();
        user.getShoppingList().removeIf(foodItem -> itemList.contains(foodItem.getName()));
        repository.save(user);
        return user.getShoppingList();
    }

    public Map<Long, FoodItem> deleteItemsFromUserInventory(String name, List<FoodItem> itemList) {
        User user = getUser(name);
        if (user == null) return new HashMap<>();
        itemList.forEach(it -> user.getInventory().remove(it.getId()));
        repository.save(user);
        return user.getInventory();
    }
}
