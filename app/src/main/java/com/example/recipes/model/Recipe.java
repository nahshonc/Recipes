package com.example.recipes.model;

/**
 * מודל לייצוג מתכון באפליקציה.
 * כולל מידע כמו יוצר המתכון, סוג, שם, מרכיבים, עלות, זמן הכנה ותמונה מקודדת כ-Base64.
 */
public class Recipe {

    /** שם המשתמש שיצר את המתכון */
    private String creator;

    /** תמונת המתכון כטקסט Base64 */
    private String imageBase64;

    /** סוג המתכון (למשל: עיקרי, קינוח, טבעוני וכו') */
    private String recipeType;

    /** כותרת המתכון */
    private String title;

    /** רשימת המרכיבים של המתכון */
    private String ingredients;

    /** עלות הכנת המתכון */
    private String cost;

    /** זמן ההכנה של המתכון */
    private String preparationTime;

    /**
     * בנאי ריק נדרש עבור Firebase.
     */
    public Recipe() {
        // חובה עבור Firebase
    }

    /**
     * מחזיר את שם היוצר של המתכון.
     *
     * @return שם היוצר
     */
    public String getcreator() {
        return creator;
    }

    /**
     * מחזיר את תמונת המתכון כ-Base64.
     *
     * @return מחרוזת Base64 של התמונה
     */
    public String getImageBase64() {
        return imageBase64;
    }

    /**
     * מחזיר את סוג המתכון.
     *
     * @return סוג המתכון
     */
    public String getRecipeType() {
        return recipeType;
    }

    /**
     * מחזיר את כותרת המתכון.
     *
     * @return כותרת המתכון
     */
    public String getTitle() {
        return title;
    }

    /**
     * מחזיר את רשימת המרכיבים של המתכון.
     *
     * @return המרכיבים
     */
    public String getIngredients() {
        return ingredients;
    }

    /**
     * מחזיר את עלות הכנת המתכון.
     *
     * @return העלות
     */
    public String getCost() {
        return cost;
    }

    /**
     * מחזיר את זמן ההכנה של המתכון.
     *
     * @return זמן ההכנה
     */
    public String getPreparationTime() {
        return preparationTime;
    }
}