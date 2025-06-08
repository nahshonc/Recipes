package com.example.recipes.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.recipes.R;
import com.example.recipes.controller.RecipeAdapter;
import com.example.recipes.model.Recipe;

/**
 * פעילות המציגה את פרטי המתכון הנבחר, כולל תמונה, שם, מרכיבים, זמן הכנה ועלות.
 */
public class RecipeDetailsActivity extends AppCompatActivity {

    ImageView imageView;
    TextView tvTitle, tvIngredients, tvTime, tvCost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        imageView = findViewById(R.id.ivImage);
        tvTitle = findViewById(R.id.tvTitle);
        tvIngredients = findViewById(R.id.tvIngredients);
        tvTime = findViewById(R.id.tvTime);
        tvCost = findViewById(R.id.tvCost);

        Intent intent = getIntent();
        int position = intent.getIntExtra("position", -1);
        if (position != -1) {
            Recipe recipe = RecipeAdapter.recipeList.get(position);
            tvTitle.setText(recipe.getTitle());
            tvIngredients.setText("מרכיבים:\n" + recipe.getIngredients());
            tvTime.setText("זמן הכנה: " + recipe.getPreparationTime());
            tvCost.setText("עלות: " + recipe.getCost());
            String imageBase64 = recipe.getImageBase64();

            if (imageBase64 != null && !imageBase64.isEmpty()) {
                byte[] decodedBytes = Base64.decode(imageBase64, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                imageView.setImageBitmap(bitmap);
            }
        }
    }
}