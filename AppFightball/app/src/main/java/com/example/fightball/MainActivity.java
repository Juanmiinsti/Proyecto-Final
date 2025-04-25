package com.example.fightball;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fightball.API.RetroFitBuilder;
import com.example.fightball.PostModels.CharacterModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    RetroFitBuilder retroFitBuilder=RetroFitBuilder.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        config();
    }
    public  void  config(){
        retroFitBuilder.build("http://localhost:8080");
        Button botonprueba=findViewById(R.id.buttonTest);
        TextView textView =findViewById(R.id.textTest);

        botonprueba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                Call <List<CharacterModel>> call=retroFitBuilder.callApi().getCharacters();
                call.enqueue(new Callback<List<CharacterModel>>() {
                    @Override
                    public void onResponse(Call<List<CharacterModel>> call, Response<List<CharacterModel>> response) {
                        textView.setText(response.body().toString());
                    }

                    @Override
                    public void onFailure(Call<List<CharacterModel>> call, Throwable t) {

                    }
                });
            }
        });
    }
}