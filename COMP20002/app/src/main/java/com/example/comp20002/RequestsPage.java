package com.example.comp20002;

import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests_page); // Ensure this layout file is updated.

        // Initialize views
        requestsSpinner = findViewById(R.id.requestsSpinner);
        notificationsSpinner = findViewById(R.id.notificationsSpinner);
        selectedRequestTextView = findViewById(R.id.selectedRequestTextView);
        selectedNotificationTextView = findViewById(R.id.selectedNotificationTextView);
        readButton = findViewById(R.id.readButton);
        acceptButton = findViewById(R.id.acceptButton);
        denyButton = findViewById(R.id.denyButton);
        returnToAdminPortalButton = findViewById(R.id.returnToAdminPortalButton);

        // Set default messages
        selectedRequestTextView.setText("Select a request");
        selectedNotificationTextView.setText("Select a notification");

        // Set up requests spinner (for accept/deny)
        String[] requestsMessages = {"Request 1", "Request 2", "Request 3"};
        requestsSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, requestsMessages));

        // Set up notifications spinner (for reading notifications)
        String[] notificationsMessages = {"Notification 1", "Notification 2", "Notification 3"};
        notificationsSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, notificationsMessages));

        // Spinner item selection listener for requests (Accept/Deny)
        requestsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View view, int position, long id) {
                String selectedMessage = parentView.getItemAtPosition(position).toString();
                selectedRequestTextView.setText("Selected Request: " + selectedMessage);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                selectedRequestTextView.setText("No request selected.");
            }
        });

        // Spinner item selection listener for notifications (Read)
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

        // Handle the "Read" button click (for notifications)
        readButton.setOnClickListener(v -> {
            String selectedMessage = selectedNotificationTextView.getText().toString();
            if (selectedMessage.contains("Selected Notification:")) {
                // Simulate reading the notification
                Toast.makeText(RequestsPage.this, "Notification read.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(RequestsPage.this, "Please select a notification to read.", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle the "Accept" button click (for requests)
        acceptButton.setOnClickListener(v -> {
            String selectedMessage = selectedRequestTextView.getText().toString();
            if (selectedMessage.contains("Selected Request:")) {
                // Simulate accepting the request
                Toast.makeText(RequestsPage.this, "Request accepted.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(RequestsPage.this, "Please select a request to accept.", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle the "Deny" button click (for requests)
        denyButton.setOnClickListener(v -> {
            String selectedMessage = selectedRequestTextView.getText().toString();
            if (selectedMessage.contains("Selected Request:")) {
                // Simulate denying the request
                Toast.makeText(RequestsPage.this, "Request denied.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(RequestsPage.this, "Please select a request to deny.", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle the "Return to Admin Portal" button click
        returnToAdminPortalButton.setOnClickListener(v -> {
            Intent intent = new Intent(RequestsPage.this, AdminPortal.class);
            startActivity(intent);
        });
    }
}
