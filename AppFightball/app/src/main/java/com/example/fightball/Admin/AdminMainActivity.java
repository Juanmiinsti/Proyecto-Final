package com.example.fightball.Admin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fightball.API.RetroFitBuilder;
import com.example.fightball.Mod.ModAllMatchesActivity;
import com.example.fightball.Mod.ModRoleActivity;
import com.example.fightball.Mod.ModUsersActivity;
import com.example.fightball.Models.MatchModel;
import com.example.fightball.Models.RoleModel;
import com.example.fightball.Models.UserModel;
import com.example.fightball.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminMainActivity extends AppCompatActivity {

    TextView userNameText;
    TextView partidasTotales;
    TextView tutoriales;
    TextView usuariosTotales;


    Button btverPTotales;
    Button btverTutoriales;
    Button btVerUsariosTotales;

    static public ArrayList<UserModel> users;
    static public ArrayList<RoleModel>roles;
    static public ArrayList<MatchModel>matches;

    RetroFitBuilder retroFitBuilder = RetroFitBuilder.getInstance();
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_main);
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

        userNameText=findViewById(R.id.adminUsernameText);
        userNameText.setText(sp.getString("username",""));

        usuariosTotales=findViewById(R.id.uTotalesadmin);
        tutoriales=findViewById(R.id.tutoTotalesAdminText);
        partidasTotales=findViewById(R.id.pTotalesAdmintext);

        btverPTotales=findViewById(R.id.bttPartidasAdmin);
        btverTutoriales=findViewById(R.id.bttTutorialesAdmin);
        btVerUsariosTotales=findViewById(R.id.bttUsuariosAdmin);


        Toolbar toolbar = findViewById(R.id.barraMenu);
        setSupportActionBar(toolbar);

        apicalls();

        setListeners();


    }

    private void setListeners() {
        btverPTotales.setOnClickListener(e ->{
            Intent intent=new Intent(this, AdminMatchesActivity.class);
            startActivity(intent);
        });

        btVerUsariosTotales.setOnClickListener(e ->{
            Intent intent=new Intent(this, AdminUsersActivity.class);
            startActivity(intent);
        });

        btverTutoriales.setOnClickListener(e ->{

        });


    }

    private void apicalls() {
        try {
            getallUsers();

            getMatches();

        } catch (Exception e) {
            System.out.println("error"+ e.getMessage());
        }
    }

    private void getMatches() {

        Call<List<MatchModel>> getMatches =retroFitBuilder.callApi().getALlMatches(sp.getString("key",""));
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