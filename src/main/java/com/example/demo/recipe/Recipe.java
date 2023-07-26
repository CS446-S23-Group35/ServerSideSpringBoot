package com.example.demo.recipe;

// Recipe is a data class containing all the information about a recipe.
public class Recipe {
    public static class Ingredient {
        public String name;
        public IngredientAmount amount;
        public String notes;
        public boolean optional;
    }

    public static class IngredientAmount {
        public int type;
        public String typeName;
        public double value;
    }

    public static class Metadata {
        public String[] tags;
        public int minutes_to_prep;
        public int minutes_to_cook;
        public int minutes_total;
        public int difficulty;
        public ServingRange servings;
        public int estimated_calories;
        public String image_url;
        public String image_alt;
        public String source_url;
        public DietartyMetadata dietary;
    }
    public static class DietartyMetadata {
        public boolean is_vegetarian;
        public boolean is_vegan;
        public boolean is_gluten_free;
        public boolean is_dairy_free;
        public boolean is_nut_free;
        public boolean is_shellfish_free;
        public boolean is_egg_free;
        public boolean is_soy_free;
        public boolean is_fish_free;
        public boolean is_pork_free;
        public boolean is_red_meat_free;
        public boolean is_alcohol_free;
        public boolean is_kosher;
        public boolean is_halal;
    }

    public static class ServingRange {
        public int min;
        public int max;
        public String alternative;
    }

    public String name;
    public String description;
    public Ingredient[] ingredients;
    public String[] steps;
    public Metadata metadata;
}
