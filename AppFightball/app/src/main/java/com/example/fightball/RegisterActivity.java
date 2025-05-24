package com.example.fightball;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fightball.API.RetroFitBuilder;
import com.example.fightball.Models.RegisterResponse;
import com.example.fightball.Models.UserModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    Button cancelbt;
    Button saveBt;
    EditText usernameET;
    EditText passwordET;
    EditText conPasswordET;
    SharedPreferences sp;
    RetroFitBuilder retroFitBuilder = RetroFitBuilder.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        config();
    }

    private void config() {

        sp = getSharedPreferences("FightBall", MODE_PRIVATE);

        cancelbt = findViewById(R.id.registerCancelbtt);
        saveBt = findViewById(R.id.registerSavebtt);
        usernameET = findViewById(R.id.inputuserRegister);
        passwordET = findViewById(R.id.inputpwdRegister);
        conPasswordET = findViewById(R.id.confirmpwdRegister);

        setListeners();

    }

    private void setListeners() {
        cancelbt.setOnClickListener(e -> {
            finish();
        });
        saveBt.setOnClickListener(e -> {
            apiCall();

        });

    }

    private void apiCall() {
        boolean valid=true;
        UserModel aux =new UserModel(0,usernameET.getText().toString(),passwordET.getText().toString());

        if (aux.getName().isEmpty()){
            Toast.makeText(this,"please enter a valid user name",Toast.LENGTH_LONG).show();
            valid=false;
        }
        if (aux.getPassword().isEmpty()){
            Toast.makeText(this,"please enter a valid password",Toast.LENGTH_LONG).show();
            valid=false;
        }


        if (!aux.getPassword().equals(conPasswordET.getText().toString())){
            Toast.makeText(this,"the passwords dotn match",Toast.LENGTH_LONG).show();
            valid=false;
        }


        if (valid){
            Call<RegisterResponse>register=retroFitBuilder.callApi().registerUser(sp.getString("key",""),aux);
            register.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                    if (response.body()!=null){
                        if (response.code()==200){
                            Toast.makeText(RegisterActivity.this,"User Succeful Registered",Toast.LENGTH_LONG).show();
                            finish();
                        }else {
                            Toast.makeText(RegisterActivity.this,"Something Went wrong",Toast.LENGTH_LONG).show();

                        }
                        Toast.makeText(RegisterActivity.this,"Response Was null",Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {
                    Toast.makeText(RegisterActivity.this,"Response Failed",Toast.LENGTH_LONG).show();
                    System.out.println(t.getMessage());

                }
            });
        }
    }


}