package com.example.fightball.Admin;

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
import com.example.fightball.Models.EnemyModel;
import com.example.fightball.Models.ItemModelAdmin;
import com.example.fightball.Models.MatchModel;
import com.example.fightball.Models.RoleModel;
import com.example.fightball.Models.TutorialModel;
import com.example.fightball.Models.UserModel;
import com.example.fightball.PreferencesActivity;
import com.example.fightball.R;
import com.example.fightball.websocket.Ws_chatActivity;

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

    Button btnVerItems;
    Button btnVerEnemigos;

    static public ArrayList<UserModel> users;
    static public ArrayList<RoleModel>roles;
    static public ArrayList<MatchModel>matches;
    static public ArrayList<ItemModelAdmin>items;
    static public ArrayList<EnemyModel>enemies;
    static public ArrayList<TutorialModel>tutorials;

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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if(item.getItemId()==R.id.acercaDe){
            Intent intentAcercade=new Intent(this, AboutActivity.class);
            startActivity(intentAcercade);
        } else if (item.getItemId()==R.id.preferenciasId) {
            Intent intentPreferencias =new Intent(this, PreferencesActivity.class);
            startActivity(intentPreferencias);
        }else if (item.getItemId()==R.id.joinChatId){
            Intent startChat=new Intent(this, Ws_chatActivity.class);
            startActivity(startChat);
        }
        return super.onOptionsItemSelected(item);
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
        btnVerItems=findViewById(R.id.bttveritemsAdmin);
        btnVerEnemigos=findViewById(R.id.bttVerEnemigos);

        Toolbar toolbar = findViewById(R.id.adminMenuBar);
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
            Intent intent=new Intent(this, AdminTutorialsActivity.class);
            startActivity(intent);
        });

        btnVerItems.setOnClickListener(e->{
            Intent intent=new Intent(this,AdminItemsActivity.class);
            startActivity(intent);
        });
        btnVerEnemigos.setOnClickListener(e->{
            Intent intent=new Intent(this,AdminEnemiesActivity.class);
            startActivity(intent);
        });


    }

    private void apicalls() {
        try {
            getallUsers();

            getMatches();

            getItems();

            getEnemies();


        } catch (Exception e) {
            System.out.println("error"+ e.getMessage());
        }
    }

    private void getEnemies() {
        Call<List<EnemyModel>>getEnemies=retroFitBuilder.callApi().getAllEnemies(sp.getString("key",""));
        getEnemies.enqueue(new Callback<List<EnemyModel>>() {
            @Override
            public void onResponse(Call<List<EnemyModel>> call, Response<List<EnemyModel>> response) {
                if (response.body()!=null){
                    enemies= (ArrayList<EnemyModel>) response.body();
                }
            }

            @Override
            public void onFailure(Call<List<EnemyModel>> call, Throwable t) {
                System.out.println("error en la llamada a getEnemiesAdmin");

            }
        });
    }

    private void getItems() {
        Call<List<ItemModelAdmin>> geItemscall =retroFitBuilder.callApi().getItemsAdmin(sp.getString("key",""));
        geItemscall.enqueue(new Callback<List<ItemModelAdmin>>() {
            @Override
            public void onResponse(Call<List<ItemModelAdmin>> call, Response<List<ItemModelAdmin>> response) {
                if (response.body() !=null){
                    items= (ArrayList<ItemModelAdmin>) response.body();
                }
            }

            @Override
            public void onFailure(Call<List<ItemModelAdmin>> call, Throwable t) {
                System.out.println("error en la llamada a getItemsAdmin");

            }
        });

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
                System.out.println("error en la llamada a getMatchesAdmin");
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