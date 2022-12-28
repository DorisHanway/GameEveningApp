package com.hanway.game.evening.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hanway.game.evening.app.R;
import com.hanway.game.evening.app.models.GameEvening;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class GameEveningAdapter extends ArrayAdapter<GameEvening> {

    public GameEveningAdapter(Context context, ArrayList<GameEvening> gameEvenings) {
        super(context, 0, gameEvenings);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.game_evening_item, parent, false);
        }

        GameEvening gameEvening = getItem(position);
        if(gameEvening.isUpcoming()){
            convertView.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.upcoming_item));
        } else {
            convertView.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.past_item));
        }

        // Lookup view for data population
        TextView host = convertView.findViewById(R.id.host);
        TextView location = convertView.findViewById(R.id.location);
        TextView date = convertView.findViewById(R.id.gameEveningDate);
        // Populate the data into the template view using the data object
        host.setText(gameEvening.host);
        location.setText(gameEvening.location);

        // Format date
        Locale locale = new Locale("de", "DE");
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, locale);
        String formattedDate = dateFormat.format(gameEvening.date);
        date.setText(formattedDate);

        return convertView;
    }
}
