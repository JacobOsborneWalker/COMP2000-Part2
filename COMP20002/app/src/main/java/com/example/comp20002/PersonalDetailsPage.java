package com.example.comp20002;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class PersonalDetailsPage extends AppCompatActivity {

    TextView emailView, firstNameView, lastNameView, jobIdView, joiningDateView, leavesView, salaryView, positionView;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_details_page);

        emailView = findViewById(R.id.EmailAdress);
        firstNameView = findViewById(R.id.FirstName);
        lastNameView = findViewById(R.id.SecondName);
        jobIdView = findViewById(R.id.StaffID);
        joiningDateView = findViewById(R.id.StartDate);
        leavesView = findViewById(R.id.Leaves);
        salaryView = findViewById(R.id.JobSalary);
        positionView = findViewById(R.id.JobTitle);

        databaseHelper = new DatabaseHelper(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loadUserData();
    }

    private void loadUserData() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query("users", null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            String firstName = cursor.getString(cursor.getColumnIndexOrThrow("firstname"));
            String lastName = cursor.getString(cursor.getColumnIndexOrThrow("lastname"));
            String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
            String jobId = cursor.getString(cursor.getColumnIndexOrThrow("id"));
            String joiningDate = cursor.getString(cursor.getColumnIndexOrThrow("joining_date"));
            int leaves = cursor.getInt(cursor.getColumnIndexOrThrow("leaves"));
            int salary = cursor.getInt(cursor.getColumnIndexOrThrow("salary"));
            String position = cursor.getString(cursor.getColumnIndexOrThrow("department"));

            firstNameView.setText(firstName);
            lastNameView.setText(lastName);
            emailView.setText(email);
            jobIdView.setText(jobId);
            joiningDateView.setText(joiningDate);
            leavesView.setText(String.valueOf(leaves));
            salaryView.setText(String.valueOf(salary));
            positionView.setText(position);

            cursor.close();
        }

        db.close();
    }
}
