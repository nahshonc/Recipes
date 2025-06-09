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

/**
 * מסך הכניסה הראשי של האפליקציה.
 * מאפשר התחברות עם אימייל וסיסמה, או מעבר למסך רישום.
 * מבצע בדיקת חיבור לאינטרנט ומשתמש ב-Firebase לאימות משתמשים.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etEmail, etPassword;
    TextView tvInstruction;
    Button btSignIn, btSignUp;
    FirebaseAuth firebaseAuth;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intent = new Intent(this, WelcomeActivity.class);

        if (!isConnected()) {
            Toast.makeText(this, "אין חיבור אינטרנט. יש להתחבר כדי להשתמש באפליקציה.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        initViews();
    }

    /**
     * בודק האם המכשיר מחובר לאינטרנט (WiFi, סלולר או Ethernet).
     *
     * @return true אם יש חיבור אינטרנט, אחרת false
     */
    public boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) return false;

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

    /**
     * מאתחל את כל הרכיבים הגרפיים במסך ואת Firebase Authentication.
     */
    public void initViews() {
        firebaseAuth = FirebaseAuth.getInstance();
        etEmail = findViewById(R.id.username);
        etPassword = findViewById(R.id.enter_password);
        tvInstruction = findViewById(R.id.textView3);
        btSignIn = findViewById(R.id.btnEnter);
        btSignUp = findViewById(R.id.btnEnter2);

        btSignIn.setOnClickListener(this);
        btSignUp.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if (view == btSignUp) {
            // מעבר למסך הרשמה
            Intent go = new Intent(this, RegisterActivity.class);
            startActivity(go);
        } else if (view == btSignIn) {
            // ניסיון התחברות
            String email = etEmail.getText().toString().trim();
            String pass = etPassword.getText().toString();

            if (email.isEmpty() || pass.isEmpty()) {
                tvInstruction.setText("יש למלא את כל השדות");

                // רענון המסך
                Intent refresh = new Intent(MainActivity.this, MainActivity.class);
                finish();
                startActivity(refresh);
                return;
            }

            // התחברות ל-Firebase
            firebaseAuth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        /**
                         * מאזין לתוצאת התחברות Firebase.
                         *
                         * @param task תוצאת הפעולה (הצלחה או כישלון)
                         */
                        @Override
                        public void onComplete(Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // מעבר למסך הבית
                                startActivity(intent);
                            } else {
                                // הצגת הודעת שגיאה
                                tvInstruction.setText("המייל או הסיסמה שגויים");
                            }
                        }
                    });
        }
    }
}