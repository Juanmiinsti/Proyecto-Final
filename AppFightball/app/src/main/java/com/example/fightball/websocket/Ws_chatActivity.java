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

/**
 * Activity for a WebSocket-based chat interface.
 * Handles connection to a WebSocket server, sending and receiving chat messages,
 * and updating the UI accordingly.
 */
public class Ws_chatActivity extends AppCompatActivity {

    /** Tag for logging purposes */
    private static final String TAG = "WS_Chat";

    /** SharedPreferences to retrieve stored user data (e.g., auth key, username) */
    private SharedPreferences sp;

    /** WebSocket client instance for real-time communication */
    private WebSocket webSocket;

    /** HTTP client used to create WebSocket connections */
    private OkHttpClient client;

    /** Send button UI element */
    private Button bt;

    /** Adapter to bind chat messages to the ListView */
    private ChatAdapter adapter;

    /** ListView UI element to display chat messages */
    private ListView chatVisual;

    /** List to store chat messages */
    private ArrayList<String> chatMessages = new ArrayList<>();

    /** EditText input for typing new chat messages */
    private EditText inputMensaje;

    /**
     * Called when the activity is created.
     * Sets up the UI, initializes the WebSocket connection, and sets listeners.
     *
     * @param savedInstanceState Saved instance state bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ws_chat);

        config();  // Initialize UI components and adapter

        inputMensaje = findViewById(R.id.editTextMessage);

        // Retrieve shared preferences to get user info and authentication key
        sp = getSharedPreferences("FightBall", MODE_PRIVATE);

        bt = findViewById(R.id.buttonSend);

        // Setup WebSocket connection to server using OkHttp
        configWebSocket();

        // Set click listener for the send button to send chat messages
        bt.setOnClickListener(e -> {
            if (!inputMensaje.getText().toString().isEmpty()) {
                sendMessage(inputMensaje.getText().toString());
            }
        });
    }

    /**
     * Configures UI elements and initializes the chat adapter for the ListView.
     */
    private void config() {
        chatVisual = findViewById(R.id.chatListView);
        adapter = new ChatAdapter(this, chatMessages);
        chatVisual.setAdapter(adapter);
    }

    /**
     * Establishes the WebSocket connection to the chat server with proper authentication.
     * Sets up WebSocket event callbacks for open, message received, closing, closed, and failure.
     */
    private void configWebSocket() {
        client = new OkHttpClient();

        // Build the WebSocket request with URL and authorization header
        Request request = new Request.Builder()
                .url("ws://54.164.115.171/chat")
                .addHeader("Authorization", sp.getString("key", ""))
                .build();

        // Define WebSocket event listener
        WebSocketListener listener = new WebSocketListener() {
            @Override
            public void onOpen(WebSocket ws, Response response) {
                Log.d(TAG, "WebSocket connection opened");
                webSocket = ws;
                // Optionally send an initial connection message
                ws.send("Conexión inicializada");
            }

            @Override
            public void onMessage(WebSocket ws, String text) {
                Log.d(TAG, "Message received: " + text);
                // Update UI on main thread when a message arrives
                runOnUiThread(() -> {
                    chatMessages.add(text);
                    adapter.notifyDataSetChanged();
                    // Scroll ListView to the last message
                    chatVisual.smoothScrollToPosition(chatMessages.size() - 1);
                });
            }

            @Override
            public void onClosing(WebSocket ws, int code, String reason) {
                Log.d(TAG, "WebSocket closing: " + reason);
                ws.close(1000, null);
            }

            @Override
            public void onClosed(WebSocket ws, int code, String reason) {
                Log.d(TAG, "WebSocket closed: " + reason);
            }

            @Override
            public void onFailure(WebSocket ws, Throwable t, Response response) {
                Log.e(TAG, "WebSocket error", t);
            }
        };

        // Start the WebSocket connection
        client.newWebSocket(request, listener);

        // Note: Do NOT shut down the executor service here; do it on activity destruction
        // client.dispatcher().executorService().shutdown();
    }

    /**
     * Sends a chat message through the WebSocket.
     * Prefixes the message with the username from SharedPreferences.
     *
     * @param message The plain text message to send
     */
    private void sendMessage(String message) {
        message = sp.getString("username", "") + ": " + message;
        if (webSocket != null) {
            webSocket.send(message);
        } else {
            Log.e(TAG, "WebSocket is not initialized yet");
        }
    }

    /**
     * Called when the activity is being destroyed.
     * Properly closes the WebSocket connection and shuts down the HTTP client.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webSocket != null) {
            webSocket.close(1000, "Activity destroyed");
        }
        if (client != null) {
            client.dispatcher().executorService().shutdown();
        }
    }
}
