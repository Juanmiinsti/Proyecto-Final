package com.example.fightball;

import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * Activity to play a video using VideoView with MediaController support.
 * The video playback starts automatically and shows a toast when finished.
 * Supports edge-to-edge layout and system bars padding.
 */
public class MediaPlayerActivity extends AppCompatActivity {

    /** VideoView used to display the video */
    private VideoView videoView;

    /** MediaController to provide playback controls (play, pause, seek, etc.) */
    private MediaController mediaController;

    /** A view used as an anchor for the MediaController controls */
    private View controllerSpacer;

    /**
     * Called when the activity is created.
     * Sets up edge-to-edge layout, inflates the layout,
     * adjusts padding for system bars, and configures video playback.
     *
     * @param savedInstanceState Bundle containing saved state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable edge-to-edge layout for immersive UI experience
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_media);

        // Adjust padding to accommodate system bars (status, navigation)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        configurar();
    }

    /**
     * Configures the VideoView, MediaController, and event listeners.
     * Sets the video URI, starts playback automatically when ready,
     * and displays a toast message upon video completion.
     */
    private void configurar() {
        videoView = findViewById(R.id.videoView);
        controllerSpacer = findViewById(R.id.controllerSpacer);

        // Initialize MediaController and anchor it to the spacer view
        mediaController = new MediaController(this);
        mediaController.setAnchorView(controllerSpacer);

        videoView.setMediaController(mediaController);

        // Set the video URI to a raw resource bundled in the app
        videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video));

        // Listener called when the video is ready to play
        videoView.setOnPreparedListener(mediaPlayer -> {
            mediaController.show(0);  // Show controls indefinitely when ready
            videoView.start();        // Start playback automatically
        });

        // Listener called when the video playback is completed
        videoView.setOnCompletionListener(mp -> {
            Toast.makeText(this, "Video finished", Toast.LENGTH_SHORT).show();
        });
    }

    /**
     * Called when the activity is destroyed.
     * Stops video playback and releases resources.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (videoView != null) {
            videoView.stopPlayback();
        }
    }

    /**
     * Handles touch events to toggle the visibility of the MediaController.
     * Shows the controller if hidden, hides it if visible when screen is touched.
     *
     * @param event The motion event
     * @return true if the event was handled, else calls superclass implementation
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (mediaController != null) {
                if (!mediaController.isShowing()) {
                    mediaController.show(0);  // Show indefinitely
                } else {
                    mediaController.hide();
                }
            }
        }
        return super.onTouchEvent(event);
    }
}
