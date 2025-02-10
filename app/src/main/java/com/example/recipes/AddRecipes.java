package com.example.recipes;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddRecipes extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipes);
        EdgeToEdge.enable(this);
        Spinner recipeTypeSpinner = findViewById(R.id.spi);

        // יצירת מתאם (Adapter) לשילוב המידע ב-Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.recipe_types,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // חיבור המתאם ל-Spinner
        recipeTypeSpinner.setAdapter(adapter);

        // האזנה לאירוע בחירה ב-Spinner
        recipeTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedRecipeType = parent.getItemAtPosition(position).toString();
                Toast.makeText(AddRecipes.this, "נבחר סוג מתכון: " + selectedRecipeType, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // פעולה אם לא נבחר כלום
            }
        });
    }
    }