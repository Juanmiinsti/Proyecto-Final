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
import com.example.fightball.Adapters.TutorialAdapter;
import com.example.fightball.Models.TutorialModel;
import com.example.fightball.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * AdminTutorialsActivity manages the list of tutorials in the admin panel.
 * It allows viewing, creating, editing, and deleting tutorials via a context menu.
 */
public class AdminTutorialsActivity extends AppCompatActivity {

    /** Adapter for displaying tutorial items in the ListView */
    public static TutorialAdapter adapter;

    /** ListView UI element to show tutorials */
    ListView tutorialListView;

    /** Button to create a new tutorial */
    Button createTutorialButton;

    /** Retrofit instance to handle API calls */
    RetroFitBuilder retroFitBuilder = RetroFitBuilder.getInstance();

    /** SharedPreferences to store user session data like API keys */
    SharedPreferences sp;

    /**
     * Called when the activity is created.
     * Sets up edge-to-edge display, inflates the layout,
     * configures UI elements, and registers context menu for the ListView.
     * @param savedInstanceState saved instance state bundle, if any
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable edge-to-edge layout for better immersive UI
        EdgeToEdge.enable(this);

        // Set the layout from XML resource
        setContentView(R.layout.activity_admin_tutorials);

        // Apply system bar insets as padding to avoid overlapping UI
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views and API calls
        config();

        // Register the ListView for a context menu to handle edit/delete actions
        registerForContextMenu(tutorialListView);
    }

    /**
     * Setup method to initialize UI components and load tutorials.
     * Sets click listener for the "create tutorial" button.
     */
    private void config() {
        sp = getSharedPreferences("FightBall", MODE_PRIVATE);

        // Initialize ListView
        tutorialListView = findViewById(R.id.tutorialList);

        // Load and display the list of tutorials from the backend API
        listTutorials();

        // Setup the button to create new tutorials
        createTutorialButton = findViewById(R.id.nuevoTutorialbt);
        createTutorialButton.setOnClickListener(v -> {
            // Start EditTutorialActivity in create mode (type=2)
            Intent intent = new Intent(this, EditTutorialActivity.class);
            intent.putExtra("type", 2); // 2 means "create"
            startActivity(intent);
        });
    }

    /**
     * Makes an asynchronous API call to retrieve the list of tutorials.
     * On successful response, updates the ListView with the data.
     */
    private void listTutorials() {
        Call<List<TutorialModel>> call = retroFitBuilder.callApi().getTutorials(sp.getString("key", ""));
        call.enqueue(new Callback<List<TutorialModel>>() {
            @Override
            public void onResponse(Call<List<TutorialModel>> call, Response<List<TutorialModel>> response) {
                if (response.code() == 200) {
                    List<TutorialModel> tutorials = response.body();

                    // Save tutorials list globally for admin access
                    AdminMainActivity.tutorials = (ArrayList<TutorialModel>) tutorials;

                    // Create adapter with the retrieved tutorials
                    adapter = new TutorialAdapter(AdminTutorialsActivity.this, R.layout.tutorial_item, tutorials);

                    // Set the adapter to the ListView to display tutorials
                    tutorialListView = findViewById(R.id.tutorialList);
                    tutorialListView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<TutorialModel>> call, Throwable t) {
                // Log error if API call fails
                Log.e("Tutorials", "Failed to fetch tutorials", t);
            }
        });
    }

    /**
     * Creates the context menu shown when long-pressing on a tutorial item.
     * Provides options to edit or delete the selected tutorial.
     * @param menu the context menu being built
     * @param v the view for which the context menu is being built
     * @param menuInfo extra information about the context menu
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.menu_contextual_admin, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    /**
     * Handles the selection of an item in the context menu.
     * Either deletes the tutorial or opens the edit screen depending on the choice.
     * @param item the selected menu item
     * @return boolean indicating whether the event was handled
     */
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if (info == null) return super.onContextItemSelected(item);

        // Get the selected tutorial based on the position in the ListView
        TutorialModel selectedTutorial = (TutorialModel) tutorialListView.getItemAtPosition(info.position);

        if (item.getItemId() == R.id.IdEliminar) {
            // Delete tutorial if "Delete" option selected
            deleteTutorial(selectedTutorial.getId(), info.position);
        } else {
            // Open edit tutorial activity if "Edit" option selected
            Intent intent = new Intent(this, EditTutorialActivity.class);
            intent.putExtra("type", 1); // 1 means "edit"
            intent.putExtra("pos", info.position);
            startActivity(intent);
        }

        return super.onContextItemSelected(item);
    }

    /**
     * Makes an API call to delete the tutorial with the given ID.
     * On success, removes it from the list and updates the adapter.
     * @param id the ID of the tutorial to delete
     * @param pos the position of the tutorial in the ListView
     */
    private void deleteTutorial(int id, int pos) {
        Call<Boolean> deleteCall = retroFitBuilder.callApi().deleteTutorial(sp.getString("key", ""), id);
        deleteCall.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.code() == 200) {
                    // Remove tutorial from global list and notify adapter
                    AdminMainActivity.tutorials.remove(pos);
                    adapter.notifyDataSetChanged();
                    Log.d("Tutorials", "Tutorial deleted and list updated");
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                // Log error if deletion fails
                Log.e("Tutorials", "Error deleting tutorial", t);
            }
        });
    }
}
