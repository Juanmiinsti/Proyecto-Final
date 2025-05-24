package com.example.fightball.Mod;

import android.os.Bundle;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fightball.Adapters.RoleAdapter;
import com.example.fightball.R;

/**
 * Activity responsible for displaying the list of available roles to the moderator.
 * It uses a custom adapter (RoleAdapter) to populate the ListView with role data.
 */
public class ModRoleActivity extends AppCompatActivity {

    // Static adapter reference for external access if needed.
    public static RoleAdapter adapter;

    // ListView to display the list of roles.
    ListView roleVisual;

    /**
     * Called when the activity is first created.
     * Initializes the layout and loads the role data into the ListView.
     * @param savedInstanceState Bundle with the saved state of the activity (if any).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Enables fullscreen edge-to-edge display.
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mod_role);

        // Adjust padding to avoid overlap with system UI elements.
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Load and display the list of roles.
        config();
    }

    /**
     * Configures the activity by populating the list of roles.
     */
    private void config() {
        listofroles();
    }

    /**
     * Sets up the ListView with the RoleAdapter using the roles obtained
     * from the ModMainActivity static reference.
     */
    private void listofroles() {
        adapter = new RoleAdapter(this, R.layout.role_item, ModMainActivity.roles);
        roleVisual = findViewById(R.id.modRolesList);
        roleVisual.setAdapter(adapter);
    }
}
