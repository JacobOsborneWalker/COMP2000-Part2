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

    Button PersonalDetails, BookingButton;
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

        int userId = getIntent().getIntExtra("user_id", -1);
        int leavesLeft = getIntent().getIntExtra("leaves_left", 0);

        loadUserData(userId, leavesLeft);

        PersonalDetails.setOnClickListener(v -> {
            Intent personalDetailsIntent = new Intent(StaffPortal.this, PersonalDetailsPage.class);
            startActivity(personalDetailsIntent);
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
            String firstName = cursor.getString(cursor.getColumnIndexOrThrow("firstname"));
            String lastName = cursor.getString(cursor.getColumnIndexOrThrow("lastname"));

            welcomeTextView.setText("Welcome, " + firstName + " " + lastName + "!");
            cursor.close();
        } else {
            welcomeTextView.setText("No user data found. Please log in again.");
        }

        db.close();
    }

}
