package com.example.fightball.Mod;

import android.os.Bundle;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fightball.Adapters.RoleAdapter;
import com.example.fightball.R;

public class ModRoleActivity extends AppCompatActivity {
    public static RoleAdapter adapter;
    ListView roleVisual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mod_role);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        config();
    }
    private void config() {
        listofroles();
    }

    private void listofroles(){
        adapter=new RoleAdapter(this,R.layout.role_item, ModMainActivity.roles);
        roleVisual=findViewById(R.id.modRolesList);
        roleVisual.setAdapter(adapter);
    }
}