package com.example.comp20002;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class NewBookings extends AppCompatActivity {

    TextView daysRemainingText, daysBookedText;
    Button btnStartDate, btnEndDate, btnConfirmBooking, btnReturnToPortal;
    Calendar startDate, endDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_booking);

        daysRemainingText = findViewById(R.id.daysRemainingText);
        daysBookedText = findViewById(R.id.daysBookedText);
        btnStartDate = findViewById(R.id.btnStartDate);
        btnEndDate = findViewById(R.id.btnEndDate);
        btnConfirmBooking = findViewById(R.id.btnConfirmBooking);
        btnReturnToPortal = findViewById(R.id.btnReturnToPortal);

        startDate = Calendar.getInstance();
        endDate = Calendar.getInstance();

        // Fetch remaining days from API (placeholder for now)
        fetchRemainingDays();

        // Start date picker
        btnStartDate.setOnClickListener(v -> showDatePicker(startDate, btnStartDate));

        // End date picker
        btnEndDate.setOnClickListener(v -> showDatePicker(endDate, btnEndDate));

        // Confirm booking logic
        btnConfirmBooking.setOnClickListener(v -> calculateDaysBooked());

        // Return to staff portal
        btnReturnToPortal.setOnClickListener(v -> {
            Intent intent = new Intent(NewBookings.this, StaffPortal.class);
            startActivity(intent);
            finish();
        });
    }

    private void fetchRemainingDays() {
        // Placeholder for fetching data from API
        int remainingDays = 10; // Replace with API call
        daysRemainingText.setText("Days Remaining: " + remainingDays);
    }

    private void showDatePicker(Calendar calendar, Button button) {
        DatePickerDialog datePicker = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            calendar.set(year, month, dayOfMonth);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            button.setText(dateFormat.format(calendar.getTime()));
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePicker.show();
    }

    private void calculateDaysBooked() {
        if (startDate != null && endDate != null) {
            long difference = endDate.getTimeInMillis() - startDate.getTimeInMillis();
            int daysBetween = (int) (difference / (1000 * 60 * 60 * 24)) + 1;

            // Subtract weekends
            Calendar temp = (Calendar) startDate.clone();
            int weekends = 0;
            while (!temp.after(endDate)) {
                int dayOfWeek = temp.get(Calendar.DAY_OF_WEEK);
                if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
                    weekends++;
                }
                temp.add(Calendar.DAY_OF_MONTH, 1);
            }
            daysBetween -= weekends;

            daysBookedText.setText("Days Booked (excluding weekends): " + daysBetween);
        }
    }
}
