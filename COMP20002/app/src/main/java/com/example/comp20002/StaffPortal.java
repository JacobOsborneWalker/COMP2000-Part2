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

public class StaffPortal extends AppCompatActivity {

    Button PersonalDetails, BookingButton; // Added BookingButton
    TextView welcomeTextView;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_portal);

        welcomeTextView = findViewById(R.id.welcome_text);
        PersonalDetails = findViewById(R.id.Info_Button);
        BookingButton = findViewById(R.id.StaffMangement);
        databaseHelper = new DatabaseHelper(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Get the passed user ID and leaves data
        int userId = getIntent().getIntExtra("user_id", -1);
        int leavesLeft = getIntent().getIntExtra("leaves_left", 0);

        loadUserData(userId, leavesLeft);

        // Set up listener for the PersonalDetails button
        PersonalDetails.setOnClickListener(v -> {
            Intent personalDetailsIntent = new Intent(StaffPortal.this, PersonalDetailsPage.class);
            startActivity(personalDetailsIntent);
        });

        // Set up listener for the Booking button
        BookingButton.setOnClickListener(v -> {
            Intent bookingIntent = new Intent(StaffPortal.this, StaffBookings.class);
            // Pass the user ID and leaves as extras to the StaffBookings page
            bookingIntent.putExtra("user_id", userId); // Pass user ID
            bookingIntent.putExtra("leaves_left", leavesLeft); // Pass holidays left
            bookingIntent.putExtra("remaining_days", leavesLeft); // Pass remaining days explicitly
            startActivity(bookingIntent);
        });


    }

    private void loadUserData(int userId, int leavesLeft) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query("users", null, "id = ?", new String[]{String.valueOf(userId)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            String firstName = cursor.getString(cursor.getColumnIndexOrThrow("firstname"));
            String lastName = cursor.getString(cursor.getColumnIndexOrThrow("lastname"));

            // Set the welcome text with the user's name and remaining leaves
            welcomeTextView.setText("Welcome, " + firstName + " " + lastName + "!");
            cursor.close();
        } else {
            welcomeTextView.setText("No user data found. Please log in again.");
        }

        db.close();
    }

}
