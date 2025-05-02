package com.example.fightball.Admin;

import android.os.Bundle;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fightball.Adapters.UserAdapter;
import com.example.fightball.Adapters.UserAdapterAdmin;
import com.example.fightball.Mod.ModMainActivity;
import com.example.fightball.R;

public class AdminUsersActivity extends AppCompatActivity {
    public static UserAdapterAdmin adapter;
    ListView userVisual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_users);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        config();
    }
    private void config() {
        listofUsers();
        registerForContextMenu(userVisual);

    }

    private void listofUsers() {
        adapter = new UserAdapterAdmin(this, R.layout.user_admin_item, AdminMainActivity.users);
        userVisual = findViewById(R.id.adminUsersList);
        userVisual.setAdapter(adapter);
    }

}