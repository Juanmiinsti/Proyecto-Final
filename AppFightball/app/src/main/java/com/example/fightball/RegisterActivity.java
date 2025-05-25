package com.example.fightball;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fightball.API.RetroFitBuilder;
import com.example.fightball.Models.RegisterResponse;
import com.example.fightball.Models.UserModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activity for user registration.
 * Provides a form for the user to enter username and password, confirm password,
 * and sends the data to a REST API to create a new user account.
 * Uses Retrofit for API calls and SharedPreferences to get the authorization key.
 * Supports edge-to-edge display with system bar insets handling.
 */
public class RegisterActivity extends AppCompatActivity {

    /** Cancel button to close the registration without saving */
    Button cancelbt;

    /** Save button to submit registration */
    Button saveBt;

    /** Input field for username */
    EditText usernameET;

    /** Input field for password */
    EditText passwordET;

    /** Input field for password confirmation */
    EditText conPasswordET;

    /** SharedPreferences instance to retrieve stored data (e.g., authorization key) */
    SharedPreferences sp;

    /** Singleton instance for Retrofit API calls */
    RetroFitBuilder retroFitBuilder = RetroFitBuilder.getInstance();

    /**
     * Called when activity is created.
     * Enables edge-to-edge layout, sets the content view, and adjusts padding
     * to avoid system bars. Calls config() to initialize views and listeners.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable edge-to-edge layout for immersive UI
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_register);

        // Add padding for system bars (status and navigation bars)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        config();
    }

    /**
     * Initializes UI components and SharedPreferences,
     * then sets up button click listeners.
     */
    private void config() {
        sp = getSharedPreferences("FightBall", MODE_PRIVATE);

        cancelbt = findViewById(R.id.registerCancelbtt);
        saveBt = findViewById(R.id.registerSavebtt);
        usernameET = findViewById(R.id.inputuserRegister);
        passwordET = findViewById(R.id.inputpwdRegister);
        conPasswordET = findViewById(R.id.confirmpwdRegister);

        setListeners();
    }

    /**
     * Sets click listeners for Cancel and Save buttons.
     * Cancel closes the activity without action.
     * Save triggers the API call to register the user.
     */
    private void setListeners() {
        cancelbt.setOnClickListener(e -> finish());

        saveBt.setOnClickListener(e -> apiCall());
    }

    /**
     * Validates user input and makes an API call to register the new user.
     * Checks for empty username/password and password confirmation match.
     * On successful registration shows a Toast and closes the activity.
     * Displays error Toast messages on validation failure or API call failure.
     */
    private void apiCall() {
        boolean valid = true;

        // Create user model with entered username and password
        UserModel aux = new UserModel(0, usernameET.getText().toString(), passwordET.getText().toString());

        if (aux.getName().isEmpty()) {
            Toast.makeText(this, "Please enter a valid user name", Toast.LENGTH_LONG).show();
            valid = false;
        }

        if (aux.getPassword().isEmpty()) {
            Toast.makeText(this, "Please enter a valid password", Toast.LENGTH_LONG).show();
            valid = false;
        }

        if (!aux.getPassword().equals(conPasswordET.getText().toString())) {
            Toast.makeText(this, "The passwords don't match", Toast.LENGTH_LONG).show();
            valid = false;
        }

        if (valid) {
            // Make asynchronous API call to register user
            Call<RegisterResponse> register = retroFitBuilder.callApi().registerUser(sp.getString("key", ""), aux);
            register.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                    if (response.body() != null) {
                        if (response.code() == 200) {
                            Toast.makeText(RegisterActivity.this, "User Successfully Registered", Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, "Response was null", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {
                    Toast.makeText(RegisterActivity.this, "Response Failed", Toast.LENGTH_LONG).show();
                    System.out.println(t.getMessage());
                }
            });
        }
    }
}
