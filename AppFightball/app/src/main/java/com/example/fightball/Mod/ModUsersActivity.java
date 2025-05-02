package com.example.fightball.Mod;

import android.os.Bundle;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fightball.Adapters.MatchAdapter;
import com.example.fightball.Adapters.UserAdapter;
import com.example.fightball.R;

public class ModUsersActivity extends AppCompatActivity {
    public static UserAdapter adapter;
    ListView userVisual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mod_users);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        config();
    }

    private void config() {
        listofUsers();
    }

    private void listofUsers(){
        adapter=new UserAdapter(this,R.layout.usermod_item, ModMainActivity.users);
        userVisual=findViewById(R.id.usersModList);
        userVisual.setAdapter(adapter);
    }
}