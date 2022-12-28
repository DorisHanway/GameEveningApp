package com.hanway.game.evening.app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hanway.game.evening.app.R;
import com.hanway.game.evening.app.adapters.GameSuggestionsAdapter;
import com.hanway.game.evening.app.models.GameEvening;
import com.hanway.game.evening.app.models.GameSuggestion;
import com.hanway.game.evening.app.services.DataProviderResultReceiver;
import com.hanway.game.evening.app.services.DataProviderService;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

public class UpcomingGameEveningDetailActivity extends AppCompatActivity {

    private DataProviderResultReceiver resultReceiver;
    private GameEvening gameEvening;
    private ArrayList<GameSuggestion> gameSuggestions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameEvening = (GameEvening) getIntent().getSerializableExtra("GAME_EVENING");
        setContentView(R.layout.upcoming_game_evening_detail);

        setupServiceReceiver();
        populateGameEveningInfo();
        getGameSuggestions();
    }

    public void onAddSuggestion(View button) {
        Intent dataProviderIntent = new Intent(this, DataProviderService.class);
        TextView gameNameInput = findViewById(R.id.game_suggestion_input);
        String gameName = String.valueOf(gameNameInput.getText()).trim();

        if (gameName.equals("")) {
            Toast.makeText(UpcomingGameEveningDetailActivity.this, "Spielname darf nicht leer sein!", Toast.LENGTH_LONG).show();
            return;
        }

        // Check if suggestion already exists
        for (GameSuggestion gameSuggestion : gameSuggestions) {
            boolean isDuplicate = gameSuggestion.gameName.equals(gameName);
            if (isDuplicate) {
                Toast.makeText(UpcomingGameEveningDetailActivity.this, "Spielvorschlag existiert schon!", Toast.LENGTH_LONG).show();
                return;
            }
        }

        dataProviderIntent.setAction(DataProviderService.CREATE_GAME_SUGGESTION);
        dataProviderIntent.putExtra(DataProviderService.GAME_EVENING_SUGGESTION, new GameSuggestion(0L, gameEvening.id, gameName, new ArrayList<Long>()));
        startService(dataProviderIntent);
        gameNameInput.setText("");
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        Toast.makeText(UpcomingGameEveningDetailActivity.this, "Vorschlag hinzugefügt!", Toast.LENGTH_SHORT).show();
        // Update the list of suggestions
        getGameSuggestions();
    }

    public void onBackButtonClicked(View button) {
        Intent changeIntent = new Intent(this, GameEveningOverviewActivity.class);
        startActivity(changeIntent);
    }

    private void populateGameEveningInfo() {
        TextView host = findViewById(R.id.host);
        TextView location = findViewById(R.id.location);
        TextView date = findViewById(R.id.gameEveningDate);

        host.setText(gameEvening.host);
        location.setText(gameEvening.location);
        Locale locale = new Locale("de", "DE");
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, locale);
        String formattedDate = dateFormat.format(gameEvening.date);
        date.setText(formattedDate);
    }

    private void getGameSuggestions() {
        Intent dataProviderIntent = new Intent(this, DataProviderService.class);
        dataProviderIntent.setAction(DataProviderService.GET_GAME_EVENING_SUGGESTIONS);
        dataProviderIntent.putExtra(DataProviderService.GAME_EVENING_ID, gameEvening.id);
        dataProviderIntent.putExtra(DataProviderService.GENERAL_RECEIVER, resultReceiver);
        startService(dataProviderIntent);
    }

    private void setupServiceReceiver() {
        resultReceiver = new DataProviderResultReceiver(new Handler());
        resultReceiver.setReceiver(new DataProviderResultReceiver.Receiver() {
            @Override
            public void onReceiveResult(int resultCode, Bundle resultData) {
                if (resultCode == RESULT_OK) {
                    if (resultData.containsKey(DataProviderService.GET_GAME_EVENING_SUGGESTIONS_RESULT)) {
                        gameSuggestions = (ArrayList<GameSuggestion>) resultData.getSerializable(DataProviderService.GET_GAME_EVENING_SUGGESTIONS_RESULT);
                        // Sort so that the suggestions with the most votes are on top
                        Collections.sort(gameSuggestions, new Comparator<GameSuggestion>() {
                            @Override
                            public int compare(GameSuggestion lhs, GameSuggestion rhs) {
                                return Integer.compare(rhs.votedByUsers.size(), lhs.votedByUsers.size());
                            }
                        });
                        GameSuggestionsAdapter adapter = new GameSuggestionsAdapter(UpcomingGameEveningDetailActivity.this, gameSuggestions, gameEvening.isUpcoming());
                        ListView listView = findViewById(R.id.list_game_recommendations);
                        listView.setAdapter(adapter);
                    }
                } else {
                    Toast.makeText(UpcomingGameEveningDetailActivity.this, "Fehler beim Laden von Daten", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}