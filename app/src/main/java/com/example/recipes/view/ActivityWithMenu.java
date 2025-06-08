package com.example.recipes.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.recipes.R;

/**
 * Activity בסיסית שמכילה תפריט עליון (Menu).
 * מטפלת בלחיצות בתפריט לניווט בין מסכים שונים באפליקציית המתכונים.
 */
public class ActivityWithMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // אין תוכן נוסף בפעולה זו
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.ilovecooking, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent recipesByTypeIntent = new Intent(this, RecipesByTypeActivity.class);
        Intent addRecipIntent = new Intent(this, AddRecipesActivity.class);
        String title = item.getTitle().toString();

        if (title.equals(getString(R.string.addRecipes)))
            startActivity(addRecipIntent);
        else {
            recipesByTypeIntent.putExtra("title", title);
            startActivity(recipesByTypeIntent);
        }

        return super.onOptionsItemSelected(item);
    }
}