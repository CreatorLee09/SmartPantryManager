package com.example.smartpantrymanager;

import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
        faButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // creating an intent to open the AddEditIngredientActivity
                Intent intent = new Intent(MainActivity.this, AddEditIngredientActivity.class);
                startActivity(intent);
            }
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

        // Updates the RecyclerView
        adapter = new PantryAdapter(pantryList);
        recyclerView.setAdapter(adapter);
    }
}