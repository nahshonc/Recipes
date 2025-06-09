package com.example.recipes.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.recipes.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Activity המאפשרת למשתמש להירשם לאפליקציה עם אימייל וסיסמה.
 * מבצעת בדיקות בסיסיות על סיסמה, ושומרת את שם המשתמש ב-SharedPreferences.
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvPasswordInstruction;
    private static FirebaseAuth firebaseAuth;
    private EditText etUsername;
    private EditText etPassword;
    private EditText etConfirmPassword;
    private EditText etEmail;
    private Button btnFinish;
    static String user;
    Intent go;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initViews();
        firebaseAuth = FirebaseAuth.getInstance();
        go = new Intent(this, MainActivity.class);
    }

    /**
     * מאתחל את רכיבי ה-UI וקישורי האירועים.
     */
    private void initViews() {
        tvPasswordInstruction = findViewById(R.id.textView1);
        etEmail = findViewById(R.id.edGmail);
        etUsername = findViewById(R.id.edUsername);
        etPassword = findViewById(R.id.edPassword);
        etConfirmPassword = findViewById(R.id.edConfirmPassword);
        btnFinish = findViewById(R.id.button);
        btnFinish.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if (view == btnFinish) {
            String email = etEmail.getText().toString().trim();
            String userInput = etUsername.getText().toString().trim();
            String pass = etPassword.getText().toString();
            String confirmPass = etConfirmPassword.getText().toString();

            // בדיקת שדות ריקים
            if (email.isEmpty() || userInput.isEmpty() || pass.isEmpty() || confirmPass.isEmpty()) {
                tvPasswordInstruction.setText("אנא מלא את כל השדות");
                return;
            }

            // בדיקת תקינות סיסמה (לפחות 8 תווים, אות גדולה, אות קטנה, ספרה, וסיסמה תואמת)
            if (pass.length() < 8 ||
                    !pass.matches(".*[A-Z].*") ||
                    !pass.matches(".*[a-z].*") ||
                    !pass.matches(".*[0-9].*") ||
                    !pass.equals(confirmPass)) {
                tvPasswordInstruction.setText("סיסמה לא תקינה או לא תואמת");
                return;
            }

            // יצירת משתמש ב-Firebase
            firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        SharedPreferences sharedPref = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("username", userInput);
                        editor.apply();

                        startActivity(go);
                    } else {
                        tvPasswordInstruction.setText("אירעה שגיאה: " + task.getException().getMessage());
                        // רענון המסך במקרה של שגיאה
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }
                }
            });
        }
    }
}
