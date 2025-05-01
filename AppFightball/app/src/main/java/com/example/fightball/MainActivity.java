package com.example.fightball;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {



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


        /*botonprueba.setOnClickListener(new View.OnClickListener() {
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
                        System.out.println("fallo");
                        System.out.println(t.toString());
                    }
                });
            }
        });*/
    }
}