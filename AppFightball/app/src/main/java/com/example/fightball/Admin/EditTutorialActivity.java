package com.example.fightball.Admin;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fightball.API.RetroFitBuilder;
import com.example.fightball.Models.TutorialModel;
import com.example.fightball.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activity for creating or editing a tutorial item.
 * Supports both POST (create) and PUT (edit) operations based on the intent extra "type".
 */
public class EditTutorialActivity extends AppCompatActivity {

    // UI elements
    EditText inputName, inputDesc;
    Button btnSave, btnCancel;

    // SharedPreferences for storing/retrieving user session or API key
    SharedPreferences sp;

    // Retrofit instance for API calls
    RetroFitBuilder retroFitBuilder = RetroFitBuilder.getInstance();

    // Position of the tutorial in the list (used for editing)
    int position;

    // The tutorial being edited
    TutorialModel selectedTutorial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tutorial);

        // Initialize UI elements
        inputName = findViewById(R.id.inputNameTutorial);
        inputDesc = findViewById(R.id.inputDescpTutorial);
        btnSave = findViewById(R.id.savetutorialbt);
        btnCancel = findViewById(R.id.cancelEdiitutorialbt);

        // Get SharedPreferences for auth token or other saved data
        sp = getSharedPreferences("FightBall", MODE_PRIVATE);

        // Determine if this activity is for creating (type == 2) or editing (type != 2)
        int type = getIntent().getIntExtra("type", 0);

        if (type == 2) {
            // Configure UI and behavior for creating a new tutorial
            configForPost();
        } else {
            // Configure UI and behavior for editing an existing tutorial
            configForPut();
        }

        // Cancel button simply finishes the activity without saving changes
        btnCancel.setOnClickListener(v -> finish());
    }

    /**
     * Configures the activity for editing an existing tutorial.
     * Loads tutorial data into UI fields and sets save button listener for update.
     */
    private void configForPut() {
        // Get the position of the tutorial in the list from intent extras
        position = getIntent().getIntExtra("pos", 0);
        // Retrieve the tutorial to edit
        selectedTutorial = AdminMainActivity.tutorials.get(position);

        // Populate UI fields with existing tutorial data
        inputName.setText(selectedTutorial.getTitle());
        inputDesc.setText(selectedTutorial.getDescription());

        // Set save button listener to update tutorial on API
        btnSave.setOnClickListener(v -> {
            if (!inputName.getText().toString().isEmpty()) {
                editCall();
            } else {
                Toast.makeText(this, "Title is required", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Configures the activity for creating a new tutorial.
     * Sets save button listener to post new tutorial.
     */
    private void configForPost() {
        // Set save button listener to create tutorial on API
        btnSave.setOnClickListener(v -> {
            if (!inputName.getText().toString().isEmpty()) {
                postCall();
            } else {
                Toast.makeText(this, "Title is required", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Sends a POST request to create a new tutorial using Retrofit.
     * On success, adds the new tutorial to the list and updates the UI.
     */
    private void postCall() {
        String title = inputName.getText().toString();
        String description = inputDesc.getText().toString();

        // Create new TutorialModel instance (id=0 for new)
        TutorialModel tutorial = new TutorialModel(0, title, description);

        // Make API call to create tutorial with user's key from SharedPreferences
        Call<TutorialModel> call = retroFitBuilder.callApi().createTutorial(sp.getString("key", ""), tutorial);
        call.enqueue(new Callback<TutorialModel>() {
            @Override
            public void onResponse(Call<TutorialModel> call, Response<TutorialModel> response) {
                // Add new tutorial to main list and notify adapter to refresh UI
                AdminMainActivity.tutorials.add(response.body());
                AdminTutorialsActivity.adapter.notifyDataSetChanged();
                finish(); // Close activity after success
            }

            @Override
            public void onFailure(Call<TutorialModel> call, Throwable t) {
                System.out.println("Error al crear tutorial");
            }
        });
    }

    /**
     * Sends a PUT request to update an existing tutorial using Retrofit.
     * On success, updates the tutorial in the list and refreshes the UI.
     */
    private void editCall() {
        int id = selectedTutorial.getId();
        String title = inputName.getText().toString();
        String description = inputDesc.getText().toString();

        // Create updated TutorialModel instance with existing id
        TutorialModel updated = new TutorialModel(id, title, description);

        // Make API call to edit tutorial with user's key from SharedPreferences
        Call<TutorialModel> call = retroFitBuilder.callApi().editTutorial(sp.getString("key", ""), id, updated);
        call.enqueue(new Callback<TutorialModel>() {
            @Override
            public void onResponse(Call<TutorialModel> call, Response<TutorialModel> response) {
                // Update tutorial in main list and notify adapter to refresh UI
                AdminMainActivity.tutorials.set(position, updated);
                AdminTutorialsActivity.adapter.notifyDataSetChanged();
                finish(); // Close activity after success
            }

            @Override
            public void onFailure(Call<TutorialModel> call, Throwable t) {
                System.out.println("Error al editar tutorial");
            }
        });
    }
}
