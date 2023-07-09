package com.example.demo.user;

public class FoodItem {
    private String foodName;
    private String quantity;

    public FoodItem(String foodName, String quantity) {
        this.foodName = foodName;
        this.quantity = quantity;
    }
    public FoodItem() {
    }

    public FoodItem(String foodName) {
        this.foodName = foodName;
        this.quantity = "always_in_inventory";
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
