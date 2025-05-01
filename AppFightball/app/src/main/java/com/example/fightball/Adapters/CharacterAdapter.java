package com.example.fightball.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fightball.Models.CharacterModel;
import com.example.fightball.R;

import java.util.ArrayList;

public class CharacterAdapter extends ArrayAdapter<CharacterModel> {

    private int resource;
    private ArrayList<CharacterModel> characters;

    public CharacterAdapter(@NonNull Context context, int resource, @NonNull ArrayList<CharacterModel> objects) {
        super(context, resource, objects);
        this.resource = resource;
        this.characters = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View row = inflater.inflate(resource, parent, false);

        CharacterModel character = characters.get(position);

        TextView txtName = row.findViewById(R.id.characterName);
        TextView txtHealth = row.findViewById(R.id.characterHealth);
        TextView txtStamina = row.findViewById(R.id.characterStamina);
        TextView txtDamage = row.findViewById(R.id.characterDamage);

        txtName.setText(character.getName());
        txtHealth.setText("Health: " + character.getMax_health());
        txtStamina.setText("Stamina: " + character.getMax_stamina());
        txtDamage.setText("Damage: " + character.getDamage());

        return row;
    }
}
