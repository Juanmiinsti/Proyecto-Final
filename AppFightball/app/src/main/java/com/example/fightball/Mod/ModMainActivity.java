package com.example.fightball.Mod;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fightball.API.RetroFitBuilder;
import com.example.fightball.AboutActivity;
import com.example.fightball.Models.MatchModel;
import com.example.fightball.Models.RoleModel;
import com.example.fightball.Models.UserModel;
import com.example.fightball.PreferencesActivity;
import com.example.fightball.R;
import com.example.fightball.websocket.Ws_chatActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * ModMainActivity
 *
 * This activity serves as the main dashboard for moderators. It displays:
 * - Total number of users
 * - Available roles
 * - Total matches
 *
 * Moderators can navigate to view full user lists, roles, and matches,
 * and access chat, preferences, or about screens.
 */
public class ModMainActivity extends AppCompatActivity {

    // TextViews displaying summary data
    TextView userNameText;
    TextView partidasTotales;
    TextView rolesDisponibles;
    TextView usuariosTotales;

    // Buttons to navigate to detailed lists
    Button btverPTotales;
    Button btverRolesDisponibles;
    Button btVerUsariosTotales;

    // Static lists holding data fetched from the API
    public static ArrayList<UserModel> users;
    public static ArrayList<RoleModel> roles;
    public static ArrayList<MatchModel> matches;

    // API helper
    RetroFitBuilder retroFitBuilder = RetroFitBuilder.getInstance();

    // SharedPreferences for session data (e.g., username, API key)
    SharedPreferences sp;

    /**
     * Initializes the activity, UI components, and API data fetching.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mod_main);

        // Apply window insets (status/navigation bar handling)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        config();
    }

    /**
     * Inflates the top-right menu in the toolbar.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mi_menu, menu);
        return true;
    }

    /**
     * Configures UI components and initializes API data calls.
     */
    private void config() {
        sp = getSharedPreferences("FightBall", MODE_PRIVATE);

        userNameText = findViewById(R.id.usernameTextMod);
        userNameText.setText(sp.getString("username", ""));

        usuariosTotales = findViewById(R.id.uRegistradosModText);
        rolesDisponibles = findViewById(R.id.rDisponiblesText);
        partidasTotales = findViewById(R.id.ptotalesText);

        btverPTotales = findViewById(R.id.bttverPartidasMod);
        btverRolesDisponibles = findViewById(R.id.bttverRoles);
        btVerUsariosTotales = findViewById(R.id.bttVerUsuariosMod);

        Toolbar toolbar = findViewById(R.id.modMenuBar);
        setSupportActionBar(toolbar);

        apicalls();
        setListeners();
    }

    /**
     * Handles menu item selections (About, Preferences, Chat).
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.acercaDe) {
            startActivity(new Intent(this, AboutActivity.class));
        } else if (item.getItemId() == R.id.preferenciasId) {
            startActivity(new Intent(this, PreferencesActivity.class));
        } else if (item.getItemId() == R.id.joinChatId) {
            startActivity(new Intent(this, Ws_chatActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Sets click listeners for buttons to navigate to different moderator sections.
     */
    private void setListeners() {
        btverPTotales.setOnClickListener(e -> {
            startActivity(new Intent(this, ModAllMatchesActivity.class));
        });

        btVerUsariosTotales.setOnClickListener(e -> {
            startActivity(new Intent(this, ModUsersActivity.class));
        });

        btverRolesDisponibles.setOnClickListener(e -> {
            startActivity(new Intent(this, ModRoleActivity.class));
        });
    }

    /**
     * Initiates the API calls to fetch users, roles, and matches.
     */
    private void apicalls() {
        try {
            getallUsers();
            getRoles();
            getMatches();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Calls the API to fetch all matches and updates the view.
     */
    private void getMatches() {
        Call<List<MatchModel>> getMatches = retroFitBuilder.callApi().getALlMatches(sp.getString("key", ""));
        getMatches.enqueue(new Callback<List<MatchModel>>() {
            @Override
            public void onResponse(Call<List<MatchModel>> call, Response<List<MatchModel>> response) {
                if (response.body() != null) {
                    matches = (ArrayList<MatchModel>) response.body();
                    partidasTotales.setText(String.valueOf(response.body().size()));
                } else {
                    matches = new ArrayList<>();
                }
            }

            @Override
            public void onFailure(Call<List<MatchModel>> call, Throwable t) {
                System.out.println("Error fetching matches in ModMainActivity");
            }
        });
    }

    /**
     * Calls the API to fetch all roles and updates the view.
     */
    private void getRoles() {
        Call<List<RoleModel>> getRoles = retroFitBuilder.callApi().getAllRoles(sp.getString("key", ""));
        getRoles.enqueue(new Callback<List<RoleModel>>() {
            @Override
            public void onResponse(Call<List<RoleModel>> call, Response<List<RoleModel>> response) {
                if (response.body() != null) {
                    roles = (ArrayList<RoleModel>) response.body();
                    rolesDisponibles.setText(String.valueOf(response.body().size()));
                } else {
                    roles = new ArrayList<>();
                }
            }

            @Override
            public void onFailure(Call<List<RoleModel>> call, Throwable t) {
                System.out.println("Error fetching roles in ModMainActivity");
            }
        });
    }

    /**
     * Calls the API to fetch all users and updates the view.
     */
    private void getallUsers() {
        Call<List<UserModel>> getUsers = retroFitBuilder.callApi().getAllUsers(sp.getString("key", ""));
        getUsers.enqueue(new Callback<List<UserModel>>() {
            @Override
            public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
                if (response.body() != null) {
                    users = (ArrayList<UserModel>) response.body();
                    usuariosTotales.setText(String.valueOf(response.body().size()));
                } else {
                    users = new ArrayList<>();
                }
            }

            @Override
            public void onFailure(Call<List<UserModel>> call, Throwable t) {
                System.out.println("Error fetching users in ModMainActivity");
            }
        });
    }
}
