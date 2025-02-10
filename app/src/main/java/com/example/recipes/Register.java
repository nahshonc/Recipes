package com.example.recipes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity implements View.OnClickListener {
    private TextView text;
    private static FirebaseAuth firebaseAuth;
    private EditText username;
    private EditText password;
    private EditText confirmpassword;
    private EditText etEmail;
    private Button but;
    String user;
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

            boolean right = false;
            while (right == false) {
                if (password.getText().toString().length() > 7)
                    if (password.getText().toString().equals(confirmpassword.getText().toString()))
                        if (password.getText().toString().matches(".*[A-Z].*"))
                            if (password.getText().toString().matches(".*[a-z].*"))
                                if (password.getText().toString().matches(".*[0-9].*"))
                                    right = true;
                if (right) {
                    text.setText("good");

                    String email = etEmail.getText().toString();
                    user = username.getText().toString();
                    String pass = password.getText().toString();
                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                startActivity(go);
                            } else {
                                task.getException();
                            }
                        }
                    });


                }

            }
        }
    }
}