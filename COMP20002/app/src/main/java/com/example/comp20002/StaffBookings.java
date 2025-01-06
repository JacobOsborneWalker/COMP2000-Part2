package com.example.comp20002;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class StaffBookings extends AppCompatActivity {

    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    int remainingDays;
    int userId;
    TextView remainingDaysTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_bookings);


        remainingDays = getIntent().getIntExtra("remaining_days", 0);
        userId = getIntent().getIntExtra("user_id", -1);

        Log.d("StaffBookings", "Remaining days: " + remainingDays);
        Log.d("StaffBookings", "User ID: " + userId);


        ExpandableListView expandableListView = findViewById(R.id.expandableListView);
        remainingDaysTextView = findViewById(R.id.tvRemainingDays);


        remainingDaysTextView.setText("Remaining Days: " + remainingDays);


        prepareListData();


        if (listDataHeader != null && listDataChild != null) {
            ExpandableListAdapter listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
            expandableListView.setAdapter(listAdapter);
            expandableListView.expandGroup(0);
        } else {
            Log.e("StaffBookings", "Data not initialized properly!");
        }


        Button returnToPortalButton = findViewById(R.id.btnReturnToPortal);
        returnToPortalButton.setOnClickListener(v -> {
            Intent intent = new Intent(StaffBookings.this, StaffPortal.class);
            startActivity(intent);
        });


        Button newBookingButton = findViewById(R.id.btnCreateNewBooking);
        newBookingButton.setOnClickListener(v -> {
            Intent intent = new Intent(StaffBookings.this, NewBookings.class);
            intent.putExtra("remaining_days", remainingDays);  // Pass remaining days
            intent.putExtra("user_id", userId);  // Pass user ID
            startActivity(intent);
        });
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        listDataHeader.add("Approved Breaks");
        listDataHeader.add("Pending Breaks");
        listDataHeader.add("Past Breaks");


        List<String> approved = Arrays.asList("Option 1", "Option 2", "Option 3");
        List<String> pending = Arrays.asList("Option 1", "Option 2", "Option 3");
        List<String> past = Arrays.asList("Option 1", "Option 2", "Option 3");

        listDataChild.put(listDataHeader.get(0), approved);
        listDataChild.put(listDataHeader.get(1), pending);
        listDataChild.put(listDataHeader.get(2), past);
    }
}
