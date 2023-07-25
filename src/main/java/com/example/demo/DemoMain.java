package com.example.demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.demo.recipe.OpenSearchImpl;
import com.example.demo.recipe.Recipe;
import com.example.demo.recipe.Searcher;

@SpringBootApplication
public class DemoMain {

	public static void main(String[] args) {
		SpringApplication.run(DemoMain.class, args);
	}

	// public static void main(String[] args) {
	// 	Searcher.Filters filters =  Searcher.Filters.empty()
	// 	.withExcludedIngredients(
	// 		Arrays.asList("flour", "farro")
	// 	).withInventoryIngredients(
	// 		Arrays.asList("paprika", "vanilla extract", "coffee")
	// 	).withExpiringIngredients(
	// 		Arrays.asList("pork")
	// 	);//.withDietaryRestrictions(
	// 	// 	Arrays.asList(Searcher.DietaryRestrictions.VEGAN)
	// 	// );

	// 	Searcher searcher = new OpenSearchImpl("localhost");
	// 	List<Recipe> recipes = searcher.SearchByInventory(filters);
	// 	System.out.println("Found " + recipes.size() + " recipes");
	// 	for(Recipe recipe : recipes){ 
	// 		System.out.println(recipe.name);
	// 	}
	// }

}
