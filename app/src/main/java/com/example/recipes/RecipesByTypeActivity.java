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
     private String name;
     private String time;
     private int cost;

    public RecipesByTypeActivity(String name, String time, int cost) {
        this.time = time;
        this.name = name;
        this.cost = cost;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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