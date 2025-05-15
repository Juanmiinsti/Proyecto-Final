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

public class AdminTutorialsActivity extends AppCompatActivity {


    public static TutorialAdapter adapter;
    ListView tutorialListView;
    Button createTutorialButton;
    RetroFitBuilder retroFitBuilder = RetroFitBuilder.getInstance();
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_tutorials);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        config();
        registerForContextMenu(tutorialListView);
    }

    private void config() {
        sp = getSharedPreferences("FightBall", MODE_PRIVATE);
        tutorialListView=findViewById(R.id.tutorialList);
        listTutorials();

        createTutorialButton = findViewById(R.id.nuevoTutorialbt);
        createTutorialButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditTutorialActivity.class);
            intent.putExtra("type", 2); // Crear
            startActivity(intent);
        });
    }

    private void listTutorials() {
        Call<List<TutorialModel>> call = retroFitBuilder.callApi().getTutorials(sp.getString("key", ""));
        call.enqueue(new Callback<List<TutorialModel>>() {
            @Override
            public void onResponse(Call<List<TutorialModel>> call, Response<List<TutorialModel>> response) {
                if (response.code() == 200) {
                    List<TutorialModel> tutorials = response.body();
                    AdminMainActivity.tutorials = (ArrayList<TutorialModel>) tutorials; // Asegúrate de tener esta lista pública en AdminMainActivity
                    adapter = new TutorialAdapter(AdminTutorialsActivity.this, R.layout.tutorial_item, tutorials);
                    tutorialListView = findViewById(R.id.tutorialList);
                    tutorialListView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<TutorialModel>> call, Throwable t) {
                Log.e("Tutorials", "Fallo al obtener tutoriales", t);
            }
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

        TutorialModel selectedTutorial = (TutorialModel) tutorialListView.getItemAtPosition(info.position);

        if (item.getItemId() == R.id.IdEliminar) {
            deleteTutorial(selectedTutorial.getId(), info.position);
        } else {
            Intent intent = new Intent(this, EditTutorialActivity.class);
            intent.putExtra("type", 1); // Editar
            intent.putExtra("pos", info.position);
            startActivity(intent);
        }

        return super.onContextItemSelected(item);
    }

    private void deleteTutorial(int id, int pos) {
        Call<Boolean> deleteCall = retroFitBuilder.callApi().deleteTutorial(sp.getString("key", ""), id);
        deleteCall.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.code() == 200) {
                    AdminMainActivity.tutorials.remove(pos);
                    adapter.notifyDataSetChanged();
                    Log.d("Tutorials", "Tutorial eliminado y cambios notificados");
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.e("Tutorials", "Error al eliminar el tutorial", t);
            }
        });
    }
}
