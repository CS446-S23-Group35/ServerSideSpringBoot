package com.example.demo.user;

import java.sql.Date;
import java.util.HashSet;

public interface FoodItem{
    Long getId();
    HashSet<String> getOwners();
    void setExpiryDate(Date expiry_date);

    void setOwners(HashSet<String> newOwners);

    void copy_to(FoodItem foodItem);

    void setId(Long id);

    void setName(String name);

    void setAmount(Number amount);

    void setAmountUnit(String amount_unit);

    void setPurchaseDate(Date purchase_date);

    void setIsExpiryEstimated(Boolean is_expiry_estimated);

    void setSoftDelete(Boolean soft_delete);

    boolean validateFoodItem();
}
