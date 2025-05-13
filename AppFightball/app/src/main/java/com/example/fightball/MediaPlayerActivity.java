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

public class MediaPlayerActivity extends AppCompatActivity {
    private VideoView videoView;
    private MediaController mediaController;
    private View controllerSpacer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_media);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        configurar();
    }


    private void configurar() {

        videoView = findViewById(R.id.videoView);
        controllerSpacer = findViewById(R.id.controllerSpacer);

        mediaController = new MediaController(this);
        mediaController.setAnchorView(controllerSpacer); // Ancla al espacio reservado

        videoView.setMediaController(mediaController);

        videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video));

        videoView.setOnPreparedListener(mediaPlayer -> {
            mediaController.show(0); // Mostrar el controlador al estar listo
            videoView.start(); // Iniciar la reproducción automáticamente
        });


        videoView.setOnCompletionListener(mp -> {
            Toast.makeText(this, "Video terminado", Toast.LENGTH_SHORT).show();
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Liberar los recursos del VideoView
        if (videoView != null) {
            videoView.stopPlayback();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (mediaController != null) {
                if (!mediaController.isShowing()) {
                    mediaController.show(0);
                } else {
                    mediaController.hide();
                }
            }
        }
        return super.onTouchEvent(event);
    }
}