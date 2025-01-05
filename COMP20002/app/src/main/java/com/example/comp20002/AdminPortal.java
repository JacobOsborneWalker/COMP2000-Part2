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
    Button requestsButton;  // Updated ID for requestsButton
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_portal);

        // Initialize the UI components
        welcomeTextView = findViewById(R.id.welcome_text);
        PersonalDetails = findViewById(R.id.Info_Button);
        StaffView = findViewById(R.id.StaffMangement);
        requestsButton = findViewById(R.id.requestsButton);  // Updated ID here
        databaseHelper = new DatabaseHelper(this);

        // Handle system insets for padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loadUserData();

        // Button to navigate to Personal Details page
        PersonalDetails.setOnClickListener(v -> {
            Intent personalDetailsIntent = new Intent(AdminPortal.this, PersonalDetailsPage.class);
            startActivity(personalDetailsIntent);
        });

        // Button to navigate to Staff View page
        StaffView.setOnClickListener(v -> {
            Intent staffViewIntent = new Intent(AdminPortal.this, AdminStaffView.class);
            startActivity(staffViewIntent);
        });

        // Button to navigate to Requests page
        requestsButton.setOnClickListener(v -> {
            Intent requestsPageIntent = new Intent(AdminPortal.this, RequestsPage.class);
            startActivity(requestsPageIntent);
        });
    }

    // Load the user's data from the database to show a personalized welcome message
    private void loadUserData() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query("users", null, null, null, null, null, null);

        // Retrieve name
        if (cursor != null && cursor.moveToFirst()) {
            String firstName = cursor.getString(cursor.getColumnIndexOrThrow("firstname"));
            String lastName = cursor.getString(cursor.getColumnIndexOrThrow("lastname"));

            // Set welcome message
            welcomeTextView.setText("Welcome, " + firstName + " " + lastName + "!");
            cursor.close();
        }
        // Close the database
        db.close();
    }
}
