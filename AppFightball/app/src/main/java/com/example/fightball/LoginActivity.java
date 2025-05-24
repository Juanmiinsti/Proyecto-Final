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
import com.example.fightball.Admin.AdminMainActivity;
import com.example.fightball.Mod.ModMainActivity;
import com.example.fightball.Models.LoginModel;
import com.example.fightball.Player.PlayerMainActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activity responsible for handling user login and role-based navigation
 * in the FightBall application.
 *
 * Depending on the authenticated user's roles, it will navigate to:
 * - PlayerMainActivity
 * - ModMainActivity
 * - AdminMainActivity
 */
public class LoginActivity extends AppCompatActivity {

    // Retrofit instance for making API calls
    RetroFitBuilder retroFitBuilder = RetroFitBuilder.getInstance();

    // UI Components
    Button buttonLogin;
    Button buttonRegister;
    TextView username;
    TextView password;

    // SharedPreferences for storing session data (token, username)
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        config();
    }

    /**
     * Configures Retrofit and click listeners.
     */
    protected void config() {
        sp = getSharedPreferences("FightBall", MODE_PRIVATE);
        editor = sp.edit();

        buttonLogin = findViewById(R.id.loginButton);
        buttonRegister=findViewById(R.id.registerButton);
        username = findViewById(R.id.inputUsername);
        password = findViewById(R.id.inputPassword);


        if (sp.getBoolean("saveUser",false)){
            username.setText(sp.getString("username",""));
        }
        if (sp.getBoolean("savePassword",false)){
            password.setText(sp.getString("password",""));
        }

        retroFitBuilder.build("http://54.164.115.171");
        buttonLogin.setOnClickListener(e -> login());
        buttonRegister.setOnClickListener(e ->{
            Intent intent=new Intent(this,RegisterActivity.class);
            startActivity(intent);
        });
    }

    /**
     * Displays a dynamic dialog allowing user to choose between their available roles.
     * Admins (role 3) can access all roles.
     *
     * @param roles Real list of role IDs from the backend.
     */
    protected void dialog(List<Integer> roles) {
        String[] roleNames = new String[]{"Jugador", "Moderador", "Administrador"};

        // Si el usuario es Admin, puede elegir todos los roles
        List<Integer> rolesToShow = new ArrayList<>();
        if (roles.contains(3)) {
            rolesToShow.add(1);
            rolesToShow.add(2);
            rolesToShow.add(3);
        } else {
            rolesToShow.addAll(roles);
        }

        // Convertir IDs a nombres de roles para mostrar
        String[] choices = rolesToShow.stream()
                .map(roleId -> roleNames[roleId - 1])
                .toArray(String[]::new);

        AtomicInteger selected = new AtomicInteger(0);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.ElijaModo)
                .setSingleChoiceItems(choices, 0, (dialog, which) -> selected.set(which))
                .setPositiveButton(R.string.Aceptar, (dialog, which) -> {
                    int selectedRoleId = rolesToShow.get(selected.get());
                    goToActivity(selectedRoleId);
                })
                .setNegativeButton(R.string.Cancelar, (dialog, which) -> {
                    // Cancelar = no hacer nada
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Navega a la actividad correspondiente según el rol.
     *
     * @param roleId ID del rol (1=Jugador, 2=Moderador, 3=Admin)
     */
    protected void goToActivity(int roleId) {
        switch (roleId) {
            case 1:
                startActivity(new Intent(LoginActivity.this, PlayerMainActivity.class));
                break;
            case 2:
                startActivity(new Intent(LoginActivity.this, ModMainActivity.class));
                break;
            case 3:
                startActivity(new Intent(LoginActivity.this, AdminMainActivity.class));
                break;
            default:
                Toast.makeText(this, "Rol desconocido: " + roleId, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Obtiene roles del usuario tras el login.
     * Si tiene un solo rol (y no es admin), lo redirige.
     * Si tiene múltiples roles o es admin, abre diálogo para elegir.
     */
    protected void selectMode() {
        try {
            Call<List<Integer>> selectActivity = retroFitBuilder.callApi().rolesIdsByname(
                    username.getText().toString(),
                    sp.getString("key", "")
            );

            selectActivity.enqueue(new Callback<List<Integer>>() {
                @Override
                public void onResponse(Call<List<Integer>> call, Response<List<Integer>> response) {
                    List<Integer> roles = response.body();

                    if (roles != null) {
                        if (roles.size() == 1 && !roles.contains(3)) {
                            // Un solo rol y NO es admin
                            goToActivity(roles.get(0));
                        } else {
                            // Es admin o tiene múltiples roles
                            dialog(roles);
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "No se encontraron roles", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<Integer>> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "Error al obtener roles: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

        } catch (Exception e) {
            Toast.makeText(LoginActivity.this, "Excepción: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Intenta iniciar sesión con la API.
     * Si es exitoso, guarda el token y procede a seleccionar rol.
     */
    public void login() {
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
                        editor.putString("username", username.getText().toString());
                        if (sp.getBoolean("savePassword",false)){
                            editor.putString("password",password.getText().toString());
                        }
                        editor.apply();

                        Toast.makeText(LoginActivity.this, "Login OK", Toast.LENGTH_SHORT).show();
                        selectMode();
                    } else {
                        editor.putString("username", "");
                        editor.putString("key", "");
                        editor.apply();
                        Toast.makeText(LoginActivity.this, "Login fallido", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

        } catch (Exception ex) {
            Toast.makeText(LoginActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
