package com.example.fightball.Admin;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fightball.API.RetroFitBuilder;
import com.example.fightball.Models.EnemyModel;
import com.example.fightball.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activity to create or edit an enemy in the FightBall app.
 *
 * This activity allows the admin user to either add a new enemy (POST)
 * or modify an existing enemy (PUT) depending on the "type" extra passed in the intent.
 *
 * It provides input fields for enemy name, health, and damage, and buttons to save or cancel.
 * API calls are made using Retrofit to send the changes to the backend.
 */
public class EditEnemyActivity extends AppCompatActivity {

    /** Input field for the enemy's name */
    EditText enemyName;

    /** Input field for the enemy's maximum health */
    EditText enemyHealth;

    /** Input field for the enemy's damage */
    EditText enemyDamage;

    /** Button to save the changes */
    Button btnSave;

    /** Button to cancel and close the activity */
    Button btnCancel;

    /** SharedPreferences instance to retrieve stored data (e.g. API key) */
    SharedPreferences sp;

    /** Retrofit instance for API calls */
    RetroFitBuilder retroFitBuilder = RetroFitBuilder.getInstance();

    /** Position of the enemy in the list (used when editing) */
    int position;

    /** Currently selected enemy (used when editing) */
    EnemyModel selectedEnemy;

    /**
     * Called when the activity is created.
     * Initializes the layout and configures the screen based on the intent extras.
     * @param savedInstanceState Saved state bundle (if any)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_enemy);
        config();
    }

    /**
     * Configures the UI components and behavior of the activity.
     * Determines whether to configure for creating a new enemy (POST) or editing (PUT).
     */
    private void config() {
        sp = getSharedPreferences("FightBall", MODE_PRIVATE);

        enemyName = findViewById(R.id.inputEnemyName);
        enemyHealth = findViewById(R.id.inputEnemyHealth);
        enemyDamage = findViewById(R.id.inputEnemyDamage);
        btnSave = findViewById(R.id.saveEnemyEdit);
        btnCancel = findViewById(R.id.cancelEnemyEdit);

        btnCancel.setOnClickListener(v -> finish());

        int type = getIntent().getIntExtra("type", 0);
        if (type == 2) {
            configForPost();
        } else if (type == 1) {
            configForPut();
        }
    }

    /**
     * Configures the activity for editing an existing enemy (PUT request).
     * Loads the enemy data into the input fields and sets the save button to update the enemy.
     */
    private void configForPut() {
        position = getIntent().getIntExtra("pos", 0);
        selectedEnemy = AdminMainActivity.enemies.get(position);

        enemyName.setText(selectedEnemy.getName());
        enemyHealth.setText(String.valueOf(selectedEnemy.getMax_health()));
        enemyDamage.setText(String.valueOf(selectedEnemy.getDamage()));

        btnSave.setOnClickListener(v -> {
            try {
                if (!enemyName.getText().toString().isEmpty()) {
                    editCall();
                } else {
                    Toast.makeText(this, "Name is required", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Configures the activity for creating a new enemy (POST request).
     * Sets the save button to send a create request with the entered data.
     */
    private void configForPost() {
        btnSave.setOnClickListener(v -> {
            try {
                if (!enemyName.getText().toString().isEmpty()) {
                    postCall();
                } else {
                    Toast.makeText(this, "Name is required", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Sends a POST request to create a new enemy with the data entered by the user.
     * On success, updates the enemy list and closes the activity.
     */
    private void postCall() {
        String name = enemyName.getText().toString();
        int health = Integer.parseInt(enemyHealth.getText().toString());
        int damage = Integer.parseInt(enemyDamage.getText().toString());

        EnemyModel newEnemy = new EnemyModel(0, name, health, damage);
        Call<EnemyModel> call = retroFitBuilder.callApi().createEnemy(sp.getString("key", ""), newEnemy);
        call.enqueue(new Callback<EnemyModel>() {
            @Override
            public void onResponse(Call<EnemyModel> call, Response<EnemyModel> response) {
                AdminMainActivity.enemies.add(response.body());
                AdminEnemiesActivity.adapter.notifyDataSetChanged();
                finish();
            }

            @Override
            public void onFailure(Call<EnemyModel> call, Throwable t) {
                System.out.println("Error creating enemy");
            }
        });
    }

    /**
     * Sends a PUT request to update an existing enemy with the modified data.
     * On success, updates the enemy list and closes the activity.
     */
    private void editCall() {
        int id = selectedEnemy.getId();
        String name = enemyName.getText().toString();
        int health = Integer.parseInt(enemyHealth.getText().toString());
        int damage = Integer.parseInt(enemyDamage.getText().toString());

        EnemyModel updatedEnemy = new EnemyModel(id, name, health, damage);
        Call<EnemyModel> call = retroFitBuilder.callApi().editEnemy(sp.getString("key", ""), id, updatedEnemy);
        call.enqueue(new Callback<EnemyModel>() {
            @Override
            public void onResponse(Call<EnemyModel> call, Response<EnemyModel> response) {
                AdminMainActivity.enemies.set(position, updatedEnemy);
                AdminEnemiesActivity.adapter.notifyDataSetChanged();
                finish();
            }

            @Override
            public void onFailure(Call<EnemyModel> call, Throwable t) {
                System.out.println("Error editing enemy");
            }
        });
    }
}
