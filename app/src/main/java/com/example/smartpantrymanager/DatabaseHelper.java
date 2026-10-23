package com.example.smartpantrymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;
public class DatabaseHelper extends SQLiteOpenHelper {

    // database info
    private static final String DATABASE_NAME = "PantryManager.db";
    private static final int DATABASE_VERSION = 1;

    // table info
    private static final String TABLE_PANTRY = "pantry_items";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_QUANTITY = "quantity";
    private static final String COLUMN_UNIT = "unit";
    private static final String COLUMN_EXPIRY = "expiry_date";

    // constructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // onCreate runs only ONCE when the app is first installed
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + TABLE_PANTRY + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_QUANTITY + " REAL, " +
                COLUMN_UNIT + " TEXT, " +
                COLUMN_EXPIRY + " TEXT)";
        db.execSQL(createTableStatement);

        // creating a recipe table
        String createRecipeTable = "CREATE TABLE recipes (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "instructions TEXT, " +
                "required_ingredients_json TEXT)"; // stored as a JSON string for simplicity

        db.execSQL(createRecipeTable);

        insertSeedRecipes(db); // adds seed recipes - to test functionality

    }

    // onUpgrade runs if the DATABASE_VERSION ever changes (e.g. when the app is updated in the future)
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PANTRY);
        db.execSQL("DROP TABLE IF EXISTS recipes");
        onCreate(db);
    }

    // CRUD Operations

    // Create - to add a new ingredient
    public boolean addIngredient(Ingredient ingredient) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME, ingredient.getName());
        cv.put(COLUMN_QUANTITY, ingredient.getQuantity());
        cv.put(COLUMN_UNIT, ingredient.getUnit());
        cv.put(COLUMN_EXPIRY, ingredient.getExpiryDate());

        // insert will return the new ID if successful
        long result = db.insert(TABLE_PANTRY, null, cv); // long returns large integer values - if ingredients continue to be added, the id could get larger hence long as opposed to int just to be safe
        return result != -1; // returns -1 if it failed
    }

    // Read - gets all the ingredients
    public List<Ingredient> getAllIngredients() {
        List<Ingredient> returnList = new ArrayList<>();
        String queryString = "SELECT * FROM " + TABLE_PANTRY;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do { // loops through the results
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                double quantity = cursor.getDouble(2);
                String unit = cursor.getString(3);
                String expiry = cursor.getString(4);

                Ingredient newIngredient = new Ingredient(id, name, quantity, unit, expiry);
                returnList.add(newIngredient);
            } while (cursor.moveToNext());
        }

        //  everything is closed to prevent memory leaks
        cursor.close();
        db.close();
        return returnList;
    }

    // Update - modify an existing ingredient
    public boolean updateIngredient(Ingredient ingredient) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME, ingredient.getName());
        cv.put(COLUMN_QUANTITY, ingredient.getQuantity());
        cv.put(COLUMN_UNIT, ingredient.getUnit());
        cv.put(COLUMN_EXPIRY, ingredient.getExpiryDate());

        // Update the row where the ID matches
        int result = db.update(TABLE_PANTRY, cv, COLUMN_ID + " = ?", new String[]{String.valueOf(ingredient.getId())});
        db.close();
        return result > 0; // validation to confirm successful change to database, otherwise will return false
    }

    // Delete - removing an ingredient
    public void deleteIngredient(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PANTRY, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    // insertSeedRecipes method
    private void insertSeedRecipes(SQLiteDatabase db) {
        // Recipe 1: Scrambled eggs - ingredients: 3 eggs and 1 piece of butter
        String recipe1Ingredients = "Eggs:3:pcs,Butter:1:pcs";
        db.execSQL("INSERT INTO recipes (name, instructions, required_ingredients_json) VALUES ('Scrambled Eggs', 'Whisk eggs and cook low heat in butter for about 1 minute or until ready.', '" + recipe1Ingredients + "')");


        // Recipe 2: Rice with tomato - ingredients: 2 Tomatoes and 1 kg of Rice
        String recipe2Ingredients = "Tomatoes:2:pcs,Rice:1:kg";
        db.execSQL("INSERT INTO recipes (name, instructions, required_ingredients_json) VALUES ('Tomato Rice', 'Cook rice in a pot and mix with grated tomatoes.', '" + recipe2Ingredients + "')");
    }

    // method to fetch all recipes from the database
    public List<Recipe> getAllRecipes() {
        List<Recipe> recipeList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM recipes", null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String instructions = cursor.getString(2);
                String reqJson = cursor.getString(3); // ingredient requirements e.g. "Eggs:3:pcs,Butter:1:pcs"

                // parsing the JSON string into a list of RecipeIngredients
                List<RecipeIngredient> reqList = new ArrayList<>();
                String[] items = reqJson.split(",");
                for (String item : items) {
                    String[] stringParts = item.split(":");
                    if(stringParts.length == 3) {
                        reqList.add(new RecipeIngredient(stringParts[0], Double.parseDouble(stringParts[1]), stringParts[2]));
                    }
                }
                recipeList.add(new Recipe(id, name, instructions, reqList));
            } while (cursor.moveToNext());
        }
        cursor.close(); // freeing up resources
        db.close();
        return recipeList;
    }


}
