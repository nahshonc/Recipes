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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText etEmail, password;
    TextView bob;
    Button button, signUp;
    FirebaseAuth firebaseAuth;
    Intent  intent ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intent = new Intent(this, AddRecipes.class);
        initViews();

    }

    public void initViews(){
        firebaseAuth = FirebaseAuth.getInstance();
        etEmail = findViewById(R.id.username);
        password = findViewById(R.id.enter_password);
        bob = findViewById(R.id.bob);
        button = findViewById(R.id.btnEnter);
        button.setOnClickListener(this);
        signUp = findViewById(R.id.btnEnter2);
        signUp.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {

        if(view == signUp) {
            Intent go = new Intent(this,Register.class);
            startActivity(go);
        }
        else if(view == button){
            String email= etEmail.getText().toString();    
            String pass = password.getText().toString();    
            firebaseAuth.signInWithEmailAndPassword(email,pass) .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {                
                @Override                
                public void onComplete(Task<AuthResult> task) {                    
                    if(task.isSuccessful()) {

                        startActivity(intent);
                    }
                    else                        
                        task.getException();               
                }            
            });

               


            }
        }

    }

