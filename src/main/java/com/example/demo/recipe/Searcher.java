package com.example.demo.recipe;

import java.util.List;

import ch.qos.logback.core.filter.Filter;

// Searcher defines the interface for the ways we can search for recipes.
public interface Searcher {
    public class Filters {
        public List<String> excludedIngredients;
        public List<String> inventoryIngredients;
        public List<String> expiringIngedients;
        public List<DietaryRestrictions> dietaryRestrictions;

        public Filters() {
            this(null, null, null, null);
        }

        public Filters(
            List<String> excludedIngredients,
            List<String> inventoryIngredients,
            List<String> expiringIngedients,
            List<DietaryRestrictions> dietaryRestrictions
        ) {
            this.excludedIngredients = excludedIngredients;
            this.inventoryIngredients = inventoryIngredients;
            this.expiringIngedients = expiringIngedients;
            this.dietaryRestrictions = dietaryRestrictions;
        }

        public static Filters empty() {
            return new Filters(null, null, null, null);
        }

        public Filters withExcludedIngredients(List<String> excludedIngredients) {
            return new Filters(excludedIngredients, this.inventoryIngredients, this.expiringIngedients, this.dietaryRestrictions);
        }

        public Filters withInventoryIngredients(List<String> inventoryIngredients) {
            return new Filters(this.excludedIngredients, inventoryIngredients, this.expiringIngedients, this.dietaryRestrictions);
        }

        public Filters withExpiringIngredients(List<String> expiringIngedients) {
            return new Filters(this.excludedIngredients, this.inventoryIngredients, expiringIngedients, this.dietaryRestrictions);
        }

        public Filters withDietaryRestrictions(List<DietaryRestrictions> dietaryRestrictions) {
            return new Filters(this.excludedIngredients, this.inventoryIngredients, this.expiringIngedients, dietaryRestrictions);
        }
    }

    public enum DietaryRestrictions {
        VEGAN,
        VEGETARIAN,
        GLUTEN_FREE,
        DAIRY_FREE,
        NUT_FREE,
        SHELLFISH_FREE,
        EGG_FREE,
        SOY_FREE,
        FISH_FREE,
        PORK_FREE,
        RED_MEAT_FREE,
        ALCOHOL_FREE,
        KOSHER,
        HALAL
    }

    public List<Recipe> SearchByName(String name, Filters filters);
    public List<Recipe> SearchByInventory(Filters filters);
}
