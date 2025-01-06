package com.example.comp20002;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RequestsPage extends AppCompatActivity {

    private Spinner requestsSpinner;
    private Spinner notificationsSpinner;
    private TextView selectedRequestTextView;
    private TextView selectedNotificationTextView;
    private Button readButton;
    private Button acceptButton;
    private Button denyButton;
    private Button returnToAdminPortalButton;
    private DatabaseHelper databaseHelper;

    private int currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests_page);

        requestsSpinner = findViewById(R.id.requestsSpinner);
        notificationsSpinner = findViewById(R.id.notificationsSpinner);
        selectedRequestTextView = findViewById(R.id.selectedRequestTextView);
        selectedNotificationTextView = findViewById(R.id.selectedNotificationTextView);
        readButton = findViewById(R.id.readButton);
        acceptButton = findViewById(R.id.acceptButton);
        denyButton = findViewById(R.id.denyButton);
        returnToAdminPortalButton = findViewById(R.id.returnToAdminPortalButton);
        databaseHelper = new DatabaseHelper(this);

        currentUserId = getIntent().getIntExtra("user_id", -1);

        selectedRequestTextView.setText("Select a request");
        selectedNotificationTextView.setText("Select a notification");

        String[] notificationsMessages = {"Notification 1", "Notification 2", "Notification 3"};
        notificationsSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, notificationsMessages));

        fetchRequestsFromDatabase();

        requestsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View view, int position, long id) {
                String selectedMessage = parentView.getItemAtPosition(position).toString();
                if (selectedMessage.equals("No holiday requests available")) {
                    selectedRequestTextView.setText("No request selected.");
                } else {
                    selectedRequestTextView.setText("Selected Request: " + selectedMessage);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                selectedRequestTextView.setText("No request selected.");
            }
        });

        notificationsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View view, int position, long id) {
                String selectedMessage = parentView.getItemAtPosition(position).toString();
                selectedNotificationTextView.setText("Selected Notification: " + selectedMessage);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                selectedNotificationTextView.setText("No notification selected.");
            }
        });

        readButton.setOnClickListener(v -> {
            String selectedMessage = selectedNotificationTextView.getText().toString();
            if (selectedMessage.contains("Selected Notification:")) {
                Toast.makeText(RequestsPage.this, "Notification read.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(RequestsPage.this, "Please select a notification to read.", Toast.LENGTH_SHORT).show();
            }
        });

        acceptButton.setOnClickListener(v -> {
            String selectedMessage = selectedRequestTextView.getText().toString();
            if (selectedMessage.contains("Selected Request:") && !selectedMessage.contains("No holiday requests available")) {

                String requestId = selectedMessage.split(":")[1].trim();
                acceptOrDenyRequest(requestId, "accepted");
            } else {
                Toast.makeText(RequestsPage.this, "Please select a valid request to accept.", Toast.LENGTH_SHORT).show();
            }
        });

        denyButton.setOnClickListener(v -> {
            String selectedMessage = selectedRequestTextView.getText().toString();
            if (selectedMessage.contains("Selected Request:") && !selectedMessage.contains("No holiday requests available")) {
                String requestId = selectedMessage.split(":")[1].trim(); // Extract the request ID from the text
                acceptOrDenyRequest(requestId, "denied");
            } else {
                Toast.makeText(RequestsPage.this, "Please select a valid request to deny.", Toast.LENGTH_SHORT).show();
            }
        });

        returnToAdminPortalButton.setOnClickListener(v -> {
            Intent intent = new Intent(RequestsPage.this, AdminPortal.class);
            startActivity(intent);
        });
    }

    private void fetchRequestsFromDatabase() {
        Cursor cursor = databaseHelper.getAllHolidayRequests();

        if (cursor != null && cursor.getCount() > 0) {
            String[] requestsArray = new String[cursor.getCount()];
            int index = 0;


            int requestIdIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_REQUEST_ID);
            int startDateIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_START_DATE);
            int endDateIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_END_DATE);

            if (requestIdIndex == -1 || startDateIndex == -1 || endDateIndex == -1) {
                Toast.makeText(this, "Error: Missing expected columns in database.", Toast.LENGTH_SHORT).show();
                return;
            }

            while (cursor.moveToNext()) {
                String requestId = cursor.getString(requestIdIndex);
                String startDate = cursor.getString(startDateIndex);
                String endDate = cursor.getString(endDateIndex);
                requestsArray[index] = requestId + ": Start Date: " + startDate + ", End Date: " + endDate;
                index++;
            }
            cursor.close();

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, requestsArray);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            requestsSpinner.setAdapter(adapter);
        } else {

            String[] noRequests = {"No holiday requests available"};
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, noRequests);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            requestsSpinner.setAdapter(adapter);
        }
    }

    private void acceptOrDenyRequest(String requestId, String status) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        int rowsDeleted = db.delete(DatabaseHelper.TABLE_HOLIDAY_REQUESTS,
                "request_id = ?",
                new String[]{requestId});

        if (rowsDeleted > 0) {
            Toast.makeText(RequestsPage.this, "Request " + status + " and removed from the database.", Toast.LENGTH_SHORT).show();
            fetchRequestsFromDatabase();
        } else {
            Toast.makeText(RequestsPage.this, "Failed to process the request.", Toast.LENGTH_SHORT).show();
        }

        db.close();
    }
}
