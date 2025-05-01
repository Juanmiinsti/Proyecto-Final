package com.example.fightball;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fightball.API.RetroFitBuilder;
import com.example.fightball.Models.LoginModel;
import com.example.fightball.Player.PlayerMainActivity;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    RetroFitBuilder retroFitBuilder = RetroFitBuilder.getInstance();

    Button buttonLogin;
    TextView username;
    TextView password;

    SharedPreferences sp;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        buttonLogin = findViewById(R.id.loginButton);
        username = findViewById(R.id.inputUsername);
        password = findViewById(R.id.inputPassword);

        sp = getSharedPreferences("FightBall", MODE_PRIVATE);
        editor = sp.edit();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        config();
    }

    private void config() {

        retroFitBuilder.build("http://10.0.2.2:8080");

        buttonLogin.setOnClickListener(e -> {
            login();
            System.out.println(sp.getString("key", ""));
            if (!sp.getString("key", "").isEmpty()) {
                selectMode();
            }
        });

    }

    private void dialog() {

        String[] choices = {
                this.getString(R.string.Jugador),
                this.getString(R.string.Moderador),
                this.getString(R.string.Administrador)
        };
        AtomicInteger aux = new AtomicInteger();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setTitle(R.string.ElijaModo)
                .setPositiveButton(R.string.Aceptar, (dialog, which) -> {
                    int selectedIndex = aux.get()+1;
                    goToActivity(selectedIndex);

                })
                .setNegativeButton(R.string.Cancelar, (dialog, which) -> {

                })
                .setSingleChoiceItems(choices, 0, (dialog, which) -> {
                aux.set(which);
                });

        AlertDialog dialog = builder.create();
        dialog.show();

    }
    private void goToActivity(int aux){
        switch (aux){
            case 1:
                Intent playerMainActivity=new Intent(LoginActivity.this, PlayerMainActivity.class);
                startActivity(playerMainActivity);
                break;
            case 2:
                break;
            case 3:
                break;
        }

    }

    private void selectMode() {
        try {
            Call<List<Integer>> selectActivity =
                    retroFitBuilder.callApi().rolesIdsByname(
                            username.getText().toString(),
                            sp.getString("key", ""));


            selectActivity.enqueue(new Callback<List<Integer>>() {
                @Override
                public void onResponse(Call<List<Integer>> call, Response<List<Integer>> response) {
                    if (response.body() != null) {
                        if (response.body().size() == 1) {
                            Toast.makeText(LoginActivity.this, "Tiene un rol", Toast.LENGTH_SHORT).show();
                            goToActivity(response.body().get(0));
                        } else {
                            dialog();
                            Toast.makeText(LoginActivity.this, "Tiene muchos roles", Toast.LENGTH_SHORT).show();
                        }
                    }

                }

                @Override
                public void onFailure(Call<List<Integer>> call, Throwable t) {

                }
            });

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void login() {
        try {
            LoginModel aux = new LoginModel(
                    username.getText().toString(),
                    password.getText().toString()
            );

            Call<String> loginCall = retroFitBuilder.callApi().login(aux);

            loginCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        String token = "Bearer " + response.body();
                        editor.putString("key", token);
                        editor.apply();
                        Toast.makeText(LoginActivity.this, "Login OK", Toast.LENGTH_SHORT).show();


                    } else {
                        Toast.makeText(LoginActivity.this, "Login fallido", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    editor.putString("key", "");

                }
            });

        } catch (Exception ex) {
            Toast.makeText(LoginActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
