package com.example.fightball.Player;

import android.os.Bundle;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fightball.Adapters.CharacterAdapter;
import com.example.fightball.Adapters.ItemAdapter;
import com.example.fightball.R;

public class PlayerItemActivity extends AppCompatActivity {
    public static ItemAdapter adapter;
    ListView itemsvisual;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_player_item);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        config();
    }

    private void config() {
        adapter=new ItemAdapter(this,R.layout.item,PlayerMainActivity.items);
        itemsvisual=findViewById(R.id.itemList);
        itemsvisual.setAdapter(adapter);

    }
}