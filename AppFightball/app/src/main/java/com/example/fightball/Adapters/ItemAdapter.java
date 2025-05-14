package com.example.fightball.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fightball.Models.ItemModel;
import com.example.fightball.R;

import java.util.List;

public class ItemAdapter extends ArrayAdapter<ItemModel> {

    private int resource;

    public ItemAdapter(@NonNull Context context, int resource, @NonNull List<ItemModel> items) {
        super(context, resource, items);
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(resource, parent, false);

        ItemModel item = getItem(position);

        TextView txtName = view.findViewById(R.id.itemName);
        TextView txtId = view.findViewById(R.id.itemId);
        TextView txtDescription = view.findViewById(R.id.itemDescription);

        if (item != null) {
            txtName.setText(item.getName());
            txtId.setText("ID: " + item.getId());
            txtDescription.setText(item.getDescription());
        }




        return view;
    }
}
