package com.example.fightball.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.fightball.Models.TutorialModel;
import com.example.fightball.R;

import java.util.List;

public class TutorialAdapter extends ArrayAdapter<TutorialModel> {

    private int resource;

    public TutorialAdapter(Context context, int resource, List<TutorialModel> tutorials) {
        super(context, resource, tutorials);
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TutorialModel tutorial = getItem(position);

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(resource, parent, false);

        TextView txtTutorialTitle = view.findViewById(R.id.tutorialTitle);
        TextView txtTutorialDesc = view.findViewById(R.id.tutorialDescription);

        txtTutorialTitle.setText("Título: " + tutorial.getTitle());
        txtTutorialDesc.setText("Descripción: " + tutorial.getDescription());

        return view;
    }
}
