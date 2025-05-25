package com.example.fightball.Player;

import android.os.Bundle;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fightball.Adapters.CharacterAdapter;
import com.example.fightball.R;

/**
 * Activity for displaying the list of characters available to a player.
 * This screen uses a custom adapter to render each character item in a ListView.
 */
public class PlayerCharactersActivity extends AppCompatActivity {

    // Static adapter used to bind the character data to the ListView
    public static CharacterAdapter adapter;

    // ListView to visually display characters
    ListView CharacterVisual;

    /**
     * Called when the activity is created. Initializes the UI and sets up the character list.
     * @param savedInstanceState Bundle containing the activity’s previously saved state, if any.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_player_characters);

        // Set padding to avoid overlap with system bars (status and navigation)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set up character data and visual representation
        config();
    }

    /**
     * Configures the character list by:
     * - Creating a CharacterAdapter with character data from PlayerMainActivity.
     * - Assigning it to the ListView for display.
     */
    private void config() {
        adapter = new CharacterAdapter(this, R.layout.character_item, PlayerMainActivity.characters);
        CharacterVisual = findViewById(R.id.characterList);
        CharacterVisual.setAdapter(adapter);
    }
}
