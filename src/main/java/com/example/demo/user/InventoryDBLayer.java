package com.example.demo.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class InventoryDBLayer {

    @Autowired
    InventoryRepository repository;

    public FoodItem getFoodItem(Long id) {
        return repository.findById(id).orElse(null);
    }

    public FoodItem addItemForUser(String id, FoodItem foodItem) {
        return repository.save(foodItem);
    }

    public Boolean removeItemForUser(String id, Long foodItemId) {
        FoodItem item = repository.findById(foodItemId).orElse(null);
        if (item == null) return false;

        item.getOwners().remove(id);
        return true;
    }

    public List<FoodItem> addItemsForUser(String name, List<FoodItem> itemList) {
        List<FoodItem> ret = new ArrayList<>();
        for (FoodItem it : itemList)
            ret.add(repository.save(it));
        return ret;
    }
}