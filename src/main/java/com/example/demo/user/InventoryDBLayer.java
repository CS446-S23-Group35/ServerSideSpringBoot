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

    public Boolean addItemForUser(String id, FoodItem foodItem) {
        FoodItem item = repository.findById(foodItem.getId()).orElse(null);
        List<String> newOwners;

        if (item == null) {
            item = foodItem;
            newOwners = foodItem.getOwners();
        } else {
            newOwners = item.getOwners();
        }

        newOwners.add(id);
        item.setOwners(newOwners);
        repository.save(item);
        item.copy_to(foodItem); // Update our FoodItem object to match the DB record.

        return true;
    }

    public Boolean removeItemForUser(String id, Long foodItemId) {
        FoodItem item = repository.findById(foodItemId).orElse(null);
        if (item == null) return false;

        item.getOwners().remove(id);
        return true;
    }

}