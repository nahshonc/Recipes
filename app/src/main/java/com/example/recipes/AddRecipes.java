package com.example.recipes;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AddRecipes extends ActivityWithMenu {
    private Uri imageUri;
    private ImageView imageButton;
    private Spinner recipeTypeSpinner;
    private String selectedRecipeType = "";
    private Bitmap selectedBitmap;
    private EditText edtName, edtMoney, edtTime, edtMaking;

    private final ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri selectedImageUri = result.getData().getData();
                    imageUri = selectedImageUri;
                    try {
                        selectedBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                        imageButton.setImageBitmap(selectedBitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    private final ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Bitmap photo = (Bitmap) result.getData().getExtras().get("data");
                    if (photo != null) {
                        selectedBitmap = photo;
                        imageButton.setImageBitmap(photo);
                    }
                }
            });

    private final ActivityResultLauncher<String[]> permissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestMultiplePermissions(),
            result -> {
                boolean allGranted = true;
                for (Boolean granted : result.values()) {
                    if (!granted) {
                        allGranted = false;
                        break;
                    }
                }
                if (!allGranted) {
                    Toast.makeText(this, "Permissions required!", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipes);

        imageButton = findViewById(R.id.imageView);
        recipeTypeSpinner = findViewById(R.id.spi);

        imageButton.setOnClickListener(v -> openImageChooser());
        initSpinner();
        requestPermissionsIfNeeded();

        findViewById(R.id.btnSave).setOnClickListener(v -> saveRecipeToFirebase());
    }

    private void initSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.recipe_types, android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        recipeTypeSpinner.setAdapter(adapter);

        recipeTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedRecipeType = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void openImageChooser() {
        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Intent chooser = Intent.createChooser(pickIntent, "Select Image");
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{cameraIntent});

        galleryLauncher.launch(chooser);
    }

    private void requestPermissionsIfNeeded() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionLauncher.launch(new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            });
        }
    }

    private void saveRecipeToFirebase() {
        if (selectedBitmap == null || selectedRecipeType.isEmpty()) {
            Toast.makeText(this, "Please select an image and a recipe type", Toast.LENGTH_SHORT).show();
            return;
        }

        // ממירים את התמונה ל-Base64
        String imageBase64 = bitmapToBase64(selectedBitmap);

        saveToRealtimeDatabase(imageBase64, selectedRecipeType);
    }

    private void saveToRealtimeDatabase(String imageBase64, String recipeType) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("recipes");

        edtMaking = findViewById(R.id.edtIngredients);
        edtMoney = findViewById(R.id.edtMoney);
        edtName = findViewById(R.id.edtName);
        edtTime = findViewById(R.id.edtTime);
        Map<String, Object> recipe = new HashMap<>();
        recipe.put("imageBase64", imageBase64);
        recipe.put("recipeType", recipeType);

        recipe.put("title", edtName.getText().toString());

        recipe.put("ingredients", edtMaking.getText().toString());
        recipe.put("cost", edtMoney.getText().toString());
        recipe.put("preparationTime",edtTime.getText().toString());
        dbRef.push().setValue(recipe)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(AddRecipes.this, "Recipe saved to Realtime DB!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AddRecipes.this, "Failed to save recipe: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("AddRecipes", "Error saving to Realtime Database", e);
                });
    }

    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] bytes = baos.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }
}
