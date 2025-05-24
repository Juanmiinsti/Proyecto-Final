package com.example.fightball.Admin;

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
import com.example.fightball.Models.EnemyModel;
import com.example.fightball.Models.ItemModelAdmin;
import com.example.fightball.Models.MatchModel;
import com.example.fightball.Models.RoleModel;
import com.example.fightball.Models.TutorialModel;
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
 * Main activity for the FightBall admin panel.
 * Displays summary information about users, matches, tutorials, items, and enemies.
 * Allows navigation to specific admin sections.
 */
public class AdminMainActivity extends AppCompatActivity {

    // TextViews to display summary statistics
    TextView userNameText;
    TextView partidasTotales; // total matches
    TextView tutoriales;      // total tutorials
    TextView usuariosTotales; // total users

    // Buttons for navigating to detailed admin views
    Button btverPTotales;
    Button btverTutoriales;
    Button btVerUsariosTotales;
    Button btnVerItems;
    Button btnVerEnemigos;

    // Static lists to hold data fetched from the API
    static public ArrayList<UserModel> users;
    static public ArrayList<RoleModel> roles;
    static public ArrayList<MatchModel> matches;
    static public ArrayList<ItemModelAdmin> items;
    static public ArrayList<EnemyModel> enemies;
    static public ArrayList<TutorialModel> tutorials;

    // Singleton instance for Retrofit API calls
    RetroFitBuilder retroFitBuilder = RetroFitBuilder.getInstance();

    // SharedPreferences instance to store/retrieve user session data
    SharedPreferences sp;

    /**
     * Called when the activity is created.
     * Initializes the UI and loads data.
     * @param savedInstanceState saved instance state bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable edge-to-edge layout for immersive UI
        EdgeToEdge.enable(this);

        // Set the layout from XML
        setContentView(R.layout.activity_admin_main);

        // Apply padding to avoid overlapping system bars (status/navigation bars)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Setup views, listeners, and API calls
        config();
    }

    /**
     * Inflates the menu into the toolbar.
     * @param menu the menu to inflate
     * @return true if the menu is displayed
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mi_menu, menu);
        return true;
    }

    /**
     * Handles toolbar menu item clicks.
     * @param item selected menu item
     * @return true if the event was handled
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if(item.getItemId() == R.id.acercaDe){
            // Open About screen
            Intent intentAbout = new Intent(this, AboutActivity.class);
            startActivity(intentAbout);
        } else if (item.getItemId() == R.id.preferenciasId) {
            // Open Preferences screen
            Intent intentPreferences = new Intent(this, PreferencesActivity.class);
            startActivity(intentPreferences);
        } else if (item.getItemId() == R.id.joinChatId){
            // Open WebSocket chat screen
            Intent startChat = new Intent(this, Ws_chatActivity.class);
            startActivity(startChat);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Configures the UI components, toolbar, listeners, and triggers data loading.
     */
    private void config() {
        // Get SharedPreferences instance to retrieve stored values
        sp = getSharedPreferences("FightBall", MODE_PRIVATE);

        // Initialize TextViews and set username from preferences
        userNameText = findViewById(R.id.adminUsernameText);
        userNameText.setText(sp.getString("username",""));

        usuariosTotales = findViewById(R.id.uTotalesadmin);
        tutoriales = findViewById(R.id.tutoTotalesAdminText);
        partidasTotales = findViewById(R.id.pTotalesAdmintext);

        // Initialize buttons
        btverPTotales = findViewById(R.id.bttPartidasAdmin);
        btverTutoriales = findViewById(R.id.bttTutorialesAdmin);
        btVerUsariosTotales = findViewById(R.id.bttUsuariosAdmin);
        btnVerItems = findViewById(R.id.bttveritemsAdmin);
        btnVerEnemigos = findViewById(R.id.bttVerEnemigos);

        // Setup the toolbar
        Toolbar toolbar = findViewById(R.id.adminMenuBar);
        setSupportActionBar(toolbar);

        // Call APIs to load data
        apicalls();

        // Set button click listeners
        setListeners();
    }

    /**
     * Sets click listeners for all navigation buttons.
     * Starts corresponding admin activities on button press.
     */
    private void setListeners() {
        btverPTotales.setOnClickListener(e -> {
            Intent intent = new Intent(this, AdminMatchesActivity.class);
            startActivity(intent);
        });

        btVerUsariosTotales.setOnClickListener(e -> {
            Intent intent = new Intent(this, AdminUsersActivity.class);
            startActivity(intent);
        });

        btverTutoriales.setOnClickListener(e -> {
            Intent intent = new Intent(this, AdminTutorialsActivity.class);
            startActivity(intent);
        });

        btnVerItems.setOnClickListener(e -> {
            Intent intent = new Intent(this, AdminItemsActivity.class);
            startActivity(intent);
        });

        btnVerEnemigos.setOnClickListener(e -> {
            Intent intent = new Intent(this, AdminEnemiesActivity.class);
            startActivity(intent);
        });
    }

    /**
     * Makes API calls to load users, matches, items, and enemies.
     * Exceptions are caught and logged.
     */
    private void apicalls() {
        try {
            getallUsers();
            getMatches();
            getItems();
            getEnemies();
        } catch (Exception e) {
            System.out.println("Error in API calls: " + e.getMessage());
        }
    }

    /**
     * Retrieves all enemies from the backend API.
     * Updates the static enemies list on success.
     */
    private void getEnemies() {
        Call<List<EnemyModel>> getEnemies = retroFitBuilder.callApi().getAllEnemies(sp.getString("key",""));
        getEnemies.enqueue(new Callback<List<EnemyModel>>() {
            @Override
            public void onResponse(Call<List<EnemyModel>> call, Response<List<EnemyModel>> response) {
                if (response.body() != null) {
                    enemies = (ArrayList<EnemyModel>) response.body();
                }
            }

            @Override
            public void onFailure(Call<List<EnemyModel>> call, Throwable t) {
                System.out.println("Error in getEnemies API call");
            }
        });
    }

    /**
     * Retrieves all items for admin from the backend API.
     * Updates the static items list on success.
     */
    private void getItems() {
        Call<List<ItemModelAdmin>> getItemsCall = retroFitBuilder.callApi().getItemsAdmin(sp.getString("key",""));
        getItemsCall.enqueue(new Callback<List<ItemModelAdmin>>() {
            @Override
            public void onResponse(Call<List<ItemModelAdmin>> call, Response<List<ItemModelAdmin>> response) {
                if (response.body() != null) {
                    items = (ArrayList<ItemModelAdmin>) response.body();
                }
            }

            @Override
            public void onFailure(Call<List<ItemModelAdmin>> call, Throwable t) {
                System.out.println("Error in getItemsAdmin API call");
            }
        });
    }

    /**
     * Retrieves all matches from the backend API.
     * Updates the static matches list and the matches count TextView.
     */
    private void getMatches() {
        Call<List<MatchModel>> getMatches = retroFitBuilder.callApi().getALlMatches(sp.getString("key",""));
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
                System.out.println("Error in getMatchesAdmin API call");
            }
        });
    }

    /**
     * Retrieves all users from the backend API.
     * Updates the static users list and the users count TextView.
     */
    private void getallUsers() {
        Call<List<UserModel>> getUsers = retroFitBuilder.callApi().getAllUsers(sp.getString("key",""));
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
                System.out.println("Error in getUsersMod API call");
            }
        });
    }
}
