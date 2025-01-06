package com.example.comp20002;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    interface RequestUser {
        @GET("employees")
        Call<List<UserData>> getUsers();
    }

    TextView textView;
    Button LoginButton;
    EditText EnteredEmail, UserID;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        LoginButton = findViewById(R.id.Login_Button);
        EnteredEmail = findViewById(R.id.email_input);
        UserID = findViewById(R.id.id_input);

        databaseHelper = new DatabaseHelper(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return WindowInsetsCompat.CONSUMED;
        });

        // logging
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

        // retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.224.41.11/comp2000/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestUser requestUser = retrofit.create(RequestUser.class);

        LoginButton.setOnClickListener(v -> {
            String email = EnteredEmail.getText().toString().trim();
            String userIdString = UserID.getText().toString().trim();

            // test data
            if (email.equalsIgnoreCase("test")) {
                saveUserDataToDatabase("Test", "User", "test@gmail.com", 111, "Mon, 24 Mar 2021 00:00:00 GMT", 30, 50000, "HR");
                navigateToPortal("HR", 111, 30); // Pass default values for test user
                return;
            }

            // valid
            if (email.isEmpty() || userIdString.isEmpty()) {
                textView.setText("Please enter both email and ID.");
                return;
            }

            int userId;
            try {
                userId = Integer.parseInt(userIdString);
            } catch (NumberFormatException e) {
                textView.setText("Invalid ID. Please enter a numeric value.");
                return;
            }

            textView.setText("Checking user credentials...");

            // api call
            requestUser.getUsers().enqueue(new Callback<List<UserData>>() {
                @Override
                public void onResponse(Call<List<UserData>> call, Response<List<UserData>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        boolean found = false;

                        for (UserData user : response.body()) {
                            if (user.email != null && email != null && user.email.equalsIgnoreCase(email) && user.id == userId) {
                                found = true;
                                saveUserDataToDatabase(user.firstname, user.lastname, user.email, user.id, user.joiningdate, user.leaves, (int) user.salary, user.department);
                                navigateToPortal(user.department, user.id, user.leaves);
                                break;
                            }
                        }

                        if (!found) {
                            runOnUiThread(() -> textView.setText("No matching user found. Please check your email and ID."));
                        }
                    } else {
                        runOnUiThread(() -> textView.setText("Error: Unable to fetch data. Response code: " + response.code()));
                    }
                }

                @Override
                public void onFailure(Call<List<UserData>> call, Throwable t) {
                    runOnUiThread(() -> textView.setText("Network error: " + t.getMessage() + ". Please try again."));
                }
            });
        });
    }

    private void saveUserDataToDatabase(String firstName, String lastName, String email, int id, String joiningDate, int leaves, int salary, String department) {
        databaseHelper.clearUserData();

        // add data to db
        ContentValues values = new ContentValues();
        values.put("firstname", firstName);
        values.put("lastname", lastName);
        values.put("email", email);
        values.put("id", id);
        values.put("joining_date", joiningDate);
        values.put("leaves", leaves);
        values.put("salary", salary);
        values.put("department", department);

        databaseHelper.insertUserData(values);
    }

    // Check if HR
    private void navigateToPortal(String department, int userId, int leaves) {
        Intent intent;
        if ("HR".equalsIgnoreCase(department)) {
            intent = new Intent(this, AdminPortal.class);
        } else {
            intent = new Intent(this, StaffPortal.class);
            // Pass the user ID and leaves as extras to the StaffPortal
            intent.putExtra("user_id", userId);
            intent.putExtra("leaves_left", leaves);
        }
        startActivity(intent);
        finish();
    }

}
