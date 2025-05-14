package com.example.recipes;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
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
        tvTitle.setText(intent.getStringExtra("title"));
        tvIngredients.setText("מרכיבים:\n" + intent.getStringExtra("ingredients"));
        tvTime.setText("זמן הכנה: " + intent.getStringExtra("preparationTime"));
        tvCost.setText("עלות: " + intent.getStringExtra("cost"));

        String imageBase64 = intent.getStringExtra("imageBase64");
        if (imageBase64 != null && !imageBase64.isEmpty()) {
            byte[] decodedBytes = Base64.decode(imageBase64, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
            imageView.setImageBitmap(bitmap);
        }
    }
}
