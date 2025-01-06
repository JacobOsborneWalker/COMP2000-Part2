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

    int leaves;
    int userId;
    TextView leavesTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_bookings);

        userId = getIntent().getIntExtra("user_id", -1);
        leaves = getIntent().getIntExtra("leaves_left", 0);

        Log.d("StaffBookings", "Leaves: " + leaves);
        Log.d("StaffBookings", "User ID: " + userId);


        prepareListData();


        ExpandableListView expandableListView = findViewById(R.id.expandableListView);
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

            Intent intent = new Intent(StaffBookings.this, NewBooking.class);
            intent.putExtra("leaves_left", leaves);
            intent.putExtra("user_id", userId);
            startActivity(intent);
        });
    }


    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();


        listDataHeader.add("Approved Breaks");
        listDataHeader.add("Pending Breaks");
        listDataHeader.add("Past Breaks");


        List<String> approved = Arrays.asList("No Approved Breaks");
        List<String> pending = Arrays.asList("No Pending Breaks");
        List<String> past = Arrays.asList("No Past Breaks");


        listDataChild.put(listDataHeader.get(0), approved);
        listDataChild.put(listDataHeader.get(1), pending);
        listDataChild.put(listDataHeader.get(2), past);
    }
}
