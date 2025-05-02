package com.example.fightball.Player;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
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
import com.example.fightball.Models.CharacterModel;
import com.example.fightball.Models.ItemModel;
import com.example.fightball.Models.MatchModel;
import com.example.fightball.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayerMainActivity extends AppCompatActivity {
    static String canalId="777";
    TextView partidasGanadas;
    TextView partidasPerdidas;
    TextView partidasTotales;
    TextView usernameText;
    TextView personajeMasUsado;
    Button verPartidasBoton;
    Button verPersonajes;

    Button verItems;
    public static ArrayList<MatchModel> partidas;
    public static ArrayList<CharacterModel>characters;
    public static ArrayList<ItemModel>items;


    RetroFitBuilder retroFitBuilder = RetroFitBuilder.getInstance();
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_player_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        configurar();


    }
    public void configurar() {
        sp = getSharedPreferences("FightBall", MODE_PRIVATE);

        usernameText=findViewById(R.id.userNameText);
        usernameText.setText(sp.getString("username",""));

        partidasGanadas=findViewById(R.id.pGanadasText);
        partidasPerdidas=findViewById(R.id.pPerdidas);
        partidasTotales=findViewById(R.id.pJugadasText);

        verPartidasBoton=findViewById(R.id.bttVerPartidas);
        verPersonajes=findViewById(R.id.bttVerPersonajes);
        verItems=findViewById(R.id.bttVerObjetos);

        setlisteners();

        createNotificationChannel();
        Toolbar toolbar = findViewById(R.id.barraMenu);
        setSupportActionBar(toolbar);

        apicalls();


    }

    private void setlisteners(){
        verPartidasBoton.setOnClickListener(e->{
            Intent intent=new Intent(this,PlayerMatchesActiviy.class);
            startActivity(intent);
        });

        verPersonajes.setOnClickListener(e->{
            Intent intent=new Intent(this, PlayerCharactersActivity.class);
            startActivity(intent);
        });

        verItems.setOnClickListener(e ->{
            Intent intent=new Intent(this, PlayerItemActivity.class);
            startActivity(intent);
        });
    }
    private void apicalls() {
        try {
            getAllUserMatches();
            getLostMatches();
            getWinnedMatches();
            getCharacters();
            getItems();


        }catch (Exception e){
            System.out.println("erroe general "+e.getMessage());
        }
    }

    private void getItems() {

        Call<List<ItemModel>>getItems=retroFitBuilder.callApi().getItems(sp.getString("key",""));
        getItems.enqueue(new Callback<List<ItemModel>>() {
            @Override
            public void onResponse(Call<List<ItemModel>> call, Response<List<ItemModel>> response) {
                items= (ArrayList<ItemModel>) response.body();
            }

            @Override
            public void onFailure(Call<List<ItemModel>> call, Throwable t) {

            }
        });
    }

    private void getCharacters() {
        Call<List<CharacterModel>>totalcharacters=retroFitBuilder.callApi().getCharacters(sp.getString("key",""));
        totalcharacters.enqueue(new Callback<List<CharacterModel>>() {
            @Override
            public void onResponse(Call<List<CharacterModel>> call, Response<List<CharacterModel>> response) {
                characters= (ArrayList<CharacterModel>) response.body();
            }

            @Override
            public void onFailure(Call<List<CharacterModel>> call, Throwable t) {

            }
        });

    }

    private void getAllUserMatches(){

        Call<List<MatchModel>>ptotal=retroFitBuilder.callApi().geMatchesByName(sp.getString("username",""),sp.getString("key",""));
        ptotal.enqueue(new Callback<List<MatchModel>>() {
            @Override
            public void onResponse(Call<List<MatchModel>> call, Response<List<MatchModel>> response) {
                if (!(response.body() ==null)){
                    partidasTotales.setText(String.valueOf(response.body().size()));
                    partidas= (ArrayList<MatchModel>) response.body();

                }else {
                    partidas=new ArrayList<>();
                    partidasTotales.setText("No hay partidas");
                }

            }
            @Override
            public void onFailure(Call<List<MatchModel>> call, Throwable t) {
                System.out.println("error papi en total "+t.getMessage());

            }
        });
    }

    private void getWinnedMatches(){
        Call<List<MatchModel>>pganadas=retroFitBuilder.callApi().winMatchesbyName(sp.getString("username",""),sp.getString("key",""));
        pganadas.enqueue(new Callback<List<MatchModel>>() {
            @Override
            public void onResponse(Call<List<MatchModel>> call, Response<List<MatchModel>> response) {
                if (response.body()!=null){
                    partidasGanadas.setText(String.valueOf(response.body().size()));
                }

            }
            @Override
            public void onFailure(Call<List<MatchModel>> call, Throwable t) {
                System.out.println("error papi en ganadas "+t.getMessage());

            }
        });
    }
    private void getLostMatches(){
        Call<List<MatchModel>>pperdidas=retroFitBuilder.callApi().lostMatchesbyName(sp.getString("username",""),sp.getString("key",""));
        pperdidas.enqueue(new Callback<List<MatchModel>>() {
            @Override
            public void onResponse(Call<List<MatchModel>> call, Response<List<MatchModel>> response) {
                if (response.body()!=null){
                    partidasPerdidas.setText(String.valueOf(response.body().size()));
                }
            }
            @Override
            public void onFailure(Call<List<MatchModel>> call, Throwable t) {
                System.out.println("error papi en perdidas "+t.getMessage());
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mi_menu, menu);
        return true;
    }


    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "nombre";
            String description = "descripcion";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(canalId, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}