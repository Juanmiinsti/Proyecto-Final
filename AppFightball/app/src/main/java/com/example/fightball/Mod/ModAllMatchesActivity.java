package com.example.fightball.Mod;

import android.os.Bundle;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fightball.Adapters.MatchAdapter;
import com.example.fightball.R;

public class ModAllMatchesActivity extends AppCompatActivity {
    public static MatchAdapter adapter;
    ListView partidasVisual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mod_all_matches);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        config();
    }

    private void config() {
        listofMatches();
    }

    private void listofMatches(){
        adapter=new MatchAdapter(this,R.layout.match_item, ModMainActivity.matches);
        partidasVisual=findViewById(R.id.modMatchesLists);
        partidasVisual.setAdapter(adapter);
    }
}