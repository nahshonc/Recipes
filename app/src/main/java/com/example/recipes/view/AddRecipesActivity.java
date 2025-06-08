package com.example.recipes.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import androidx.core.content.FileProvider;

import com.example.recipes.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * פעילות המאפשרת למשתמש להוסיף מתכון חדש.
 * הפעילות כוללת בחירת תמונה מהגלריה או צילום,
 * הזנת פרטי המתכון, ושמירתם למסד הנתונים של Firebase.
 */
public class AddRecipesActivity extends ActivityWithMenu {

    private Uri imageUri;
    private ImageView ivAddPicture;
    private Spinner recipeTypeSpinner;
    private String selectedRecipeType = "";
    private Bitmap selectedBitmap;
    private EditText edtName, edtMoney, edtTime, edtMaking;
    private Uri cameraImageUri;

    private final ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    try {
                        if (result.getData() != null && result.getData().getData() != null) {
                            imageUri = result.getData().getData();
                        } else {
                            imageUri = cameraImageUri;
                        }

                        selectedBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                        ivAddPicture.setImageBitmap(selectedBitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "שגיאה בטעינת התמונה", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    private final ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    try {
                        selectedBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), cameraImageUri);
                        imageUri = cameraImageUri;
                        ivAddPicture.setImageBitmap(selectedBitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Failed to load camera image", Toast.LENGTH_SHORT).show();
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

    /**
     * פעולה שמתבצעת כאשר האקטיביטי נוצר.
     * מאתחלת רכיבים, מאזינים, מבקשת הרשאות ומטפלת בשחזור תמונה מצולמת.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipes);

        if (savedInstanceState != null) {
            String uriString = savedInstanceState.getString("cameraImageUri");
            if (uriString != null) {
                cameraImageUri = Uri.parse(uriString);
            }
        }

        ivAddPicture = findViewById(R.id.imageView);
        recipeTypeSpinner = findViewById(R.id.spi);

        ivAddPicture.setOnClickListener(v -> openImageChooser());
        initSpinner();
        requestPermissionsIfNeeded();

        findViewById(R.id.btnSave).setOnClickListener(v -> saveRecipeToFirebase());
    }

    /**
     * שומר את כתובת התמונה שצולמה בעת שינוי מצב המסך.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (cameraImageUri != null) {
            outState.putString("cameraImageUri", cameraImageUri.toString());
        }
    }

    /**
     * משחזר את כתובת התמונה לאחר סיבוב מסך או שחזור פעילות.
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String uriString = savedInstanceState.getString("cameraImageUri");
        if (uriString != null) {
            cameraImageUri = Uri.parse(uriString);
        }
    }

    /**
     * יוצר קובץ תמונה זמני ומחזיר את ה-Uri שלו.
     *
     * @return כתובת של קובץ תמונה
     */
    private Uri createImageFileUri() {
        File imageFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "photo_" + System.currentTimeMillis() + ".jpg");
        return FileProvider.getUriForFile(this, getPackageName() + ".provider", imageFile);
    }

    /**
     * מאתחל את הספינר עם סוגי המתכונים האפשריים.
     */
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

    /**
     * פותח חלון לבחירת תמונה מהגלריה או צילום.
     */
    private void openImageChooser() {
        cameraImageUri = createImageFileUri();

        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraImageUri);
        cameraIntent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);

        Intent chooser = Intent.createChooser(pickIntent, "בחר תמונה או צלם");
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{cameraIntent});

        galleryLauncher.launch(chooser);
    }

    /**
     * פותח את המצלמה לצילום תמונה.
     */
    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraImageUri = createImageFileUri();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraImageUri);
        cameraLauncher.launch(intent);
    }

    /**
     * מבקש הרשאות גישה למצלמה וגלריה אם נדרש.
     */
    private void requestPermissionsIfNeeded() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionLauncher.launch(new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            });
        }
    }

    /**
     * שומר את פרטי המתכון והתמונה במסד הנתונים של Firebase.
     */
    private void saveRecipeToFirebase() {
        if (selectedBitmap == null || selectedRecipeType.isEmpty()) {
            Toast.makeText(this, "Please select an image and a recipe type", Toast.LENGTH_SHORT).show();
            return;
        }

        String imageBase64 = bitmapToBase64(selectedBitmap);
        saveToRealtimeDatabase(imageBase64, selectedRecipeType);
    }

    /**
     * מבצע את השמירה בפועל ל-Firebase Realtime Database.
     *
     * @param imageBase64 תמונה בפורמט Base64
     * @param recipeType  סוג המתכון
     */
    private void saveToRealtimeDatabase(String imageBase64, String recipeType) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("recipes");

        edtMaking = findViewById(R.id.edtIngredients);
        edtMoney = findViewById(R.id.edtMoney);
        edtName = findViewById(R.id.edtName);
        edtTime = findViewById(R.id.edtTime);

        Map<String, Object> recipe = new HashMap<>();
        SharedPreferences sharedPref = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        String username = sharedPref.getString("username", "UnknownUser");

        recipe.put("imageBase64", imageBase64);
        recipe.put("recipeType", recipeType);
        recipe.put("creator", RegisterActivity.user);
        recipe.put("title", edtName.getText().toString());
        recipe.put("ingredients", edtMaking.getText().toString());
        recipe.put("cost", edtMoney.getText().toString());
        recipe.put("preparationTime", edtTime.getText().toString());

        dbRef.push().setValue(recipe)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(AddRecipesActivity.this, "Recipe saved to Realtime DB!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AddRecipesActivity.this, "Failed to save recipe: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("AddRecipes", "Error saving to Realtime Database", e);
                });
    }

    /**
     * ממיר אובייקט Bitmap למחרוזת Base64.
     *
     * @param bitmap תמונה מסוג Bitmap
     * @return מחרוזת Base64 של התמונה
     */
    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] bytes = baos.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }
}
