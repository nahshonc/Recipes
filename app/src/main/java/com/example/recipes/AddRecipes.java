package com.example.recipes;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.Manifest;


import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddRecipes extends ActivityWithMenu {
    private Uri imageUri;

    private ImageView imageButton;
    private Spinner recipeTypeSpinner;
    // Activity Result Launcher for Gallery
    private final ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri selectedImageUri = result.getData().getData();
                    imageButton.setImageURI(selectedImageUri);
                }
            });

    private final ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri selectedImageUri = result.getData().getData();
                    if (selectedImageUri != null) {
                        imageUri = selectedImageUri; // ✅ Save URI
                        imageButton.setImageURI(imageUri); // ✅ Set ImageView
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
                if (allGranted) {
                    Toast.makeText(this, "Permissions granted!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permissions denied!", Toast.LENGTH_SHORT).show();
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipes);
        EdgeToEdge.enable(this);
        initImageView();
        initSpinner();
        requestPermissionsIfNeeded(); // ✅ Ask for permissions at runtime


    }

    private void initSpinner() {
        recipeTypeSpinner = findViewById(R.id.spi);

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

    private void initImageView() {
        imageButton = findViewById(R.id.imageView);
        imageButton.setOnClickListener(v -> openImageChooser());

        // Check and request permissions (For Android 6.0+)
    }

    private void openImageChooser() {
        // Show chooser dialog for Gallery or Camera
        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        Intent chooser = Intent.createChooser(pickIntent, "Select Image");
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{cameraIntent});

        galleryLauncher.launch(chooser);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 200) { // ✅ If from gallery
                Uri selectedImageUri = data.getData();
                if (selectedImageUri != null) {
                    imageUri = selectedImageUri;
                    imageButton.setImageURI(imageUri);
                }
            } else if (requestCode == 201) { // ✅ If from camera
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                if (photo != null) {
                    imageButton.setImageBitmap(photo);
                }
            }
        }
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
}