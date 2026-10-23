package com.example.smartpantrymanager;

import java.util.List;

public class Recipe implements java.io.Serializable {
    private int id;
    private String name;
    private String instructions;
    private List<RecipeIngredient> requiredIngredients; // will be required for recipes

    public Recipe(int id, String name, String instructions, List<RecipeIngredient> requiredIngredients) {
        this.id = id;
        this.name = name;
        this.instructions = instructions;
        this.requiredIngredients = requiredIngredients;
    }

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getInstructions() {
        return instructions;
    }
    public List<RecipeIngredient> getRequiredIngredients() {
        return requiredIngredients;
    }

}
