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

public class EditEnemyActivity extends AppCompatActivity {

    EditText enemyName, enemyHealth, enemyDamage;
    Button btnSave, btnCancel;

    SharedPreferences sp;
    RetroFitBuilder retroFitBuilder = RetroFitBuilder.getInstance();
    int position;
    EnemyModel selectedEnemy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_enemy);
        config();
    }

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
                System.out.println("Error al crear enemigo");
            }
        });
    }

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
                System.out.println("Error al editar enemigo");
            }
        });
    }
}
