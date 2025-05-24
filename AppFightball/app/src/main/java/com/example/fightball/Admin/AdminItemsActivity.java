package com.example.fightball.Admin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fightball.API.RetroFitBuilder;
import com.example.fightball.Adapters.AdminItemAdapter;
import com.example.fightball.Models.ItemModelAdmin;
import com.example.fightball.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminItemsActivity extends AppCompatActivity {
    // Static adapter to manage the list of items and allow access elsewhere if needed
    public static AdminItemAdapter adapter;
    // ListView that displays the items
    ListView itemsVisual;
    // Button to trigger the creation of a new item
    Button create;
    // Retrofit instance for making API calls
    RetroFitBuilder retroFitBuilder = RetroFitBuilder.getInstance();
    // SharedPreferences to store session info like API keys
    SharedPreferences sp;

    /**
     * Called when the activity is created.
     * Enables edge-to-edge layout, sets content view, adjusts padding for system bars,
     * initializes components, and registers context menu on the ListView.
     * @param savedInstanceState saved instance state bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable edge-to-edge display for immersive UI
        EdgeToEdge.enable(this);

        // Set the XML layout for this activity
        setContentView(R.layout.activity_admin_items);

        // Adjust padding of main view according to system bars (status/navigation)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views, adapter and listeners
        config();

        // Register the ListView for context menu (long press actions)
        registerForContextMenu(itemsVisual);
    }

    /**
     * Configures SharedPreferences, initializes the list of items and the create button.
     * Sets click listener on the create button to open EditItemActivity in creation mode.
     */
    private void config() {
        // Retrieve SharedPreferences with name "FightBall"
        sp = getSharedPreferences("FightBall", MODE_PRIVATE);

        // Initialize and set adapter for the ListView
        listofItems();

        // Find the create button by ID
        create = findViewById(R.id.crearItemButton);

        // Set click listener to open EditItemActivity for creating new item (type=2)
        create.setOnClickListener(e -> {
            Intent intentEdit = new Intent(this, EditItemActivity.class);
            intentEdit.putExtra("type", 2); // 2 means creation mode
            startActivity(intentEdit);
        });
    }

    /**
     * Creates the context menu for the ListView items on long press.
     * Inflates the contextual menu with options such as delete or edit.
     * @param menu The context menu that is being built
     * @param v The view for which the context menu is being built
     * @param menuInfo Extra information about the item for which the menu is shown
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.menu_contextual_admin, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    /**
     * Handles the selection of a context menu item.
     * Performs deletion or editing of the selected item based on user's choice.
     * @param item The selected menu item
     * @return true if the event was handled, otherwise calls superclass implementation
     */
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        if (info == null) {
            // If no info about the selected item, delegate to superclass
            return super.onContextItemSelected(item);
        }

        try {
            // Get the selected item from the ListView by position
            ItemModelAdmin selectedItem = (ItemModelAdmin) itemsVisual.getItemAtPosition(info.position);

            if (selectedItem == null) {
                // Log error if the selected item is null (should not happen)
                Log.e("ContextMenu", "Selected item is null");
                return super.onContextItemSelected(item);
            }

            if (item.getItemId() == R.id.IdEliminar) {
                // If "Delete" option selected, call API to delete item
                deleteCall(selectedItem.getId(), info.position);
            } else {
                // Otherwise, open EditItemActivity in edit mode (type=1), passing position
                Intent intentEdit = new Intent(this, EditItemActivity.class);
                intentEdit.putExtra("type", 1); // 1 means edit mode
                intentEdit.putExtra("pos", info.position);
                startActivity(intentEdit);
            }
        } catch (Exception e) {
            // Catch any unexpected exceptions (should ideally log or handle properly)
        }

        return super.onContextItemSelected(item);
    }

    /**
     * Sends an asynchronous API call to delete the item by ID.
     * On success, removes the item from the local list and updates the adapter.
     * @param id The ID of the item to delete
     * @param pos The position of the item in the ListView
     */
    private void deleteCall(int id, int pos) {
        Call<Boolean> deleteItem = retroFitBuilder.callApi().deleteItem(sp.getString("key", ""), id);
        deleteItem.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.code() == 200) {
                    // Remove the item from the global list and notify adapter to refresh the UI
                    AdminMainActivity.items.remove(pos);
                    adapter.notifyDataSetChanged();
                    System.out.println("Changes notified");
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                System.out.println("Error trying to delete");
            }
        });
    }

    /**
     * Initializes the adapter and associates it with the ListView.
     * Loads the list of items from AdminMainActivity.
     */
    private void listofItems() {
        adapter = new AdminItemAdapter(this, R.layout.admin_item_layout, AdminMainActivity.items);
        itemsVisual = findViewById(R.id.adminItemsLists);
        itemsVisual.setAdapter(adapter);
    }
}
