package com.example.comp20002;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AdminPortal extends AppCompatActivity {

    TextView welcomeTextView;
    Button PersonalDetails;

    Button StaffView;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_portal);

        welcomeTextView = findViewById(R.id.welcome_text);
        PersonalDetails = findViewById(R.id.Info_Button);
        StaffView = findViewById(R.id.StaffMangement);
        databaseHelper = new DatabaseHelper(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loadUserData();

        // buttons
        PersonalDetails.setOnClickListener(v -> {
            Intent personalDetailsIntent = new Intent(AdminPortal.this, PersonalDetailsPage.class);
            startActivity(personalDetailsIntent);
        });

        StaffView.setOnClickListener(v -> {
            Intent personalDetailsIntent = new Intent(AdminPortal.this, AdminStaffView.class);
            startActivity(personalDetailsIntent);
        });
    }

    // database
    private void loadUserData() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query("users", null, null, null, null, null, null);

        // retrieve name
        if (cursor != null && cursor.moveToFirst()) {
            String firstName = cursor.getString(cursor.getColumnIndexOrThrow("firstname"));
            String lastName = cursor.getString(cursor.getColumnIndexOrThrow("lastname"));

            // welcome
            welcomeTextView.setText("Welcome, " + firstName + " " + lastName + "!");
            cursor.close();
        }
        // close db
        db.close();
    }
}
