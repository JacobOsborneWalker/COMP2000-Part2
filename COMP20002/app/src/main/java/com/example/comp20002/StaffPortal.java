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

public class StaffPortal extends AppCompatActivity {

    Button PersonalDetails, BookingButton;
    TextView welcomeTextView, leavesTextView;
    DatabaseHelper databaseHelper;

    String firstName, lastName, email, joiningDate, position;
    int salary, leaves, userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_portal);

        welcomeTextView = findViewById(R.id.welcome_text);
        leavesTextView = findViewById(R.id.tvLeaves);
        PersonalDetails = findViewById(R.id.Info_Button);
        BookingButton = findViewById(R.id.StaffMangement);
        databaseHelper = new DatabaseHelper(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userId = getIntent().getIntExtra("user_id", -1);
        int leavesLeft = getIntent().getIntExtra("leaves_left", 0);

        loadUserData(userId, leavesLeft);

        PersonalDetails.setOnClickListener(v -> {
            Intent intent = new Intent(StaffPortal.this, PersonalDetailsPage.class);
            intent.putExtra("firstname", firstName);
            intent.putExtra("lastname", lastName);
            intent.putExtra("email", email);
            intent.putExtra("jobId", userId);
            intent.putExtra("joiningDate", joiningDate);
            intent.putExtra("leaves", leaves);
            intent.putExtra("salary", salary);
            intent.putExtra("position", position);
            startActivity(intent);
        });

        BookingButton.setOnClickListener(v -> {
            Intent bookingIntent = new Intent(StaffPortal.this, StaffBookings.class);
            bookingIntent.putExtra("user_id", userId);
            bookingIntent.putExtra("leaves_left", leavesLeft);
            bookingIntent.putExtra("remaining_days", leavesLeft);
            startActivity(bookingIntent);
        });
    }

    private void loadUserData(int userId, int leavesLeft) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query("users", null, "id = ?", new String[]{String.valueOf(userId)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {

            firstName = cursor.getString(cursor.getColumnIndexOrThrow("firstname"));
            lastName = cursor.getString(cursor.getColumnIndexOrThrow("lastname"));
            email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
            joiningDate = cursor.getString(cursor.getColumnIndexOrThrow("joining_date"));
            salary = cursor.getInt(cursor.getColumnIndexOrThrow("salary"));
            position = cursor.getString(cursor.getColumnIndexOrThrow("department"));
            leaves = cursor.getInt(cursor.getColumnIndexOrThrow("leaves"));

            welcomeTextView.setText("Welcome, " + firstName + " " + lastName + "!");
            leavesTextView.setText("Leaves remaining: " + leaves);

            cursor.close();
        } else {
            welcomeTextView.setText("No user data found. Please log in again.");
        }

        db.close();
    }
}
