package com.hanway.game.evening.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hanway.game.evening.app.R;
import com.hanway.game.evening.app.models.UserMessage;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class UserMessageAdapter extends ArrayAdapter<UserMessage> {

    public UserMessageAdapter(Context context, ArrayList<UserMessage> gameEvenings) {
        super(context, 0, gameEvenings);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.user_message_item, parent, false);
        }

        UserMessage userMessage = getItem(position);
        TextView sender = convertView.findViewById(R.id.message_sender);
        TextView timestamp = convertView.findViewById(R.id.message_timestamp);
        TextView message = convertView.findViewById(R.id.user_message_content);

        // Format content
        Locale locale = new Locale("de", "DE");
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, locale);
        String formattedDate = dateFormat.format(userMessage.timestamp);

        sender.setText(userMessage.senderName);
        timestamp.setText(formattedDate);
        message.setText(userMessage.message);

        return convertView;
    }
}
