package com.example.recipes.view;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.recipes.R;

/**
 * Activity המהווה מסך פתיחה (ברוכים הבאים) לאפליקציה,
 * ומיישם קצה לקצה (Edge to Edge) להצגת תוכן בשולי המסך בהתאם למערכת.
 */
public class WelcomeActivity extends ActivityWithMenu {

    /**
     * אתחול המסך והפעלת תמיכה ב-Edge to Edge,
     * וכן הגדרת padding מתאים לרכיב הראשי כדי למנוע חיתוך עם שולי המערכת.
     *
     * @param savedInstanceState אובייקט שמכיל מצב שמור (אם קיים)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcome);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}