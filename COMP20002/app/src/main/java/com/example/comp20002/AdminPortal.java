package com.example.comp20002;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AdminPortal extends AppCompatActivity {

    Button StaffViewButton;
    Button PersonalDetails;
    Button NotificationButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_portal);

        // Handle window insets (optional)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Get first and last name from Intent
        Intent intent = getIntent();
        String firstName = intent.getStringExtra("fn");
        String lastName = intent.getStringExtra("ln");
        String email = intent.getStringExtra("email");

        // Display welcome message
        TextView welcomeTextView = findViewById(R.id.welcome_text);
        welcomeTextView.setText("Welcome, " + firstName + " " + lastName + "!");

        PersonalDetails = findViewById(R.id.Info_Button);


        PersonalDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminPortal.this, PersonalDetailsPage.class);
                intent.putExtra("email", email);
                startActivity(intent);
            }
        });
    }
}
