package com.example.demo;

import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.demo.recipe.OpenSearchImpl;
import com.example.demo.recipe.Recipe;
import com.example.demo.recipe.Searcher;

// @SpringBootApplication
public class DemoMain {

	// public static void main(String[] args) {
	// 	SpringApplication.run(DemoMain.class, args);
	// }

	public static void main(String[] args) {
		Searcher searcher = new OpenSearchImpl("localhost");
		List<Recipe> recipes = searcher.SearchByName("test", null);
		System.out.println("Found " + recipes.size() + " recipes");
		for(Recipe recipe : recipes){ 
			System.out.println(recipe.name);
		}
	}

}
