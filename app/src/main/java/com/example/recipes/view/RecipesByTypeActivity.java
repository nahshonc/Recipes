package com.example.recipes.view;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipes.R;
import com.example.recipes.controller.RecipeAdapter;
import com.example.recipes.model.Recipe;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity המציגה רשימת מתכונים מסוג מסוים (לפי קטגוריה),
 * ומטעינה אותם מבסיס הנתונים Firebase.
 */
public class RecipesByTypeActivity extends ActivityWithMenu {

    private RecyclerView recyclerView;
    private RecipeAdapter adapter;
    private List<Recipe> recipeList = new ArrayList<>();
    private String recipeType;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recipes_by_type);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecipeAdapter(this, recipeList);
        recyclerView.setAdapter(adapter);
        progressBar = findViewById(R.id.progressBar);

        recipeType = getIntent().getStringExtra("title");
        ((TextView) findViewById(R.id.titleView)).setText(recipeType);

        loadRecipesByType(recipeType);
    }

    /**
     * טוען את רשימת המתכונים לפי סוג המתכון המבוקש מבסיס הנתונים,
     * ומעדכן את ממשק המשתמש.
     *
     * @param type סוג המתכון לטעינה (למשל: "עוגות", "מרקים" וכו')
     */
    private void loadRecipesByType(String type) {
        progressBar.setVisibility(View.VISIBLE); // הצגת הספינר בעת הטעינה

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
                        progressBar.setVisibility(View.GONE); // הסתרת הספינר בסיום הטעינה
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(RecipesByTypeActivity.this, "שגיאה בטעינת מתכונים", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE); // הסתרת הספינר גם במקרה של שגיאה
                    }
                });
    }
}
