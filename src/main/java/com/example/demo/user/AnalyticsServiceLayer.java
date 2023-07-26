package com.example.demo.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Service
public class AnalyticsServiceLayer {

    @Autowired
    UserDBLayer userDBLayer;

    @Autowired
    InventoryDBLayer inventoryDBLayer;


    // queries the repo layer to retrieve appropriate into
    public int getAnalytics(String id) {
        HashMap<Long, FoodItem> inventory=  userDBLayer.getUsersInventory(id);

        Iterator inventoryIterator = inventory.entrySet().iterator();

        double expiredItems=0;
        double totalItems=0;

        while(inventoryIterator.hasNext()){
            Map.Entry myEntry = (Map.Entry)inventoryIterator.next();
            FoodItem foodItem = (FoodItem) myEntry.getValue();
            java.sql.Date curDate = new java.sql.Date(new java.util.Date().getTime());

            if(foodItem.getExpiryDate().after(curDate)){
                expiredItems++;
            }
            totalItems++;
        }
        double expiredItemsRatio = expiredItems/totalItems;
        int percentageWasted = (int)expiredItemsRatio*100;
        int score = 0;
        if(percentageWasted<5){
            score=6;
        }
        else if(percentageWasted<15){
            score=5;
        }
        else if(percentageWasted<30){
            score=4;
        }
        else if(percentageWasted<60){
            score=3;
        }
        else if(percentageWasted<80){
            score=2;
        }
        else{
            score=1;
        }

        return score;
    }
}