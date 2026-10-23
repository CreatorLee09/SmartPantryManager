package com.example.smartpantrymanager;

import android.os.Bundle;

import android.app.DatePickerDialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import java.util.Calendar;

public class AddEditIngredientActivity extends AppCompatActivity{

    private TextInputEditText etName, etQuantity;
    private Spinner spinnerUnit;
    private Button btnSelectExpiryDate, btnSaveIngredient;
    private String selectedExpiryDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_ingredient);

        // linking the java variables to the XML views
        etName = findViewById(R.id.etIngredientName);
        etQuantity = findViewById(R.id.etIngredientQuantity);
        spinnerUnit = findViewById(R.id.spinnerUnit);
        btnSelectExpiryDate = findViewById(R.id.btnSelectExpiryDate);
        btnSaveIngredient = findViewById(R.id.btnSaveIngredient);

        // setting up the spinner as a unit dropdown
        String[] units = {"pc(s)", "Kg(s)", "g(s)", "litre(s)", "ml(s)", "bunch(es)", "packet(s)"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, units);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUnit.setAdapter(adapter);

        // the date picker for expiry
        btnSelectExpiryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // displaying the current date in the picker by default
                final Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                // showing the DatePicker dialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        AddEditIngredientActivity.this,
                        (view, year1, monthOfYear, dayOfMonth) -> {
                            // when a date is picked, it will save to the variable selectedExpiryDate and the button will be updated to reflect the selected date
                            monthOfYear = monthOfYear + 1; // in java months start at 0. Adding 1 reads Jan as month 1 as humans read it. Ensures the correct month
                            selectedExpiryDate = year1 + "-" + String.format("%02d", monthOfYear) +
                                    "-" + String.format("%02d", dayOfMonth);
                            btnSelectExpiryDate.setText("Expires: " + selectedExpiryDate);

                        },
                        year, month, day);

                // prevent picking a date in the past
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000); // currentTimeMillis() gets the current time in milliseconds
                // -1000 removes a second to account for delay in code running
                datePickerDialog.show();
            }
        });

        // handling the save button
        btnSaveIngredient.setOnClickListener(v -> {
            // gets the text the user typed
            String name = etName.getText().toString().trim();
            String quantityStr = etQuantity.getText().toString().trim();
            String unit = spinnerUnit.getSelectedItem().toString();

            // input validation
            if (name.isEmpty()) {
                etName.setError("Name of ingredient is required");
                etName.requestFocus(); // shows where the error happened
                return;
            }
            if (quantityStr.isEmpty()) {
                etQuantity.setError("Quantity is required");
                etQuantity.requestFocus();
                return;
            }

            // convert the quantity to double e.g. 3.5 kg
            double quantity = Double.parseDouble(quantityStr);

            // Save to database
            DatabaseHelper dbHelper = new DatabaseHelper(AddEditIngredientActivity.this);
            Ingredient newIngredient = new Ingredient(0, name, quantity, unit, selectedExpiryDate);

            boolean success = dbHelper.addIngredient(newIngredient);

            if (success) { // toast - small temporary popup message that alerts the user e.g. ingredient saved when saved to database
                Toast.makeText(AddEditIngredientActivity.this, "Ingredient Saved!", Toast.LENGTH_SHORT).show();
                finish(); // closes the screen and goes back to MainActivity
            } else {
                Toast.makeText(AddEditIngredientActivity.this, "Error saving ingredient", Toast.LENGTH_SHORT).show();
            }
        });
    }
}