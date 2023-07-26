package com.example.demo.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class AnalyticsServiceLayer {

    @Autowired
    UserDBLayer userDBLayer;

    @Autowired
    InventoryDBLayer inventoryDBLayer;


    public String getAnalytics(String id) {
        HashMap<Long, FoodItem> inventory=  userDBLayer.getUsersInventory(id);
        return "good user";
    }
}