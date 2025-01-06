package com.example.comp20002;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NewBooking extends AppCompatActivity {

    private EditText startDateEditText;
    private EditText endDateEditText;
    private Button createBookingButton;
    private TextView remainingDaysTextView;
    private DatabaseHelper databaseHelper;

    private int userId;
    private int remainingDays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_booking);

        startDateEditText = findViewById(R.id.startDateEditText);
        endDateEditText = findViewById(R.id.endDateEditText);
        createBookingButton = findViewById(R.id.createBookingButton);
        remainingDaysTextView = findViewById(R.id.daysRemainingText);


        userId = getIntent().getIntExtra("user_id", -1);
        remainingDays = getIntent().getIntExtra("leaves_left", 0);


        updateRemainingDaysText();


        databaseHelper = new DatabaseHelper(this);


        startDateEditText.setOnClickListener(v -> showDatePickerDialog(startDateEditText));
        endDateEditText.setOnClickListener(v -> showDatePickerDialog(endDateEditText));


        createBookingButton.setOnClickListener(v -> {
            String startDate = startDateEditText.getText().toString();
            String endDate = endDateEditText.getText().toString();

            if (startDate.isEmpty() || endDate.isEmpty()) {
                Toast.makeText(NewBooking.this, "Please enter valid start and end dates.", Toast.LENGTH_SHORT).show();
                return;
            }

            int requestedDays = calculateDaysBetweenDates(startDate, endDate);

            if (requestedDays <= 0) {
                Toast.makeText(NewBooking.this, "End date must be after start date.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (requestedDays > remainingDays) {
                Toast.makeText(NewBooking.this, "Insufficient remaining days for this booking.", Toast.LENGTH_SHORT).show();
                return;
            }

            addBookingToDatabase(startDate, endDate, requestedDays);
        });
    }

    private void showDatePickerDialog(EditText editText) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDay) -> {
            String date = String.format(Locale.getDefault(), "%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear);
            editText.setText(date);
        }, year, month, day);

        datePickerDialog.show();
    }

    private int calculateDaysBetweenDates(String startDate, String endDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date start = sdf.parse(startDate);
            Date end = sdf.parse(endDate);

            if (start == null || end == null) return 0;

            long differenceInMillis = end.getTime() - start.getTime();
            return (int) (differenceInMillis / (1000 * 60 * 60 * 24)) + 1;
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private void addBookingToDatabase(String startDate, String endDate, int requestedDays) {

        boolean success = databaseHelper.insertHolidayRequest(userId, startDate, endDate, "pending");

        if (success) {
            remainingDays -= requestedDays;
            updateRemainingDaysText();
            Toast.makeText(NewBooking.this, "Booking created successfully. Remaining days: " + remainingDays, Toast.LENGTH_SHORT).show();

            startDateEditText.setText("");
            endDateEditText.setText("");
        } else {
            Toast.makeText(NewBooking.this, "Failed to create booking.", Toast.LENGTH_SHORT).show();
        }
    }


    private void updateRemainingDaysText() {
        remainingDaysTextView.setText("Remaining Days: " + remainingDays);
    }
}
