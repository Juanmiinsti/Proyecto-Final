package com.example.fightball.Mod;

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
 * ModAllMatchesActivity
 *
 * This activity allows the moderator to view all registered matches
 * using a ListView populated with a custom adapter (MatchAdapter).
 * The match data is retrieved from the static list in ModMainActivity.
 */
public class ModAllMatchesActivity extends AppCompatActivity {

    // Static adapter used to populate the ListView with match data
    public static MatchAdapter adapter;

    // ListView that displays the matches
    ListView partidasVisual;

    /**
     * Called when the activity is first created. Sets up the UI and applies
     * safe edge-to-edge insets for modern full-screen layouts.
     *
     * @param savedInstanceState The saved state of the activity, if any.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mod_all_matches);

        // Applies system window insets (status bar, navigation bar) as padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        config();
    }

    /**
     * Initializes configuration for the activity.
     */
    private void config() {
        listofMatches();
    }

    /**
     * Sets up the ListView with match data using MatchAdapter.
     */
    private void listofMatches() {
        adapter = new MatchAdapter(this, R.layout.match_item, ModMainActivity.matches);
        partidasVisual = findViewById(R.id.modMatchesLists);
        partidasVisual.setAdapter(adapter);
    }
}
