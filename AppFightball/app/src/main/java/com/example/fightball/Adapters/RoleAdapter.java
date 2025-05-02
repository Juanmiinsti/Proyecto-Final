package com.example.fightball.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.fightball.Models.RoleModel;
import com.example.fightball.R;

import java.util.List;

public class RoleAdapter extends ArrayAdapter<RoleModel> {

    private int resource;

    public RoleAdapter(Context context, int resource, List<RoleModel> roles) {
        super(context, resource, roles);
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RoleModel role = getItem(position);

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(resource, parent, false);

        TextView txtRoleId = view.findViewById(R.id.roleId);
        TextView txtRoleName = view.findViewById(R.id.roleName);

        txtRoleId.setText("ID: " + role.getId());
        txtRoleName.setText("Name: " + role.getName());

        return view;
    }
}
