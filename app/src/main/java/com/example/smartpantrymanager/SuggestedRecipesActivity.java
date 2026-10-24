package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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

        // bottom navigation setup
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setSelectedItemId(R.id.nav_recipes);

        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_pantry)  {
                startActivity(new android.content.Intent(SuggestedRecipesActivity.this, MainActivity.class));
                finish(); // Closes the main (pantry) screen
                return true;
            } else if (itemId == R.id.nav_settings) {
                startActivity(new android.content.Intent(SuggestedRecipesActivity.this, SettingsActivity.class));
                finish(); // Closes the settings screen
                return true;
            }
            return false;
        });

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
            // opens the detail screen and passes the recipe
            Intent intent = new Intent(SuggestedRecipesActivity.this, RecipeDetailActivity.class);
            intent.putExtra("RECIPE_DETAIL", recipe);
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
    }
}