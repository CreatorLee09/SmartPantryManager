package com.example.smartpantrymanager;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PantryAdapter adapter;
    private List<Ingredient> dummyPantryList; // checking to see if it works

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // recycler view from activity_main.xml
        recyclerView = findViewById(R.id.recyclerViewPantry);

        // fake/dummy data for testing purposes
        dummyPantryList = new ArrayList<>();
        dummyPantryList.add(new Ingredient(1, "Tomatoes", 4, "pcs", "2023-11-20"));
        dummyPantryList.add(new Ingredient(2, "Rice", 2, "kg", "2024-05-10"));
        dummyPantryList.add(new Ingredient(3, "Eggs", 6, "pcs", "2023-11-15"));
        dummyPantryList.add(new Ingredient(4, "Milk", 1, "litre", "2023-11-10"));

        // setting up adapter with dummy list
        adapter = new PantryAdapter(dummyPantryList);

        // telling RecyclerView to display items vertically - LinearLayout
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Connecting the adapter to the RecyclerView
        recyclerView.setAdapter(adapter);

    }
}