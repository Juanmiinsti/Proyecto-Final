package com.example.fightball.Player;

import android.os.Bundle;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fightball.Adapters.MatchAdapter;
import com.example.fightball.R;

/**
 * Activity that displays a list of matches played by the user.
 * Uses a ListView with a custom MatchAdapter to show match information.
 */
public class PlayerMatchesActiviy extends AppCompatActivity {

    /** Static adapter that manages the display of matches in the list. */
    public static MatchAdapter adapter;

    /** ListView UI element to display the list of matches. */
    ListView partidasVisual;

    /**
     * Called when the activity is first created.
     * Sets up the UI layout and applies edge-to-edge design with proper insets padding.
     *
     * @param savedInstanceState Previously saved state of the activity, if any.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable edge-to-edge layout for modern immersive UI
        EdgeToEdge.enable(this);

        // Set the layout resource for this activity
        setContentView(R.layout.activity_player_matches_activiy);

        // Adjust padding to avoid system bars (status/navigation)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Configure the ListView and adapter
        config();
    }

    /**
     * Configures the ListView by setting up the adapter with match data.
     */
    private void config() {
        listofMatches();
    }

    /**
     * Initializes the MatchAdapter with the list of matches from PlayerMainActivity
     * and sets it on the ListView to display the matches.
     */
    private void listofMatches() {
        adapter = new MatchAdapter(this, R.layout.match_item, PlayerMainActivity.partidas);
        partidasVisual = findViewById(R.id.PartidasList);
        partidasVisual.setAdapter(adapter);
    }
}
