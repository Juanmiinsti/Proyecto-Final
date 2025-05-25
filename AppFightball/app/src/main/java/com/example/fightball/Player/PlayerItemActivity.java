package com.example.fightball.Player;

import android.os.Bundle;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fightball.Adapters.ItemAdapter;
import com.example.fightball.R;

/**
 * Activity that displays the list of items owned or accessible by the player.
 * This uses a custom adapter to render each item in a ListView.
 */
public class PlayerItemActivity extends AppCompatActivity {

    // Static adapter that binds item data to the ListView
    public static ItemAdapter adapter;

    // ListView used to display the item list
    ListView itemsvisual;

    /**
     * Called when the activity is starting. Initializes the UI and populates the item list.
     * @param savedInstanceState If the activity is being re-initialized after previously
     * being shut down, this contains the data it most recently supplied.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_player_item);

        // Ensures content doesn't overlap with system UI like the status or navigation bar
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set up the item list
        config();
    }

    /**
     * Sets up the item ListView by:
     * - Initializing the adapter with the list of items from PlayerMainActivity.
     * - Binding the adapter to the ListView to display the items.
     */
    private void config() {
        adapter = new ItemAdapter(this, R.layout.item_item, PlayerMainActivity.items);
        itemsvisual = findViewById(R.id.itemList);
        itemsvisual.setAdapter(adapter);
    }
}
