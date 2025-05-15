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

public class EditTutorialActivity extends AppCompatActivity {

    EditText inputName, inputDesc;
    Button btnSave, btnCancel;

    SharedPreferences sp;
    RetroFitBuilder retroFitBuilder = RetroFitBuilder.getInstance();
    int position;
    TutorialModel selectedTutorial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tutorial);

        inputName = findViewById(R.id.inputNameTutorial);
        inputDesc = findViewById(R.id.inputDescpTutorial);
        btnSave = findViewById(R.id.savetutorialbt);
        btnCancel = findViewById(R.id.cancelEdiitutorialbt);

        sp = getSharedPreferences("FightBall", MODE_PRIVATE);
        int type = getIntent().getIntExtra("type", 0);

        if (type == 2) {
            configForPost();
        } else {
            configForPut();
        }

        btnCancel.setOnClickListener(v -> finish());
    }

    private void configForPut() {
        position = getIntent().getIntExtra("pos", 0);
        selectedTutorial = AdminMainActivity.tutorials.get(position);

        inputName.setText(selectedTutorial.getTitle());
        inputDesc.setText(selectedTutorial.getDescription());

        btnSave.setOnClickListener(v -> {
            if (!inputName.getText().toString().isEmpty()) {
                editCall();
            } else {
                Toast.makeText(this, "Title is required", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void configForPost() {
        btnSave.setOnClickListener(v -> {
            if (!inputName.getText().toString().isEmpty()) {
                postCall();
            } else {
                Toast.makeText(this, "Title is required", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void postCall() {
        String title = inputName.getText().toString();
        String description = inputDesc.getText().toString();

        TutorialModel tutorial = new TutorialModel(0, title, description);
        Call<TutorialModel> call = retroFitBuilder.callApi().createTutorial(sp.getString("key", ""), tutorial);
        call.enqueue(new Callback<TutorialModel>() {
            @Override
            public void onResponse(Call<TutorialModel> call, Response<TutorialModel> response) {
                AdminMainActivity.tutorials.add(response.body());
                AdminTutorialsActivity.adapter.notifyDataSetChanged();
                finish();
            }

            @Override
            public void onFailure(Call<TutorialModel> call, Throwable t) {
                System.out.println("Error al crear tutorial");
            }
        });
    }

    private void editCall() {
        int id = selectedTutorial.getId();
        String title = inputName.getText().toString();
        String description = inputDesc.getText().toString();

        TutorialModel updated = new TutorialModel(id, title, description);
        Call<TutorialModel> call = retroFitBuilder.callApi().editTutorial(sp.getString("key", ""), id, updated);
        call.enqueue(new Callback<TutorialModel>() {
            @Override
            public void onResponse(Call<TutorialModel> call, Response<TutorialModel> response) {
                AdminMainActivity.tutorials.set(position, updated);
                AdminTutorialsActivity.adapter.notifyDataSetChanged();
                finish();
            }

            @Override
            public void onFailure(Call<TutorialModel> call, Throwable t) {
                System.out.println("Error al editar tutorial");
            }
        });
    }
}
