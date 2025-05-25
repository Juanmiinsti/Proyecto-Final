package com.example.fightball.Admin;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fightball.API.RetroFitBuilder;
import com.example.fightball.Adapters.UserAdapterAdmin;
import com.example.fightball.Models.UserModel;
import com.example.fightball.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activity for administrative management of users in the FightBall app.
 *
 * Provides functionality to display a list of users, edit existing users, and delete users.
 * When a user is deleted, a system notification is shown.
 * Supports a contextual menu to perform actions on each user item.
 */
public class AdminUsersActivity extends AppCompatActivity {

    /** Adapter to display users in the ListView */
    public static UserAdapterAdmin adapter;

    /** ListView that holds the users list */
    ListView userVisual;

    /** Retrofit client instance for API calls */
    RetroFitBuilder retroFitBuilder = RetroFitBuilder.getInstance();

    /** SharedPreferences for persistent storage and retrieving auth tokens */
    SharedPreferences sp;

    /** Notification channel ID for Android Oreo and above */
    static String channelId = "777";

    /**
     * Called when the activity is created.
     * Sets up the view, enables Edge-to-Edge layout, and applies padding for system bars.
     * Initializes configuration and screen components.
     *
     * @param savedInstanceState Previously saved state, if any.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable Edge-to-Edge layout to allow content to extend into system bars
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_admin_users);

        // Adjust padding to avoid overlap with system bars (status bar, navigation bar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize configuration and UI components
        config();
    }

    /**
     * Sets up the user list, notification channel, and contextual menu.
     */
    private void config() {
        listOfUsers();                    // Load and display users in the list
        createNotificationChannel();      // Create notification channel for the app
        registerForContextMenu(userVisual); // Register context menu for the ListView
        sp = getSharedPreferences("FightBall", MODE_PRIVATE); // Initialize SharedPreferences
    }

    /**
     * Creates the context menu when the user performs a long press on a list item.
     *
     * @param menu     The context menu that will be shown.
     * @param v        The view for which the context menu is being built.
     * @param menuInfo Additional information about the context menu.
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.menu_contextual_admin, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    /**
     * Handles the selected options from the context menu (edit or delete user).
     *
     * @param item The selected context menu item.
     * @return true if the action was handled successfully, false otherwise.
     */
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if (info == null) {
            return super.onContextItemSelected(item);
        }

        try {
            UserModel selectedUser = (UserModel) userVisual.getItemAtPosition(info.position);
            if (selectedUser == null) {
                Log.e("ContextMenu", "Selected user is null");
                return super.onContextItemSelected(item);
            }
            int id = item.getItemId();

            if (id == R.id.IdEliminar) {
                // API call to delete user
                Call<Boolean> deleteUser = retroFitBuilder.callApi().deleteUser(sp.getString("key", ""), selectedUser.getId());
                deleteUser.enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if (response.code() == 200) {
                            // Remove user from local list and notify adapter
                            AdminMainActivity.users.remove(info.position);
                            adapter.notifyDataSetChanged();

                            // Show notification about user deletion
                            userDeletedNotification(selectedUser);
                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        Log.e("AdminUsersActivity", "Error deleting user: " + t.getMessage());
                    }
                });
            } else {
                // Intent to edit user, passing the item position
                Intent intentEditU = new Intent(this, EditUserActivity.class);
                intentEditU.putExtra("pos", info.position);
                startActivity(intentEditU);
            }
        } catch (Exception e) {
            Log.e("AdminUsersActivity", "General error in context menu: " + e.getMessage());
        }
        return true;
    }

    /**
     * Configures and assigns the adapter to display the users list.
     */
    private void listOfUsers() {
        adapter = new UserAdapterAdmin(this, R.layout.user_admin_item, AdminMainActivity.users);
        userVisual = findViewById(R.id.adminUsersList);
        userVisual.setAdapter(adapter);
    }

    /**
     * Creates the notification channel for Android Oreo (API 26) and above.
     * This is necessary for notifications to work properly on recent Android versions.
     */
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "name";        // Visible name of the channel
            String description = "description"; // Channel description
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelId, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    /**
     * Shows a notification informing that a user has been deleted.
     *
     * @param selectedUser The user that has been deleted.
     */
    private void userDeletedNotification(UserModel selectedUser) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(AdminUsersActivity.this, channelId)
                .setContentTitle("User Deleted")
                .setContentText(selectedUser.getName())
                .setSmallIcon(android.R.drawable.alert_dark_frame);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Ensure notification channel exists on API 26+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Channel", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        Notification notification = builder.build();
        notificationManager.notify(Integer.parseInt(channelId), notification);
    }
}
