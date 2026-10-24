package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.content.SharedPreferences;
import android.widget.Toast;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.materialswitch.MaterialSwitch;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // bottom navigation setup
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setSelectedItemId(R.id.nav_settings);

        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_pantry) {
                startActivity(new android.content.Intent(SettingsActivity.this, MainActivity.class));
                finish(); // closes main (pantry) screen
                return true;
            } else if (itemId == R.id.nav_recipes) {
                startActivity(new android.content.Intent(SettingsActivity.this, SuggestedRecipesActivity.class));
                finish(); // closes recipes screen
                return true;
            }
            return false;
        });


        MaterialSwitch switchExpiringSoon = findViewById(R.id.switchExpiringSoon);
        Spinner spinnerDefaultUnit = findViewById(R.id.spinnerDefaultUnit);

        // setting the spinner for default units
        String[] units = {"pcs", "kg", "g", "litre", "ml", "bunch", "packet"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, units);
        spinnerDefaultUnit.setAdapter(adapter);

        // loading saved preferences
        SharedPreferences prefs = getSharedPreferences("SmartPantryPrefs", MODE_PRIVATE);
        boolean alertsEnabled = prefs.getBoolean("alerts_enabled", true);
        switchExpiringSoon.setChecked(alertsEnabled);

        // listening for changes on the switch (toggle on or off)
        switchExpiringSoon.setOnCheckedChangeListener(((buttonView, isChecked) -> {

            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("alerts_enabled", isChecked);
            editor.apply();

            Toast.makeText(SettingsActivity.this, isChecked ? "Expiry alerts turned ON" : "Expiry alerts turned OFF", Toast.LENGTH_SHORT).show();

        }));
    }
}