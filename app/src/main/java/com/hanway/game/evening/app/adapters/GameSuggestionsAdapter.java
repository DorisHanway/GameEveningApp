package com.hanway.game.evening.app.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hanway.game.evening.app.GameEveningApplication;
import com.hanway.game.evening.app.R;
import com.hanway.game.evening.app.models.GameSuggestion;
import com.hanway.game.evening.app.models.User;
import com.hanway.game.evening.app.services.DataProviderService;

import java.util.ArrayList;

public class GameSuggestionsAdapter extends ArrayAdapter<GameSuggestion> {

    private boolean allowVoting;

    public GameSuggestionsAdapter(Context context, ArrayList<GameSuggestion> users, boolean allowVoting) {
        super(context, 0, users);
        this.allowVoting = allowVoting;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.game_suggestion_item, parent, false);
        }

        GameEveningApplication context = (GameEveningApplication) convertView.getContext().getApplicationContext();
        User currentUser = context.getCurrentUser();
        GameSuggestion gameSuggestion = getItem(position);

        // Lookup view for data population
        TextView name = (TextView) convertView.findViewById(R.id.game_suggestion_name);
        TextView votes = (TextView) convertView.findViewById(R.id.number_of_votes);
        ImageButton button = (ImageButton) convertView.findViewById(R.id.upvote_game_suggestion);
        if (allowVoting) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Send DB request
                    Intent dbIntent = new Intent(view.getContext(), DataProviderService.class);
                    dbIntent.setAction(DataProviderService.VOTE_FOR_GAME_SUGGESTION);
                    dbIntent.putExtra(DataProviderService.GAME_SUGGESTION_ID, gameSuggestion.id);
                    dbIntent.putExtra(DataProviderService.USER_ID, currentUser.userId);
                    view.getContext().startService(dbIntent);

                    // Update the view
                    String updatedText = gameSuggestion.votedByUsers.size() + 1 + " Stimme(n)";
                    ((ImageButton) view).setImageResource(R.drawable.ic_upvote);
                    votes.setText(updatedText);
                }
            });
            if (gameSuggestion.votedByUsers.contains(currentUser.userId)) {
                button.setImageResource(R.drawable.ic_upvote);
                button.setEnabled(false);
            }
        } else {
            button.setVisibility(View.GONE);
        }

        // Populate the data into the template view using the data object
        name.setText(gameSuggestion.gameName);
        String updatedText = gameSuggestion.votedByUsers.size() + " Stimme(n)";
        votes.setText(updatedText);

        // Return the completed view to render on screen
        return convertView;
    }
}
