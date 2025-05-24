package com.example.fightball.Player;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
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
import com.example.fightball.Models.CharacterModel;
import com.example.fightball.Models.ItemModel;
import com.example.fightball.Models.MatchModel;
import com.example.fightball.PreferencesActivity;
import com.example.fightball.R;
import com.example.fightball.websocket.Ws_chatActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Main activity for players. Displays a summary of user statistics (matches won, lost, and total),
 * and provides access to character and item views.
 * It also initializes API calls to retrieve the necessary data, handles the toolbar menu,
 * and sets up a notification channel.
 */
public class PlayerMainActivity extends AppCompatActivity {

    // Notification channel ID used for Android notifications
    static String canalId = "777";

    // Text views for displaying match statistics and the username
    TextView partidasGanadas;
    TextView partidasPerdidas;
    TextView partidasTotales;
    TextView usernameText;

    // Buttons for navigating to different player sections
    Button verPartidasBoton;
    Button verPersonajes;
    Button verItems;

    // Static lists to hold user data accessible from other activities
    public static ArrayList<MatchModel> partidas;
    public static ArrayList<CharacterModel> characters;
    public static ArrayList<ItemModel> items;

    // Retrofit builder for performing API calls
    RetroFitBuilder retroFitBuilder = RetroFitBuilder.getInstance();
    SharedPreferences sp;

    /**
     * Lifecycle method called when the activity is created.
     * Initializes UI components and triggers data loading.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_player_main);

        // Adjust padding for system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        configurar(); // Main setup method
    }

    /**
     * Configures the views, listeners, notification channel and toolbar.
     * Also triggers API calls to load match, character, and item data.
     */
    public void configurar() {
        sp = getSharedPreferences("FightBall", MODE_PRIVATE);

        usernameText = findViewById(R.id.userNameText);
        usernameText.setText(sp.getString("username", ""));

        partidasGanadas = findViewById(R.id.pGanadasText);
        partidasPerdidas = findViewById(R.id.pPerdidas);
        partidasTotales = findViewById(R.id.pJugadasText);

        verPartidasBoton = findViewById(R.id.bttVerPartidas);
        verPersonajes = findViewById(R.id.bttVerPersonajes);
        verItems = findViewById(R.id.bttVerObjetos);

        setlisteners(); // Attach button event handlers
        createNotificationChannel(); // Set up notification channel

        Toolbar toolbar = findViewById(R.id.barraMenu);
        setSupportActionBar(toolbar);

        apicalls(); // Make all necessary API calls
    }

    /**
     * Sets listeners for navigation buttons to open other activities.
     */
    private void setlisteners() {
        verPartidasBoton.setOnClickListener(e -> {
            Intent intent = new Intent(this, PlayerMatchesActiviy.class);
            startActivity(intent);
        });

        verPersonajes.setOnClickListener(e -> {
            Intent intent = new Intent(this, PlayerCharactersActivity.class);
            startActivity(intent);
        });

        verItems.setOnClickListener(e -> {
            Intent intent = new Intent(this, PlayerItemActivity.class);
            startActivity(intent);
        });
    }

    /**
     * Triggers all API calls needed to populate the user interface with current data.
     */
    private void apicalls() {
        try {
            getAllUserMatches();
            getLostMatches();
            getWinnedMatches();
            getCharacters();
            getItems();
        } catch (Exception e) {
            System.out.println("General error: " + e.getMessage());
        }
    }

    /**
     * Loads the list of items via API and stores it in the static variable.
     */
    private void getItems() {
        Call<List<ItemModel>> getItems = retroFitBuilder.callApi().getItems(sp.getString("key", ""));
        getItems.enqueue(new Callback<List<ItemModel>>() {
            @Override
            public void onResponse(Call<List<ItemModel>> call, Response<List<ItemModel>> response) {
                items = (ArrayList<ItemModel>) response.body();
            }

            @Override
            public void onFailure(Call<List<ItemModel>> call, Throwable t) {
                // Log error if needed
            }
        });
    }

    /**
     * Loads the list of characters via API and stores it in the static variable.
     */
    private void getCharacters() {
        Call<List<CharacterModel>> totalcharacters = retroFitBuilder.callApi().getCharacters(sp.getString("key", ""));
        totalcharacters.enqueue(new Callback<List<CharacterModel>>() {
            @Override
            public void onResponse(Call<List<CharacterModel>> call, Response<List<CharacterModel>> response) {
                characters = (ArrayList<CharacterModel>) response.body();
            }

            @Override
            public void onFailure(Call<List<CharacterModel>> call, Throwable t) {
                // Log error if needed
            }
        });
    }

    /**
     * Loads all matches played by the user and updates the total match counter.
     */
    private void getAllUserMatches() {
        Call<List<MatchModel>> ptotal = retroFitBuilder.callApi().geMatchesByName(
                sp.getString("username", ""), sp.getString("key", ""));
        ptotal.enqueue(new Callback<List<MatchModel>>() {
            @Override
            public void onResponse(Call<List<MatchModel>> call, Response<List<MatchModel>> response) {
                if (response.body() != null) {
                    partidas = (ArrayList<MatchModel>) response.body();
                    partidasTotales.setText(String.valueOf(partidas.size()));
                } else {
                    partidas = new ArrayList<>();
                    partidasTotales.setText("No matches");
                }
            }

            @Override
            public void onFailure(Call<List<MatchModel>> call, Throwable t) {
                System.out.println("Error retrieving total matches: " + t.getMessage());
            }
        });
    }

    /**
     * Loads all matches the user has won and updates the respective UI field.
     */
    private void getWinnedMatches() {
        Call<List<MatchModel>> pganadas = retroFitBuilder.callApi().winMatchesbyName(
                sp.getString("username", ""), sp.getString("key", ""));
        pganadas.enqueue(new Callback<List<MatchModel>>() {
            @Override
            public void onResponse(Call<List<MatchModel>> call, Response<List<MatchModel>> response) {
                if (response.body() != null) {
                    partidasGanadas.setText(String.valueOf(response.body().size()));
                }
            }

            @Override
            public void onFailure(Call<List<MatchModel>> call, Throwable t) {
                System.out.println("Error retrieving won matches: " + t.getMessage());
            }
        });
    }

    /**
     * Loads all matches the user has lost and updates the respective UI field.
     */
    private void getLostMatches() {
        Call<List<MatchModel>> pperdidas = retroFitBuilder.callApi().lostMatchesbyName(
                sp.getString("username", ""), sp.getString("key", ""));
        pperdidas.enqueue(new Callback<List<MatchModel>>() {
            @Override
            public void onResponse(Call<List<MatchModel>> call, Response<List<MatchModel>> response) {
                if (response.body() != null) {
                    partidasPerdidas.setText(String.valueOf(response.body().size()));
                }
            }

            @Override
            public void onFailure(Call<List<MatchModel>> call, Throwable t) {
                System.out.println("Error retrieving lost matches: " + t.getMessage());
            }
        });
    }

    /**
     * Handles selection of toolbar menu items and navigates to appropriate activities.
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
     * Inflates the toolbar menu from XML.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mi_menu, menu);
        return true;
    }

    /**
     * Creates a notification channel required for Android 8.0+.
     * This is necessary to show notifications properly.
     */
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "nombre";
            String description = "descripcion";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(canalId, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
