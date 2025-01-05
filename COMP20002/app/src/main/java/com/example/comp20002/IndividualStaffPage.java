package com.example.comp20002;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class IndividualStaffPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_staff_page);

        // Get the Intent that started this activity
        Intent intent = getIntent();

        // Retrieve the employee details from the Intent
        int userId = intent.getIntExtra("user_id", -1); // Default value -1 if not found
        String firstName = intent.getStringExtra("firstname");
        String lastName = intent.getStringExtra("lastname");
        String email = intent.getStringExtra("email");
        String department = intent.getStringExtra("department");
        String salary = intent.getStringExtra("salary");
        String joiningDate = intent.getStringExtra("joiningdate");
        String leaves = intent.getStringExtra("leaves");

        // Find the TextViews to display the employee details
        TextView nameTextView = findViewById(R.id.nameTextView);
        TextView emailTextView = findViewById(R.id.emailTextView);
        TextView departmentTextView = findViewById(R.id.departmentTextView);
        TextView salaryTextView = findViewById(R.id.salaryTextView);
        TextView joiningDateTextView = findViewById(R.id.joiningDateTextView);
        TextView leavesTextView = findViewById(R.id.leavesTextView);

        // Check for null data and set the TextViews
        if (firstName != null && lastName != null) {
            nameTextView.setText(firstName + " " + lastName);
        } else {
            nameTextView.setText("N/A");
        }

        if (email != null) {
            emailTextView.setText(email);
        } else {
            emailTextView.setText("N/A");
        }

        if (department != null) {
            departmentTextView.setText(department);
        } else {
            departmentTextView.setText("N/A");
        }

        if (salary != null) {
            salaryTextView.setText(salary);
        } else {
            salaryTextView.setText("N/A");
        }

        if (joiningDate != null) {
            joiningDateTextView.setText(joiningDate);
        } else {
            joiningDateTextView.setText("N/A");
        }

        if (leaves != null) {
            leavesTextView.setText(leaves);
        } else {
            leavesTextView.setText("N/A");
        }
    }
}
