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

        public static DietaryRestrictions fromString(String str) {
            switch (str) {
                case "is_vegan":
                    return VEGAN;
                case "is_vegetarian":
                    return VEGETARIAN;
                case "is_gluten_free":
                    return GLUTEN_FREE;
                case "is_dairy_free":
                    return DAIRY_FREE;
                case "is_nut_free":
                    return NUT_FREE;
                case "is_shellfish_free":
                    return SHELLFISH_FREE;
                case "is_egg_free":
                    return EGG_FREE;
                case "is_soy_free":
                    return SOY_FREE;
                case "is_fish_free":
                    return FISH_FREE;
                case "is_pork_free":
                    return PORK_FREE;
                case "is_red_meat_free":
                    return RED_MEAT_FREE;
                case "is_alcohol_free":
                    return ALCOHOL_FREE;
                case "is_kosher":
                    return KOSHER;
                case "is_halal":
                    return HALAL;
                default:
                    return null;
            }
        }
    }

    public class Page {
        public int page_number;
        public int page_size;
        
        public Page() {
            this(0, 15);
        }

        public Page(int page_number, int page_size) {
            this.page_number = page_number;
            this.page_size = page_size;
        }
    }

    public List<Recipe> SearchByName(String name, Filters filters);
    public List<Recipe> SearchByInventory(Filters filters);
    public List<Recipe> SearchByNameWithInventory(String name, Filters filters);

    public List<Recipe> SearchByName(String name, Filters filters, Page page);
    public List<Recipe> SearchByInventory(Filters filters, Page page);
    public List<Recipe> SearchByNameWithInventory(String name, Filters filters, Page page);
}
