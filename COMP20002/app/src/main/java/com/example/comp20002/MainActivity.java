package com.example.comp20002;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.Button;
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

    // Interface to request
    interface RequestUser {
        @GET("employees")
        Call<List<UserData>> getUsers();
    }

    TextView textView;
    Button LoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        textView = findViewById(R.id.textView);  // Ensure this ID exists in activity_main.xml
        LoginButton = findViewById(R.id.Login_Button);
        EditText EnteredEmail = findViewById(R.id.email_input);
        EditText UserID = findViewById(R.id.id_input);

        // Apply Window Insets (ensure R.id.main is a valid view)
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

        // Initialise Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.224.41.11/comp2000/")  // Ensure the base URL ends with '/'
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create an instance of the Retrofit interface
        RequestUser requestUser = retrofit.create(RequestUser.class);

        // Fetch the list of users
        requestUser.getUsers().enqueue(new Callback<List<UserData>>() {
            @Override
            public void onResponse(Call<List<UserData>> call, Response<List<UserData>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    StringBuilder userNames = new StringBuilder();
                    for (UserData user : response.body()) {
                        userNames.append(user.firstname).append(" ").append(user.lastname).append("\n");
                    }
                    textView.setText(userNames.toString());
                } else {
                    textView.setText("Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<UserData>> call, Throwable t) {
                textView.setText("Failure: " + t.getMessage());
            }
        });
    }
}
