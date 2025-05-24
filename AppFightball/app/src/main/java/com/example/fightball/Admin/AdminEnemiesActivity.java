package com.example.fightball.Admin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fightball.API.RetroFitBuilder;
import com.example.fightball.Adapters.EnemyAdapter;
import com.example.fightball.Models.EnemyModel;
import com.example.fightball.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminEnemiesActivity extends AppCompatActivity {

    // Static adapter for the enemy list to allow external access if needed
    public static EnemyAdapter adapter;
    // ListView to display the list of enemies
    ListView enemyListView;
    // Button to trigger creation of a new enemy
    Button createEnemyButton;
    // SharedPreferences to store/retrieve user/session data like API keys
    SharedPreferences sp;
    // Singleton instance of Retrofit builder to perform API calls
    RetroFitBuilder retroFitBuilder = RetroFitBuilder.getInstance();

    /**
     * Activity lifecycle method called when the activity is created.
     * Sets the layout and initializes components and context menu.
     * @param savedInstanceState - Bundle containing saved state if any.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_enemies);

        // Initialize UI components and configuration
        config();

        // Register ListView for context menu to provide options on long press
        registerForContextMenu(enemyListView);
    }

    /**
     * Helper method to initialize views, adapter, SharedPreferences and listeners.
     */
    private void config() {
        // Get SharedPreferences under name "FightBall" for storing session info
        sp = getSharedPreferences("FightBall", MODE_PRIVATE);

        // Find views by their IDs in the layout
        enemyListView = findViewById(R.id.adminEnemiesList);
        createEnemyButton = findViewById(R.id.createEnemyButton);

        // Initialize adapter with enemy list from AdminMainActivity and set to ListView
        adapter = new EnemyAdapter(this, R.layout.enemy_item, AdminMainActivity.enemies);
        enemyListView.setAdapter(adapter);

        // Set click listener for create button to open EditEnemyActivity in creation mode (type=2)
        createEnemyButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditEnemyActivity.class);
            intent.putExtra("type", 2); // 2 indicates creation mode
            startActivity(intent);
        });
    }

    /**
     * Creates the context menu for ListView items on long press.
     * Inflates a predefined menu resource with options such as delete and edit.
     * @param menu The context menu being built.
     * @param v The view for which the context menu is being built.
     * @param menuInfo Extra information about the item for which the menu is shown.
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        // Inflate menu resource into the context menu
        getMenuInflater().inflate(R.menu.menu_contextual_admin, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    /**
     * Handles selection of an item from the context menu.
     * Deletes or edits the selected enemy based on menu choice.
     * @param item The selected menu item.
     * @return true if handled, otherwise passes to superclass.
     */
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        // Retrieve info about the selected item from the context menu info
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if (info == null) return super.onContextItemSelected(item);

        // Get the enemy object at the selected position in the ListView
        EnemyModel selectedEnemy = (EnemyModel) enemyListView.getItemAtPosition(info.position);
        if (selectedEnemy == null) return super.onContextItemSelected(item);

        // If "Delete" option was selected in the menu
        if (item.getItemId() == R.id.IdEliminar) {
            // Call delete method with enemy ID and position
            deleteEnemy(selectedEnemy.getId(), info.position);
        } else {
            // Otherwise, open EditEnemyActivity in edit mode (type=1) passing the position
            Intent intent = new Intent(this, EditEnemyActivity.class);
            intent.putExtra("type", 1); // 1 indicates edit mode
            intent.putExtra("pos", info.position);
            startActivity(intent);
        }

        return true;
    }

    /**
     * Sends an API request to delete an enemy by ID.
     * On success, removes the enemy from the local list and updates the adapter.
     * @param id The ID of the enemy to delete.
     * @param position The position of the enemy in the ListView.
     */
    private void deleteEnemy(int id, int position) {
        // Make API call to delete enemy, passing API key and enemy ID
        Call<Boolean> call = retroFitBuilder.callApi().deleteEnemy(sp.getString("key", ""), id);
        call.enqueue(new Callback<Boolean>() {
            /**
             * Called when API response is received.
             * If successful (code 200), removes enemy locally and notifies adapter.
             */
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.code() == 200) {
                    // Remove enemy from the list in AdminMainActivity
                    AdminMainActivity.enemies.remove(position);
                    // Notify adapter that data has changed to refresh the ListView
                    adapter.notifyDataSetChanged();
                }
            }

            /**
             * Called when API request fails.
             * Logs an error message to the console.
             */
            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                System.out.println("Error deleting enemy");
            }
        });
    }
}
