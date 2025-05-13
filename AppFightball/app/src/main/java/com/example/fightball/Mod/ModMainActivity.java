package com.example.fightball.Mod;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fightball.API.RetroFitBuilder;
import com.example.fightball.AboutActivity;
import com.example.fightball.Models.MatchModel;
import com.example.fightball.Models.RoleModel;
import com.example.fightball.Models.UserModel;
import com.example.fightball.Player.PlayerMatchesActiviy;
import com.example.fightball.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModMainActivity extends AppCompatActivity {

    TextView userNameText;
    TextView partidasTotales;
    TextView rolesDisponibles;
    TextView usuariosTotales;


    Button btverPTotales;
    Button btverRolesDisponibles;
    Button btVerUsariosTotales;

    static public ArrayList<UserModel>users;
    static public ArrayList<RoleModel>roles;
    static public ArrayList<MatchModel>matches;


    RetroFitBuilder retroFitBuilder = RetroFitBuilder.getInstance();
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mod_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        config();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mi_menu, menu);
        return true;
    }
    private void config() {
        sp = getSharedPreferences("FightBall", MODE_PRIVATE);

        userNameText=findViewById(R.id.usernameTextMod);
        userNameText.setText(sp.getString("username",""));

        usuariosTotales=findViewById(R.id.uRegistradosModText);
        rolesDisponibles=findViewById(R.id.rDisponiblesText);
        partidasTotales=findViewById(R.id.ptotalesText);

        btverPTotales=findViewById(R.id.bttverPartidasMod);
        btverRolesDisponibles=findViewById(R.id.bttverRoles);
        btVerUsariosTotales=findViewById(R.id.bttVerUsuariosMod);


        Toolbar toolbar = findViewById(R.id.modMenuBar);
        setSupportActionBar(toolbar);

        apicalls();

        setListeners();


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if(item.getItemId()==R.id.acercaDe){
         Intent intentAcercade=new Intent(this, AboutActivity.class);
         startActivity(intentAcercade);
        }
        return super.onOptionsItemSelected(item);
    }

    private void setListeners() {
        btverPTotales.setOnClickListener(e ->{
            Intent intent=new Intent(this, ModAllMatchesActivity.class);
            startActivity(intent);
        });

        btVerUsariosTotales.setOnClickListener(e ->{
            Intent intent=new Intent(this, ModUsersActivity.class);
            startActivity(intent);
        });

        btverRolesDisponibles.setOnClickListener(e ->{
            Intent intent=new Intent(this, ModRoleActivity.class);
            startActivity(intent);
        });


    }

    private void apicalls() {
        try {
            getallUsers();
            getRoles();
            getMatches();

        } catch (Exception e) {
            System.out.println("error"+ e.getMessage());
        }
    }

    private void getMatches() {

        Call<List<MatchModel>>getMatches =retroFitBuilder.callApi().getALlMatches(sp.getString("key",""));
        getMatches.enqueue(new Callback<List<MatchModel>>() {
            @Override
            public void onResponse(Call<List<MatchModel>> call, Response<List<MatchModel>> response) {
                if (response.body()!=null){
                    matches= (ArrayList<MatchModel>) response.body();
                    partidasTotales.setText(String.valueOf(response.body().size()));
                }else{
                    matches=new ArrayList<>();
                }
            }

            @Override
            public void onFailure(Call<List<MatchModel>> call, Throwable t) {
                System.out.println("error en la llamada a getMatchesMod");
            }
        });

    }

    private void getRoles() {
        Call<List<RoleModel>>getRoles =retroFitBuilder.callApi().getAllRoles(sp.getString("key",""));
        getRoles.enqueue(new Callback<List<RoleModel>>() {
            @Override
            public void onResponse(Call<List<RoleModel>> call, Response<List<RoleModel>> response) {
                if (response.body()!=null){
                    roles= (ArrayList<RoleModel>) response.body();
                    rolesDisponibles.setText(String.valueOf(response.body().size()));
                }else{
                    roles=new ArrayList<>();
                }
            }

            @Override
            public void onFailure(Call<List<RoleModel>> call, Throwable t) {
                System.out.println("error en la llamada a getRolesMod");

            }
        });

    }

    private void getallUsers() {

        Call<List<UserModel>>getusers =retroFitBuilder.callApi().getAllUsers(sp.getString("key",""));
        getusers.enqueue(new Callback<List<UserModel>>() {
            @Override
            public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
                if (response.body()!=null){
                    users= (ArrayList<UserModel>) response.body();
                    usuariosTotales.setText(String.valueOf(response.body().size()));
                }else{
                    users=new ArrayList<>();
                }
            }

            @Override
            public void onFailure(Call<List<UserModel>> call, Throwable t) {
                System.out.println("error en la llamada a getUsersMod");
            }
        });
    }


}