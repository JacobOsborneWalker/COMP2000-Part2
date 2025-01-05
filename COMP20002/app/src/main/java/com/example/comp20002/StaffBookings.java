package com.example.comp20002;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ExpandableListView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class StaffBookings extends AppCompatActivity {

    ExpandableListView expandableListView;
    ExpandableListAdapter listAdapter;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_bookings);

        // Initialize views
        expandableListView = findViewById(R.id.expandableListView);

        // Prepare data for the expandable list
        prepareListData();

        // Initialize the adapter and set it to the expandable list view
        if (listDataHeader != null && listDataChild != null) {
            listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
            expandableListView.setAdapter(listAdapter);

            // Expand the first group by default
            expandableListView.expandGroup(0);
        } else {
            Log.e("StaffBookings", "Data not initialized properly!");
        }

        // Button to return to StaffPortal
        Button returnToPortalButton = findViewById(R.id.btnReturnToPortal);
        returnToPortalButton.setOnClickListener(v -> {
            Intent intent = new Intent(StaffBookings.this, StaffPortal.class);
            startActivity(intent);
        });

        // Button to create a new booking
        Button newBookingButton = findViewById(R.id.btnCreateNewBooking);
        newBookingButton.setOnClickListener(v -> {
            Intent intent = new Intent(StaffBookings.this, NewBookings.class);
            startActivity(intent);
        });
    }

    private void prepareListData() {
        // Initialize header and child data
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        // Adding headers
        listDataHeader.add("Approved Breaks");
        listDataHeader.add("Pending Breaks");
        listDataHeader.add("Past Breaks");

        // Adding child data for each header
        List<String> approved = Arrays.asList("Option 1", "Option 2", "Option 3");
        List<String> pending = Arrays.asList("Option 1", "Option 2", "Option 3");
        List<String> past = Arrays.asList("Option 1", "Option 2", "Option 3");

        listDataChild.put(listDataHeader.get(0), approved);
        listDataChild.put(listDataHeader.get(1), pending);
        listDataChild.put(listDataHeader.get(2), past);
    }
}
