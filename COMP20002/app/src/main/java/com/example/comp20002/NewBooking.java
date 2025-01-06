package com.example.comp20002;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class NewBooking extends AppCompatActivity {

    private EditText startDateEditText;
    private EditText endDateEditText;
    private Button createBookingButton;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_booking);

        // Initialize views
        startDateEditText = findViewById(R.id.startDateEditText);
        endDateEditText = findViewById(R.id.endDateEditText);
        createBookingButton = findViewById(R.id.createBookingButton);
        databaseHelper = new DatabaseHelper(this);

        // Set up date picker for startDateEditText
        startDateEditText.setOnClickListener(v -> showDatePickerDialog(startDateEditText));

        // Set up date picker for endDateEditText
        endDateEditText.setOnClickListener(v -> showDatePickerDialog(endDateEditText));

        // Handle "Create Booking" button click
        createBookingButton.setOnClickListener(v -> {
            String startDate = startDateEditText.getText().toString();
            String endDate = endDateEditText.getText().toString();

            if (startDate.isEmpty() || endDate.isEmpty()) {
                Toast.makeText(NewBooking.this, "Please enter valid start and end dates.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Add the booking to the database
            addBookingToDatabase(startDate, endDate);
        });
    }

    // Show a DatePickerDialog and set the selected date on the provided EditText
    private void showDatePickerDialog(EditText editText) {
        // Get current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create and show the DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDay) -> {
            // Update the EditText with the selected date
            String date = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
            editText.setText(date);
        }, year, month, day);

        datePickerDialog.show();
    }

    // Method to add a booking to the database
    private void addBookingToDatabase(String startDate, String endDate) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_START_DATE, startDate);
        values.put(DatabaseHelper.COLUMN_END_DATE, endDate);

        // Insert the booking into the holiday requests table
        long rowId = db.insert(DatabaseHelper.TABLE_HOLIDAY_REQUESTS, null, values);

        if (rowId != -1) {
            Toast.makeText(NewBooking.this, "Booking created successfully.", Toast.LENGTH_SHORT).show();
            // Optionally, clear input fields
            startDateEditText.setText("");
            endDateEditText.setText("");
        } else {
            Toast.makeText(NewBooking.this, "Failed to create booking.", Toast.LENGTH_SHORT).show();
        }

        db.close();
    }
}
