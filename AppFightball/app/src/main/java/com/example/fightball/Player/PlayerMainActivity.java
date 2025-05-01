package com.example.fightball.Player;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fightball.API.RetroFitBuilder;
import com.example.fightball.Models.MatchModel;
import com.example.fightball.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayerMainActivity extends AppCompatActivity {
    static String canalId="777";
    TextView partidasGanadas;
    TextView partidasPerdidas;
    TextView partidasTotales;
    TextView personajeMasUsado;

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

        partidasGanadas=findViewById(R.id.pGanadasText);
        partidasPerdidas=findViewById(R.id.pPerdidasText);

        apicalls();

        createNotificationChannel();
        Toolbar toolbar = findViewById(R.id.barraMenu);
        setSupportActionBar(toolbar);



    }

    private void apicalls() {
        try {
            Call<List<MatchModel>>pganadas=retroFitBuilder.callApi().winMatchesbyName("juanmi",sp.getString("key",""));
            pganadas.enqueue(new Callback<List<MatchModel>>() {
                @Override
                public void onResponse(Call<List<MatchModel>> call, Response<List<MatchModel>> response) {
                    partidasGanadas.setText(response.body().size());
                }
                @Override
                public void onFailure(Call<List<MatchModel>> call, Throwable t) {
                }
            });

            Call<List<MatchModel>>pperdidas=retroFitBuilder.callApi().lostMatchesbyName("juanmi",sp.getString("key",""));
            pperdidas.enqueue(new Callback<List<MatchModel>>() {
                @Override
                public void onResponse(Call<List<MatchModel>> call, Response<List<MatchModel>> response) {
                    partidasPerdidas.setText(response.body().size());
                }
                @Override
                public void onFailure(Call<List<MatchModel>> call, Throwable t) {
                }
            });


        }catch (Exception e){
            System.out.println(e.getMessage());
        }
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