package com.example.fightball.websocket;

import static android.content.ContentValues.TAG;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fightball.R;


import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

public class Ws_chatActivity extends AppCompatActivity {
    Button bt;
    SharedPreferences sp;

    private StompClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ws_chat);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        configWebsocket();
        sp = getSharedPreferences("FightBall", MODE_PRIVATE);
        bt=findViewById(R.id.buttonprueba);


        bt.setOnClickListener(e ->{
            sendMessage();
        });
    }

    private void configWebsocket() {



    }

    public void sendMessage(){

    }



}