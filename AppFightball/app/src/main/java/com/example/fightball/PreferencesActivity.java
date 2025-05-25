package com.example.fightball;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * Activity that allows users to configure preferences related to saving their
 * username and password. The preferences are stored using SharedPreferences.
 * Supports edge-to-edge layout with proper padding for system bars.
 */
public class PreferencesActivity extends AppCompatActivity {

    /** Checkbox to toggle saving the username */
    CheckBox saveUser;

    /** Checkbox to toggle saving the password */
    CheckBox savePasword;

    /** Button to cancel changes and close the activity */
    Button cancelbt;

    /** Button to save changes and close the activity */
    Button savebt;

    /** SharedPreferences instance for storing user preferences */
    SharedPreferences sp;

    /** Editor for modifying SharedPreferences */
    SharedPreferences.Editor editor;

    /**
     * Called when the activity is created.
     * Sets up edge-to-edge layout, inflates the view,
     * adjusts padding for system bars, and initializes the UI and listeners.
     *
     * @param savedInstanceState Bundle containing saved state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable edge-to-edge layout for immersive UI experience
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_preferences);

        // Adjust padding to accommodate system bars (status bar, navigation bar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        config();
    }

    /**
     * Configures UI elements by initializing SharedPreferences,
     * setting the initial states of checkboxes according to saved preferences,
     * and setting up button listeners.
     */
    private void config() {
        sp = getSharedPreferences("FightBall", MODE_PRIVATE);
        editor = sp.edit();

        savePasword = findViewById(R.id.rememberPasswordCheckBox);
        saveUser = findViewById(R.id.rememberUserCheckBox);
        cancelbt = findViewById(R.id.cancelPreferencesButton);
        savebt = findViewById(R.id.savePrefetencesbtt);

        // Restore saved states for password and user saving preferences
        if (sp.getBoolean("savePassword", false)) {
            savePasword.setChecked(true);
        }
        if (sp.getBoolean("saveUser", false)) {
            saveUser.setChecked(true);
        }

        setlisteners();
    }

    /**
     * Sets click listeners for the Cancel and Save buttons.
     * Cancel button closes the activity without saving changes.
     * Save button saves the checkbox states into SharedPreferences and closes the activity.
     */
    private void setlisteners() {
        cancelbt.setOnClickListener(e -> {
            // Close activity without saving changes
            finish();
        });

        savebt.setOnClickListener(e -> {
            // Save password preference
            editor.putBoolean("savePassword", savePasword.isChecked());

            // Save user preference
            editor.putBoolean("saveUser", saveUser.isChecked());

            editor.apply(); // Apply changes asynchronously
            finish();       // Close activity
        });
    }
}
