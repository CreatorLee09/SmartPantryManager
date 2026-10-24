package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SuggestedRecipesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecipeAdapter adapter;
    private List<Recipe> matchingRecipes;
    private TextView tvEmptyState;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_recipes);

        dbHelper = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerViewRecipes);
        tvEmptyState = findViewById(R.id.tvEmptyState); // for if pantry is empty
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadMatchingRecipes();
    }

    private void loadMatchingRecipes() {
        // gets all the recipes and all pantry items
        List<Recipe> allRecipes = dbHelper.getAllRecipes();
        List<Ingredient> pantry = dbHelper.getAllIngredients();

        matchingRecipes = new ArrayList<>();

        // loops through recipes and tests them against the pantry (checking ingredients and pantry)
        for (Recipe recipe : allRecipes) {
            if (RecipeMatcher.canMakeRecipe(recipe, pantry)) {
                matchingRecipes.add(recipe); // then displays the matching recipe
            }
        }

        // shows the empty state message if no recipes match
        if (matchingRecipes.isEmpty()) {
            tvEmptyState.setVisibility(View.VISIBLE); // makes the message TextView available
            recyclerView.setVisibility(View.GONE);
        } else {
            tvEmptyState.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

        // setting up the adapter
        adapter = new RecipeAdapter(matchingRecipes, recipe -> {
            // toasting for now until the recipe detail screen is built
            android.widget.Toast.makeText(SuggestedRecipesActivity.this, "Clicked: " + recipe.getName(), android.widget.Toast.LENGTH_SHORT).show();
        });
        recyclerView.setAdapter(adapter);
    }
}