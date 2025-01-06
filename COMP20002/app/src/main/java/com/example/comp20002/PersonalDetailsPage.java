package com.example.comp20002;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;

public class PersonalDetailsPage extends AppCompatActivity {

    EditText firstNameView, lastNameView, emailView, jobIdView, joiningDateView, leavesView, salaryView, positionView;
    Button saveButton;
    String userId;

    private static final String BASE_URL = "http://10.224.41.11/comp2000/employees/edit/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_details_page);

        firstNameView = findViewById(R.id.FirstName);
        lastNameView = findViewById(R.id.SecondName);
        emailView = findViewById(R.id.EmailAdress);
        jobIdView = findViewById(R.id.StaffID);
        joiningDateView = findViewById(R.id.StartDate);
        leavesView = findViewById(R.id.Leaves);
        salaryView = findViewById(R.id.JobSalary);
        positionView = findViewById(R.id.JobTitle);
        saveButton = findViewById(R.id.saveButton);

        loadUserData();

        saveButton.setOnClickListener(v -> updateUserData());
    }

    private void loadUserData() {
        firstNameView.setText("John");
        lastNameView.setText("Doe");
        emailView.setText("john.doe@example.com");
        jobIdView.setText("1");
        joiningDateView.setText("2023-01-01");
        leavesView.setText("30");
        salaryView.setText("50000");
        positionView.setText("HR");

        userId = jobIdView.getText().toString();
    }

    private void updateUserData() {
        String firstName = firstNameView.getText().toString();
        String lastName = lastNameView.getText().toString();
        String email = emailView.getText().toString();
        String jobId = jobIdView.getText().toString();
        String joiningDate = joiningDateView.getText().toString();
        String leaves = leavesView.getText().toString();
        String salary = salaryView.getText().toString();
        String position = positionView.getText().toString();

        JSONObject updatedData = new JSONObject();
        try {
            updatedData.put("firstname", firstName);
            updatedData.put("lastname", lastName);
            updatedData.put("email", email);
            updatedData.put("department", position);  // Assuming position is the department
            updatedData.put("salary", salary);
            updatedData.put("joiningdate", joiningDate);
            updatedData.put("leaves", leaves);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        updateEmployeeDataToAPI(updatedData);
    }

    private void updateEmployeeDataToAPI(JSONObject updatedData) {
        new Thread(() -> {
            OkHttpClient client = new OkHttpClient();

            RequestBody body = RequestBody.create(updatedData.toString(), okhttp3.MediaType.parse("application/json; charset=utf-8"));
            Request request = new Request.Builder()
                    .url(BASE_URL + userId)
                    .put(body)
                    .build();

            try {
                Log.d("PersonalDetailsPage", "Sending request: " + request.url());
                Response response = client.newCall(request).execute();
                Log.d("PersonalDetailsPage", "Response code: " + response.code());
                Log.d("PersonalDetailsPage", "Response body: " + response.body().string());

                if (response.isSuccessful()) {
                    runOnUiThread(() -> Toast.makeText(PersonalDetailsPage.this, "Employee data updated successfully!", Toast.LENGTH_SHORT).show());
                } else {
                    runOnUiThread(() -> Toast.makeText(PersonalDetailsPage.this, "Failed to update employee data.", Toast.LENGTH_SHORT).show());
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("PersonalDetailsPage", "Error: ", e);
                runOnUiThread(() -> Toast.makeText(PersonalDetailsPage.this, "Error occurred while updating data.", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
}
