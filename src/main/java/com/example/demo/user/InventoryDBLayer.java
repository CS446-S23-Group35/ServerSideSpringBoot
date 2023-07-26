package com.example.demo.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class InventoryDBLayer {

    @Autowired
    InventoryRepository repository;

    public FoodItem getFoodItem(Long id) {
        return repository.findById(id).orElse(null);
    }

    public FoodItem addItemForUser(String id, FoodItem foodItem) {
        FoodItem item = repository.findById(foodItem.getId()).orElse(null);
        if (item == null)
            item = foodItem;

        foodItem.copy_to(item);
        return repository.save(item);
    }

    public Boolean removeItemForUser(String id, Long foodItemId) {
        FoodItem item = repository.findById(foodItemId).orElse(null);
        if (item == null) return false;

        item.getOwners().remove(id);
        return true;
    }

}