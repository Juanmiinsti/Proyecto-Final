package com.example.fightball;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fightball.API.RetroFitBuilder;
import com.example.fightball.Admin.AdminMainActivity;
import com.example.fightball.Mod.ModMainActivity;
import com.example.fightball.Models.LoginModel;
import com.example.fightball.Player.PlayerMainActivity;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activity responsible for handling user login and role-based navigation
 * in the FightBall application.
 *
 * Depending on the authenticated user's roles, it will navigate to:
 * - PlayerMainActivity
 * - ModMainActivity
 * - AdminMainActivity
 */
public class LoginActivity extends AppCompatActivity {

    // Retrofit instance for making API calls
    RetroFitBuilder retroFitBuilder = RetroFitBuilder.getInstance();

    // UI Components
    Button buttonLogin;
    TextView username;
    TextView password;

    // SharedPreferences for storing session data (token, username)
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // Enables edge-to-edge UI rendering
        setContentView(R.layout.activity_login);

        // Initialize UI components
        buttonLogin = findViewById(R.id.loginButton);
        username = findViewById(R.id.inputUsername);
        password = findViewById(R.id.inputPassword);

        // SharedPreferences setup
        sp = getSharedPreferences("FightBall", MODE_PRIVATE);
        editor = sp.edit();

        // Handle window insets to ensure padding where system bars exist
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize logic
        config();
    }

    /**
     * Configures the Retrofit base URL and sets up button click listeners.
     *
     */
    protected void config() {
        // Set base URL (local emulator)
        retroFitBuilder.build("http://54.164.115.171");

        // Handle login button click
        buttonLogin.setOnClickListener(e -> {
            login();
        });
    }

    /**
     * Displays a role selection dialog when the user has multiple roles.
     */
    protected void dialog() {
        // Available roles to choose from
        String[] choices = {
                this.getString(R.string.Jugador),
                this.getString(R.string.Moderador),
                this.getString(R.string.Administrador)
        };

        // AtomicInteger to hold selected index (thread-safe)
        AtomicInteger aux = new AtomicInteger();

        // Build and show the role selection dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setTitle(R.string.ElijaModo)
                .setPositiveButton(R.string.Aceptar, (dialog, which) -> {
                    int selectedIndex = aux.get() + 1;
                    goToActivity(selectedIndex);
                })
                .setNegativeButton(R.string.Cancelar, (dialog, which) -> {
                    // Do nothing on cancel
                })
                .setSingleChoiceItems(choices, 0, (dialog, which) -> {
                    aux.set(which);
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Navigates to the corresponding activity based on the user's role ID.
     *
     * @param aux Role ID (1 = Player, 2 = Moderator, 3 = Admin)
     */
    protected void goToActivity(int aux) {
        switch (aux) {
            case 1:
                startActivity(new Intent(LoginActivity.this, PlayerMainActivity.class));
                break;
            case 2:
                startActivity(new Intent(LoginActivity.this, ModMainActivity.class));
                break;
            case 3:
                startActivity(new Intent(LoginActivity.this, AdminMainActivity.class));
                break;
        }
    }

    /**
     * After login, fetches user roles and navigates accordingly.
     * If multiple roles exist, it prompts the user to select one.
     */
    protected void selectMode() {
        try {
            // Call to fetch role IDs using username and saved token
            Call<List<Integer>> selectActivity = retroFitBuilder.callApi().rolesIdsByname(
                    username.getText().toString(),
                    sp.getString("key", "")
            );

            selectActivity.enqueue(new Callback<List<Integer>>() {
                @Override
                public void onResponse(Call<List<Integer>> call, Response<List<Integer>> response) {
                    if (response.body() != null) {
                        if (response.body().size() == 1) {
                            Toast.makeText(LoginActivity.this, "Tiene un rol", Toast.LENGTH_SHORT).show();
                            goToActivity(response.body().get(0));
                        } else {
                            dialog(); // Prompt role selection if multiple
                            Toast.makeText(LoginActivity.this, "Tiene muchos roles", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<Integer>> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "Error al obtener roles: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

        } catch (Exception e) {
            Toast.makeText(LoginActivity.this, "Excepción: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Attempts to log in the user using the API.
     * On success, saves the token and triggers role selection.
     */
    public void login() {
        try {
            // Create login payload
            LoginModel aux = new LoginModel(
                    username.getText().toString(),
                    password.getText().toString()
            );

            // Make login API call
            Call<String> loginCall = retroFitBuilder.callApi().login(aux);

            loginCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        // Save token and username in shared preferences
                        String token = "Bearer " + response.body();
                        editor.putString("key", token);
                        editor.putString("username", username.getText().toString());
                        editor.apply();
                        Toast.makeText(LoginActivity.this, "Login OK", Toast.LENGTH_SHORT).show();

                        // Proceed to role selection
                        selectMode();
                    } else {
                        // Clear session on failure
                        editor.putString("username", "");
                        editor.putString("key", "");
                        editor.apply();
                        Toast.makeText(LoginActivity.this, "Login fallido", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

        } catch (Exception ex) {
            Toast.makeText(LoginActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
