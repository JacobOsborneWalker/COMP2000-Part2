package com.example.comp20002;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AdminPortal extends AppCompatActivity {

    TextView welcomeTextView;
    Button PersonalDetails;
    Button StaffView;
    Button requestsButton;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_portal);

        welcomeTextView = findViewById(R.id.welcome_text);
        PersonalDetails = findViewById(R.id.Info_Button);
        StaffView = findViewById(R.id.StaffMangement);
        requestsButton = findViewById(R.id.requestsButton);
        databaseHelper = new DatabaseHelper(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loadUserData();

        PersonalDetails.setOnClickListener(v -> {
            int userId = getLoggedInUserId();

            Intent personalDetailsIntent = new Intent(AdminPortal.this, PersonalDetailsPage.class);
            personalDetailsIntent.putExtra("user_id", userId);
        });

        StaffView.setOnClickListener(v -> {
            Intent staffViewIntent = new Intent(AdminPortal.this, AdminStaffView.class);
            startActivity(staffViewIntent);
        });

        requestsButton.setOnClickListener(v -> {
            Intent requestsPageIntent = new Intent(AdminPortal.this, RequestsPage.class);
            startActivity(requestsPageIntent);
        });
    }

    private void loadUserData() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query("users", null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            String firstName = cursor.getString(cursor.getColumnIndexOrThrow("firstname"));
            String lastName = cursor.getString(cursor.getColumnIndexOrThrow("lastname"));


            welcomeTextView.setText("Welcome, " + firstName + " " + lastName + "!");
            cursor.close();
        }

        db.close();
    }

    private int getLoggedInUserId() {

        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query("users", new String[]{"id"}, null, null, null, null, null);

        int userId = -1;
        if (cursor != null && cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            cursor.close();
        }
        db.close();

        return userId;
    }
}
