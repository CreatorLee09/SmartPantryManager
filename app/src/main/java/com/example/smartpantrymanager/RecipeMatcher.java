package com.example.smartpantrymanager;

import java.util.List;
public class RecipeMatcher {

    // implementing the matching algorithm
    public static boolean canMakeRecipe(Recipe recipe, List<Ingredient> pantry) {

        // loops through every ingredient the recipe requires
        for (RecipeIngredient required : recipe.getRequiredIngredients()){

            boolean foundInPantry = false;

            // looks for this ingredient in the user's pantry
            for (Ingredient pantryItem : pantry) {
                // checks if names match (ignoring upper-, lowercase)
                if (isNameMatch(required.getName(), pantryItem.getName())) {
                    // checks if the pantry has enough quantity for recipes
                    if (pantryItem.getQuantity() >= required.getQuantity()) {
                        foundInPantry = true;
                        break; // Stops searching the pantry for this item when found
                    }
                }
            }

            // The recipe will fail(not show up) if the loop went through the whole pantry and did not find the ingredient or enough quantity for the recipe
            if (!foundInPantry) {
                return false; // ensures one missing ingredient fails the recipe
            }
        }

        return true; // the loop finishes without failing meaning it has found all ingredients
    }

    // this method is to handle plural/singular words to ensure trivial differences(such as 'potato' vs 'potatoes') do not break
    private static boolean isNameMatch(String recipeItem, String pantryItem) {
        String rec = recipeItem.toLowerCase().trim();
        String pan = pantryItem.toLowerCase().trim();

        if (rec.equals(pan)) return true;

        // checks for pluralisation
        if (pan.equals(rec + "s") || pan.equals(rec + "es")) return true;
        if (rec.equals(pan + "s") || rec.equals(pan + "es")) return true;

        return false;
    }

}
