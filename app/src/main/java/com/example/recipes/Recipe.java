package com.example.recipes;

public class Recipe {
    private String creator;
    private String imageBase64;
    private String recipeType;
    private String title;
    private String ingredients;
    private String cost;
    private String preparationTime;

    public Recipe() {
        // חובה עבור Firebase
    }

    public String getcreator() {
        return creator;
    }
    public String getImageBase64() {
        return imageBase64;
    }

    public String getRecipeType() {
        return recipeType;
    }

    public String getTitle() {
        return title;
    }

    public String getIngredients() {
        return ingredients;
    }

    public String getCost() {
        return cost;
    }

    public String getPreparationTime() {
        return preparationTime;
    }
}
