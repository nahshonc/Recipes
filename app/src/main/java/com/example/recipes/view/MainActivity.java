package com.example.recipes.view;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recipes.R;
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
        intent = new Intent(this, WelcomeActivity.class);
        if (!isConnected()) {
            // אפשר להראות הודעה עם Toast
            Toast.makeText(this, "אין חיבור אינטרנט. יש להתחבר כדי להשתמש באפליקציה.", Toast.LENGTH_LONG).show();

            // אפשר גם למנוע גישה ע"י סיום הפעילות:
            finish();
            return;
        }

        // המשך רגיל אם יש חיבור
        initViews();

    }
    public boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Network network = connectivityManager.getActiveNetwork();
            if (network == null) return false;

            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
            return capabilities != null &&
                    (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));
        } else {
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnected();
        }
    }

    public void initViews(){
        firebaseAuth = FirebaseAuth.getInstance();
        etEmail = findViewById(R.id.username);
        password = findViewById(R.id.enter_password);
        bob = findViewById(R.id.textView3);
        button = findViewById(R.id.btnEnter);
        button.setOnClickListener(this);
        signUp = findViewById(R.id.btnEnter2);
        signUp.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {

        if (view == signUp) {
            Intent go = new Intent(this, RegisterActivity.class);
            startActivity(go);
        } else if (view == button) {
            String email = etEmail.getText().toString().trim();
            String pass = password.getText().toString();

            if (email.isEmpty() || pass.isEmpty()) {
                // הדרך הנכונה: להציג שגיאה
                bob.setText("יש למלא את כל השדות");

                // או: הדרך שאתה רוצה – לרענן את המסך
                Intent refresh = new Intent(MainActivity.this, MainActivity.class);
                finish();
                startActivity(refresh);
                return;
            }

            firebaseAuth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                startActivity(intent);
                            } else {
                                // הדרך הנכונה: להציג שגיאה
                                bob.setText("המייל או הסיסמה שגויים");

                                // או לרענן את המסך

                            }
                        }
                    });
        }
    }


    }

