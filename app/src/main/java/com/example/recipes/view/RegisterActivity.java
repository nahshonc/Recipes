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

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView text;
    private static FirebaseAuth firebaseAuth;
    private EditText username;
    private EditText password;
    private EditText confirmpassword;
    private EditText etEmail;
    private Button but;
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

    private void initViews() {
        text = findViewById(R.id.textView1);
        etEmail = findViewById(R.id.edGmail);
        username = findViewById(R.id.edUsername);
        password = findViewById(R.id.edPassword);
        confirmpassword = findViewById(R.id.edConfirmPassword);
        but = findViewById(R.id.button);
        but.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        if (view == but) {
            String email = etEmail.getText().toString().trim();
            String userInput = username.getText().toString().trim();
            String pass = password.getText().toString();
            String confirmPass = confirmpassword.getText().toString();

            // בדיקת שדות ריקים
            if (email.isEmpty() || userInput.isEmpty() || pass.isEmpty() || confirmPass.isEmpty()) {
                text.setText("אנא מלא את כל השדות");
                return;
            }

            // בדיקת אורך וסיסמה תקינה
            if (pass.length() < 8 ||
                    !pass.matches(".*[A-Z].*") ||
                    !pass.matches(".*[a-z].*") ||
                    !pass.matches(".*[0-9].*") ||
                    !pass.equals(confirmPass)) {
                text.setText("סיסמה לא תקינה או לא תואמת");
                return;
            }

            // אם הכול תקין
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
                        text.setText("אירעה שגיאה: " + task.getException().getMessage());
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }
                }
            });
        }
    }
    }