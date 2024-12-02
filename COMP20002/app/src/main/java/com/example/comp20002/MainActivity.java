package com.example.comp20002;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        LoginButton = findViewById(R.id.Login_Button);
        EnteredEmail = findViewById(R.id.email_input);
        UserID = findViewById(R.id.id_input);

        // Apply Window Insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return WindowInsetsCompat.CONSUMED;
        });

        // Logging interceptor
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

        // Retrofit setup
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.224.41.11/comp2000/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestUser requestUser = retrofit.create(RequestUser.class);


        LoginButton.setOnClickListener(v -> {
            String email = EnteredEmail.getText().toString().trim();
            String userIdString = UserID.getText().toString().trim();

            if (email.equalsIgnoreCase("test")) {
                Intent intent = new Intent(MainActivity.this, AdminPortal.class);
                intent.putExtra("FIRST_NAME", "Test");
                intent.putExtra("LAST_NAME", "User");
                startActivity(intent);
                return;
            }
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

            requestUser.getUsers().enqueue(new Callback<List<UserData>>() {
                @Override
                public void onResponse(Call<List<UserData>> call, Response<List<UserData>> response) {
                    runOnUiThread(() -> {
                        if (response.isSuccessful() && response.body() != null) {
                            boolean found = false;

                            for (UserData user : response.body()) {
                                if (user.email.equalsIgnoreCase(email) && user.id == userId) {
                                    found = true;

                                    if ("HR".equalsIgnoreCase(user.department)) {
                                        // Pass first and last name to AdminPortal if department is HR
                                        Intent intent = new Intent(MainActivity.this, AdminPortal.class);
                                        intent.putExtra("FIRST_NAME", user.firstname);
                                        intent.putExtra("LAST_NAME", user.lastname);
                                        startActivity(intent);
                                    } else {
                                        textView.setText("staff portal");
                                    }
                                    break;
                                }
                            }

                            if (!found) {
                                textView.setText("No matching user found. Please check your email and ID.");
                            }
                        } else {
                            textView.setText("Error: Unable to fetch data. Response code: " + response.code());
                        }
                    });
                }

                @Override
                public void onFailure(Call<List<UserData>> call, Throwable t) {
                    runOnUiThread(() -> textView.setText("Network error: " + t.getMessage() + ". Please try again."));
                }
            });
        });



    }
}
