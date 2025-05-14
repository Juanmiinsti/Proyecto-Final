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

    public static EnemyAdapter adapter;
    ListView enemyListView;
    Button createEnemyButton;
    SharedPreferences sp;
    RetroFitBuilder retroFitBuilder = RetroFitBuilder.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_enemies);
        config();
        registerForContextMenu(enemyListView);
    }

    private void config() {
        sp = getSharedPreferences("FightBall", MODE_PRIVATE);
        enemyListView = findViewById(R.id.adminEnemiesList);
        createEnemyButton = findViewById(R.id.createEnemyButton);

        adapter = new EnemyAdapter(this, R.layout.enemy_item, AdminMainActivity.enemies);
        enemyListView.setAdapter(adapter);

        createEnemyButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditEnemyActivity.class);
            intent.putExtra("type", 2);
            startActivity(intent);
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.menu_contextual_admin, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if (info == null) return super.onContextItemSelected(item);

        EnemyModel selectedEnemy = (EnemyModel) enemyListView.getItemAtPosition(info.position);
        if (selectedEnemy == null) return super.onContextItemSelected(item);

        if (item.getItemId() == R.id.IdEliminar) {
            deleteEnemy(selectedEnemy.getId(), info.position);
        } else {
            Intent intent = new Intent(this, EditEnemyActivity.class);
            intent.putExtra("type", 1);
            intent.putExtra("pos", info.position);
            startActivity(intent);
        }

        return true;
    }

    private void deleteEnemy(int id, int position) {
        Call<Boolean> call = retroFitBuilder.callApi().deleteEnemy(sp.getString("key", ""), id);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.code() == 200) {
                    AdminMainActivity.enemies.remove(position);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                System.out.println("Error al eliminar enemigo");
            }
        });
    }
}
