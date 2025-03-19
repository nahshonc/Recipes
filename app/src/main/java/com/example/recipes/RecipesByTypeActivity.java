package com.example.recipes;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RecipesByTypeActivity extends ActivityWithMenu {
     TextView tvTitle;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recipes_by_type);
        Intent gotten = this.getIntent();
        String title;
        initViews();
        Bundle extras = gotten.getExtras();
        if(extras == null) {
            title = null;
        } else {
            title= extras.getString("title");
        }


        tvTitle.setText(title);

    }

    private void initViews() {
        tvTitle = findViewById(R.id.titleView);
    }


}