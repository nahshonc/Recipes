package com.example.recipes;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RecipesByTypeActivity extends ActivityWithMenu {

    private RecyclerView recyclerView;
    private RecipeAdapter adapter;
    private List<Recipe> recipeList = new ArrayList<>();
    private String recipeType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recipes_by_type);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecipeAdapter(this ,recipeList);
        recyclerView.setAdapter(adapter);

        recipeType = getIntent().getStringExtra("title");
        ((TextView) findViewById(R.id.titleView)).setText(recipeType);

        loadRecipesByType(recipeType);
    }

    private void loadRecipesByType(String type) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("recipes");

        dbRef.orderByChild("recipeType").equalTo(type)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        recipeList.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Recipe recipe = dataSnapshot.getValue(Recipe.class);
                            if (recipe != null) {
                                recipeList.add(recipe);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(RecipesByTypeActivity.this, "שגיאה בטעינת מתכונים", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
