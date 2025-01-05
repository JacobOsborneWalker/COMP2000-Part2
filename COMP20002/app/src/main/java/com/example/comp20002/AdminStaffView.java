package com.example.comp20002;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AdminStaffView extends AppCompatActivity {

    private static final String BASE_URL = "http://10.224.41.11/comp2000/employees";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_staff_view);

        LinearLayout layout = findViewById(R.id.staff_buttons_container);
        fetchEmployees(layout);
    }

    private void fetchEmployees(LinearLayout layout) {
        new Thread(() -> {
            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(BASE_URL).build();
                Response response = client.newCall(request).execute();

                if (response.isSuccessful() && response.body() != null) {
                    String responseBody = response.body().string();

                    // Parse JSON response
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<UserData>>() {}.getType();
                    List<UserData> employees = gson.fromJson(responseBody, listType);

                    // Filter non-HR employees and create buttons
                    runOnUiThread(() -> {
                        for (UserData employee : employees) {
                            if (!"HR".equals(employee.department)) {
                                addEmployeeButton(layout, employee);
                            }
                        }
                    });
                } else {
                    Log.e("API_ERROR", "Failed to fetch employees.");
                }
            } catch (Exception e) {
                Log.e("API_ERROR", "Error occurred while fetching employees", e);
            }
        }).start();
    }

    private void addEmployeeButton(LinearLayout layout, UserData userData) {
        RelativeLayout relativeLayout = (RelativeLayout) getLayoutInflater()
                .inflate(R.layout.staff_button_layout, null);

        Button button = relativeLayout.findViewById(R.id.employeeButton);
        button.setText(userData.firstname + " " + userData.lastname);
        button.setOnClickListener(v -> openDetailsPage(userData));

        layout.addView(relativeLayout);
    }

    private void openDetailsPage(UserData userData) {
        // Create an Intent to open the IndividualStaffPage activity
        Intent intent = new Intent(this, IndividualStaffPage.class);

        // Pass the employee details to the IndividualStaffPage using putExtra
        intent.putExtra("user_id", userData.id);
        intent.putExtra("firstname", userData.firstname);
        intent.putExtra("lastname", userData.lastname);
        intent.putExtra("email", userData.email);
        intent.putExtra("department", userData.department);
        intent.putExtra("salary", userData.salary);
        intent.putExtra("joiningdate", userData.joiningdate);
        intent.putExtra("leaves", userData.leaves);

        // Start the IndividualStaffPage activity
        startActivity(intent);
    }

}
