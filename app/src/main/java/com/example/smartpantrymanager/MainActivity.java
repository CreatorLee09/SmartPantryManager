package com.example.smartpantrymanager;

import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AlertDialog.Builder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PantryAdapter adapter;
    private List<Ingredient> pantryList;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerViewPantry);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Setting up the floating Action Button
        FloatingActionButton faButton = findViewById(R.id.fabAddIngredient);
        faButton.setOnClickListener(v -> {
            // creating an intent to open the AddEditIngredientActivity
            Intent intent = new Intent(MainActivity.this, AddEditIngredientActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // method will run every time the screen becomes visible e.g. when returning from the Add screen
        loadPantryData();
    }

    private void loadPantryData() { // fetches the real-time data from the database
        pantryList = dbHelper.getAllIngredients();

        // Updates the RecyclerView. A listener is now passed to handle clicks
        adapter = new PantryAdapter(pantryList, new PantryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Ingredient ingredient) {
                // opens the AddEdit screen, but passes the ingredient with it
                Intent intent = new Intent(MainActivity.this, AddEditIngredientActivity.class);
                intent.putExtra("EDIT_INGREDIENT", ingredient);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(Ingredient ingredient) {
                // shows a confirmation dialog to confirm before deleting
                new Builder(MainActivity.this) // initialising dialog and attaching to main activity
                        .setTitle("Delete Ingredient")
                        .setMessage("Are you sure you want to delete " + ingredient.getName() + "?")
                        .setPositiveButton("Delete", (dialog, which) -> {
                            // delete the record from the database
                            dbHelper.deleteIngredient(ingredient.getId());

                            // refreshes the list
                            loadPantryData();
                            Toast.makeText(MainActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });
        recyclerView.setAdapter(adapter);
    }
}