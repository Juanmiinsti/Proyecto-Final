package com.example.fightball.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fightball.R;

import java.util.List;

public class ChatAdapter extends ArrayAdapter<String> {

    public ChatAdapter(Context context, List<String> messages) {
        super(context, 0, messages);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String message = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.chat_item, parent, false);
        }

        TextView textMessage = convertView.findViewById(R.id.textMessage);
        ImageView background = convertView.findViewById(R.id.backgroundBubble);

        textMessage.setText(message);

        return convertView;
    }
}
