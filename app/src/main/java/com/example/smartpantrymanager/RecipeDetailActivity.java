package com.example.smartpantrymanager;

import android.os.Bundle;

import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class RecipeDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        TextView tvName = findViewById(R.id.tvDetailRecipeName);
        TextView tvIngredients = findViewById(R.id.tvDetailIngredients);
        TextView tvInstructions = findViewById(R.id.tvDetailInstructions);

        // receives the recipes object from Intent
        Recipe recipe = (Recipe) getIntent().getSerializableExtra("RECIPE_DETAIL");

        if (recipe != null) {
            tvName.setText(recipe.getName());
            tvInstructions.setText(recipe.getInstructions());

            // building the ingredient string
            StringBuilder sb = new StringBuilder();
            for (RecipeIngredient rIngredient : recipe.getRequiredIngredients()) {
                sb.append("• ").append(rIngredient.getQuantity()).append(" ").append(rIngredient.getUnit()).append(" ").append(rIngredient.getName()).append("\n");
            }
            tvIngredients.setText(sb.toString());
        }
    }

}