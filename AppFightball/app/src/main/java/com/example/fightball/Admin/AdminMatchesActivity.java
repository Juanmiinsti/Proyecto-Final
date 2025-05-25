package com.example.fightball.Admin;

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
 * Activity that displays a list of all matches in the admin panel.
 * It uses a custom adapter to show match items in a ListView.
 */
public class AdminMatchesActivity extends AppCompatActivity {

    /** Adapter to manage and display the list of matches */
    public static MatchAdapter adapter;

    /** ListView UI element to show the matches */
    ListView partidasVisual;

    /**
     * Called when the activity is created.
     * Sets up the edge-to-edge UI and initializes the match list.
     * @param savedInstanceState previously saved state, if any
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable edge-to-edge display for immersive UI experience
        EdgeToEdge.enable(this);

        // Inflate the layout from XML
        setContentView(R.layout.activity_admin_matches);

        // Adjust padding to avoid overlap with system bars (status/navigation)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize the list view with matches
        config();
    }

    /**
     * Configuration method to setup the matches list.
     */
    private void config() {
        listofMatches();
    }

    /**
     * Initializes the ListView with the match data using MatchAdapter.
     * Uses the static list of matches from AdminMainActivity.
     */
    private void listofMatches() {
        // Create a new adapter using the list of matches
        adapter = new MatchAdapter(this, R.layout.match_item, AdminMainActivity.matches);

        // Find the ListView in the layout
        partidasVisual = findViewById(R.id.adminMatchesList);

        // Set the adapter to the ListView to display the data
        partidasVisual.setAdapter(adapter);
    }
}
