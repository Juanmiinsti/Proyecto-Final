package com.example.fightball.Admin;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fightball.Models.UserModel;
import com.example.fightball.R;

import org.springframework.security.crypto.password.PasswordEncoder;

public class EditUserActivity extends AppCompatActivity {
    private int userPosition;
    private UserModel editedUser;
    private EditText userinput;
    private EditText oldpasswordInput;
    private EditText newPasswordInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_user);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        config();
    }

    private void config() {
        userinput=findViewById(R.id.inputUserEdit);

        userPosition=getIntent().getIntExtra("pos",0);
        editedUser=AdminMainActivity.users.get(userPosition);
        oldpasswordInput=findViewById(R.id.inputOldPassword);
        userinput.setText(editedUser.getName());



    }

    private boolean checKoldPassword(){





        return false;
    }
}