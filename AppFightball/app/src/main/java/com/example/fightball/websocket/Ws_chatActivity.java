package com.example.fightball.websocket;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fightball.Adapters.ChatAdapter;
import com.example.fightball.R;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class Ws_chatActivity extends AppCompatActivity {
    private static final String TAG = "WS_Chat";
    private SharedPreferences sp;
    private WebSocket webSocket;
    private OkHttpClient client;
    private Button bt;
    private ChatAdapter adapter;
    private ListView chatVisual;
    private ArrayList<String> chatMessages=new ArrayList<>();
    private EditText inputMensaje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ws_chat);
        config();
        inputMensaje=findViewById(R.id.editTextMessage);
        sp = getSharedPreferences("FightBall", MODE_PRIVATE);
        bt = findViewById(R.id.buttonSend);

        configWebSocket();  // Usa OkHttp

        bt.setOnClickListener(e -> {
            if (!(inputMensaje.getText().toString().isEmpty())){
                sendMessage(inputMensaje.getText().toString());
            }
        });
    }

    private void config() {
        chatVisual=findViewById(R.id.chatListView);
        adapter=new ChatAdapter(this,chatMessages);
        chatVisual.setAdapter(adapter);
    }

    private void configWebSocket() {
        client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("ws://10.0.2.2:8080/chat")
                .addHeader("Authorization", sp.getString("key", ""))
                .build();

        WebSocketListener listener = new WebSocketListener() {
            @Override
            public void onOpen(WebSocket ws, Response response) {
                Log.d(TAG, "Conexión WebSocket abierta");
                webSocket = ws;
                ws.send("Conexión inicializada");
            }

            @Override
            public void onMessage(WebSocket ws, String text) {
                Log.d(TAG, "Mensaje recibido: " + text);
                runOnUiThread(() -> {
                    chatMessages.add(text);
                    adapter.notifyDataSetChanged();
                    chatVisual.smoothScrollToPosition(chatMessages.size()-1);

                });
            }

            @Override
            public void onClosing(WebSocket ws, int code, String reason) {
                Log.d(TAG, "Conexión cerrándose: " + reason);
                ws.close(1000, null);
            }

            @Override
            public void onClosed(WebSocket ws, int code, String reason) {
                Log.d(TAG, "Conexión cerrada: " + reason);
            }

            @Override
            public void onFailure(WebSocket ws, Throwable t, Response response) {
                Log.e(TAG, "Error en WebSocket", t);
            }
        };

        client.newWebSocket(request, listener);
        // client.dispatcher().executorService().shutdown(); // No cerrar aquí, solo al destruir la actividad
    }

    private void sendMessage(String message) {
        message=sp.getString("username","")+": "+message;
        if (webSocket != null) {
            webSocket.send(message);
        } else {
            Log.e(TAG, "WebSocket no inicializado aún");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webSocket != null) {
            webSocket.close(1000, "Actividad destruida");
        }
        if (client != null) {
            client.dispatcher().executorService().shutdown();
        }
    }
}
