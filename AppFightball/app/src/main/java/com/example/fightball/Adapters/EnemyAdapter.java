package com.example.fightball.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fightball.Models.EnemyModel;
import com.example.fightball.R;

import java.util.List;

public class EnemyAdapter extends ArrayAdapter<EnemyModel> {

    private int resource;

    public EnemyAdapter(@NonNull Context context, int resource, @NonNull List<EnemyModel> enemies) {
        super(context, resource, enemies);
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(resource, parent, false);

        EnemyModel enemy = getItem(position);

        TextView txtName = view.findViewById(R.id.enemyName);
        TextView txtId = view.findViewById(R.id.enemyId);
        TextView txtHealth = view.findViewById(R.id.enemyHealth);
        TextView txtDamage = view.findViewById(R.id.enemyDamage);

        if (enemy != null) {
            txtName.setText(enemy.getName());
            txtId.setText("ID: " + enemy.getId());
            txtHealth.setText("Salud: " + enemy.getMax_health());
            txtDamage.setText("Daño: " + enemy.getDamage());
        }

        return view;
    }
}
