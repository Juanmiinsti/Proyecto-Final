package com.example.fightball.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fightball.Models.MatchModel;
import com.example.fightball.R;

import java.util.ArrayList;

public class MatchAdapter extends ArrayAdapter<MatchModel> {

    private int mResource;
    private ArrayList<MatchModel> matches;

    public MatchAdapter(@NonNull Context context, int resource, @NonNull ArrayList<MatchModel> objects) {
        super(context, resource, objects);
        this.mResource = resource;
        this.matches = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View row = inflater.inflate(mResource, parent, false);

        TextView txtDate = row.findViewById(R.id.matchDate);
        txtDate.setText(matches.get(position).getDate());

        TextView txtLength = row.findViewById(R.id.matchLength);
        txtLength.setText(String.valueOf(matches.get(position).getLength()));

        TextView txtWinner = row.findViewById(R.id.matchWinner);
        txtWinner.setText("Winner ID: " + matches.get(position).getUserWinnerId());

        TextView txtLoser = row.findViewById(R.id.matchLoser);
        txtLoser.setText("Loser ID: " + matches.get(position).getUserLoserId());

        return row;
    }
}
