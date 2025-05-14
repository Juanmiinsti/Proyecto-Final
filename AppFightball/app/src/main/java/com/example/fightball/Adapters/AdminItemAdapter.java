package com.example.fightball.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fightball.Models.ItemModelAdmin;
import com.example.fightball.R;

import java.util.List;

public class AdminItemAdapter extends ArrayAdapter<ItemModelAdmin> {

    private int resource;

    public AdminItemAdapter(@NonNull Context context, int resource, @NonNull List<ItemModelAdmin> items) {
        super(context, resource, items);
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(resource, parent, false);

        ItemModelAdmin item = getItem(position);

        TextView txtName = view.findViewById(R.id.adminItemName);
        TextView txtId = view.findViewById(R.id.adminItemId);
        TextView txtDescription = view.findViewById(R.id.adminItemDescription);
        TextView txtQuantity = view.findViewById(R.id.adminItemQuantity);

        if (item != null) {
            txtName.setText(item.getName());
            txtId.setText("ID: " + item.getId());
            txtDescription.setText(item.getDescription());
            txtQuantity.setText("Cantidad: " + item.getQuantity());
        }

        return view;
    }
}
