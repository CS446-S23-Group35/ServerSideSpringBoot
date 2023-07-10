package com.example.demo.user;

public class FoodItem {
    private String foodName;
    private String quantity;
    private long timeTillExpiration;

    public FoodItem() {
    }
    public FoodItem(String foodName) {
        this.foodName = foodName;
        this.quantity = "always_in_inventory";
        this.timeTillExpiration = -1;
    }
    public FoodItem(String foodName, String quantity) {
        this.foodName = foodName;
        this.quantity = quantity;
        this.timeTillExpiration = -1;
    }
    public FoodItem(String foodName, String quantity, long timeTillExpiration) {
        this.foodName = foodName;
        this.quantity = quantity;
        this.timeTillExpiration = timeTillExpiration;
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

    public long getTimeTillExpiration() {return timeTillExpiration;}

    public void setTimeTillExpiration(long timeTillExpiration) {this.timeTillExpiration = timeTillExpiration;}
}
