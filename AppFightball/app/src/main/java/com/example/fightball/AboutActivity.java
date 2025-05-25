package com.example.fightball;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * Activity that displays information about the app.
 * Includes a button to open a media player activity to watch a video.
 */
public class AboutActivity extends AppCompatActivity {

    /** Button to trigger video playback */
    Button botonVerVideo;

    /**
     * Called when the activity is created.
     * Enables edge-to-edge layout, sets the content view,
     * adjusts padding for system bars, and configures UI elements.
     *
     * @param savedInstanceState Bundle containing saved state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable edge-to-edge layout for immersive UI experience
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_about);

        // Adjust padding to accommodate system bars (status, navigation)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        config();
    }

    /**
     * Configures UI elements and their event listeners.
     * Sets up the button that starts MediaPlayerActivity when clicked.
     */
    private void config() {
        botonVerVideo = findViewById(R.id.verVideoBtn);

        // Set click listener to launch MediaPlayerActivity when the button is pressed
        botonVerVideo.setOnClickListener(e -> {
            Intent intentMedia = new Intent(this, MediaPlayerActivity.class);
            startActivity(intentMedia);
        });
    }
}
