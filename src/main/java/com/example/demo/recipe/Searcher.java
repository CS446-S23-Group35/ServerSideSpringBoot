package com.example.demo.recipe;

import java.util.ArrayList;
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
            return new Filters(
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
            );
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
        VEGAN("is_vegan"),
        VEGETARIAN("is_vegetarian"),
        GLUTEN_FREE("is_gluten_free"),
        DAIRY_FREE("is_dairy_free"),
        NUT_FREE("is_nut_free"),
        SHELLFISH_FREE("is_shellfish_free"),
        EGG_FREE("is_egg_free"),
        SOY_FREE("is_soy_free"),
        FISH_FREE("is_fish_free"),
        PORK_FREE("is_pork_free"),
        RED_MEAT_FREE("is_red_meat_free"),
        ALCOHOL_FREE("is_alcohol_free"),
        KOSHER("is_kosher"),
        HALAL("is_halal");

        private String booleanName;
        private DietaryRestrictions(String booleanName) {
            this.booleanName = booleanName;
        }

        public String getBooleanName() {
            return this.booleanName;
        }
    }

    public List<Recipe> SearchByName(String name, Filters filters);
    public List<Recipe> SearchByInventory(Filters filters);
    public List<Recipe> SearchByNameWithInventory(String name, Filters filters);
}
