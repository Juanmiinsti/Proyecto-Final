package com.example.fightball;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PreferencesActivity extends AppCompatActivity {

    CheckBox saveUser;
    CheckBox savePasword;
    Button cancelbt;
    Button savebt;



    SharedPreferences sp;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_preferences);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        config();

    }

    private void config() {
        sp = getSharedPreferences("FightBall", MODE_PRIVATE);
        editor = sp.edit();

        savePasword=findViewById(R.id.rememberPasswordCheckBox);
        saveUser=findViewById(R.id.rememberUserCheckBox);
        cancelbt=findViewById(R.id.cancelPreferencesButton);
        savebt=findViewById(R.id.savePrefetencesbtt);


        if (sp.getBoolean("savePassword",false)){
        savePasword.setChecked(true);
        }
        if (sp.getBoolean("saveUser",false)){
            saveUser.setChecked(true);
        }

        setlisteners();

    }

    private void setlisteners() {
        cancelbt.setOnClickListener(e ->{
            finish();
        });

        savebt.setOnClickListener(e ->{
            if (savePasword.isChecked()){
                editor.putBoolean("savePassword",true);
            }else {
                editor.putBoolean("savePassword",false);
            }

            if (saveUser.isChecked()){
                editor.putBoolean("saveUser",true);
            }else {
                editor.putBoolean("saveUser",false);
            }
            editor.apply();
            finish();

        });


    }

}