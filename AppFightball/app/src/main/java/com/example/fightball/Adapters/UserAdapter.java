package com.example.fightball.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.fightball.Models.UserModel;
import com.example.fightball.R;

import java.util.List;

public class UserAdapter extends ArrayAdapter<UserModel> {

    private int resource;

    public UserAdapter(Context context, int resource, List<UserModel> users) {
        super(context, resource, users);
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        UserModel user = getItem(position);

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(resource, parent, false);

        TextView txtUserName = view.findViewById(R.id.userName);
        TextView txtUserId = view.findViewById(R.id.userId);

        txtUserName.setText("Name: " + user.getName());
        txtUserId.setText("ID: " + user.getId());

        return view;
    }
}
