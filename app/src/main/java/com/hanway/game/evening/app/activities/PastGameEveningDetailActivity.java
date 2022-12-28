package com.hanway.game.evening.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hanway.game.evening.app.GameEveningApplication;
import com.hanway.game.evening.app.R;
import com.hanway.game.evening.app.adapters.GameSuggestionsAdapter;
import com.hanway.game.evening.app.models.GameEvening;
import com.hanway.game.evening.app.models.GameEveningRating;
import com.hanway.game.evening.app.models.GameSuggestion;
import com.hanway.game.evening.app.models.User;
import com.hanway.game.evening.app.services.DataProviderResultReceiver;
import com.hanway.game.evening.app.services.DataProviderService;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

public class PastGameEveningDetailActivity extends AppCompatActivity {

    private DataProviderResultReceiver resultReceiver;
    private GameEvening gameEvening;
    private User currentUser;
    private ArrayList<GameSuggestion> gameSuggestions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameEvening = (GameEvening) getIntent().getSerializableExtra("GAME_EVENING");
        GameEveningApplication context = (GameEveningApplication) getApplicationContext();
        currentUser = context.getCurrentUser();
        setContentView(R.layout.past_game_evening_detail);

        setupServiceReceiver();
        populateGameEveningInfo();
        getGameSuggestions();
        populateRatings();
    }

    public void onSubmitRating(View button) {
        Intent dataProviderIntent = new Intent(this, DataProviderService.class);
        RatingBar foodRating = findViewById(R.id.rate_food_bar);
        RatingBar hostRating = findViewById(R.id.rate_host_bar);
        RatingBar eveningRating = findViewById(R.id.rate_evening_bar);

        dataProviderIntent.setAction(DataProviderService.CREATE_GAME_EVENING_RATING);
        dataProviderIntent.putExtra(DataProviderService.GAME_EVENING_RATING,
                new GameEveningRating(0L, gameEvening.id, currentUser.userId, foodRating.getRating(), hostRating.getRating(), eveningRating.getRating()));
        startService(dataProviderIntent);
        // Update the game evening ratings
        populateRatings();
        Toast.makeText(PastGameEveningDetailActivity.this, "Bewertung gespeichert.", Toast.LENGTH_LONG).show();
    }

    public void populateRatings() {
        Intent dataProviderIntent = new Intent(this, DataProviderService.class);
        dataProviderIntent.setAction(DataProviderService.GET_RATINGS_FOR_EVENING);
        dataProviderIntent.putExtra(DataProviderService.GAME_EVENING_ID, gameEvening.id);
        dataProviderIntent.putExtra(DataProviderService.GENERAL_RECEIVER, resultReceiver);
        startService(dataProviderIntent);
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
                        GameSuggestionsAdapter adapter = new GameSuggestionsAdapter(PastGameEveningDetailActivity.this, gameSuggestions, gameEvening.isUpcoming());
                        ListView listView = findViewById(R.id.list_game_recommendations);
                        listView.setAdapter(adapter);
                    } else if (resultData.containsKey(DataProviderService.GET_GAME_EVENING_RATINGS_RESULT)) {
                        ArrayList<GameEveningRating> ratings = (ArrayList<GameEveningRating>) resultData.getSerializable(DataProviderService.GET_GAME_EVENING_RATINGS_RESULT);
                        if (!ratings.isEmpty()) {
                            RatingBar foodRating = findViewById(R.id.rate_food_bar);
                            RatingBar hostRating = findViewById(R.id.rate_host_bar);
                            RatingBar eveningRating = findViewById(R.id.rate_evening_bar);

                            float totalFoodRating = 0;
                            float totalHostRating = 0;
                            float totalEveningRating = 0;

                            for (GameEveningRating rating : ratings) {
                                // Check if the current user already voted
                                if(rating.userId.equals(currentUser.userId)){
                                    Button submitVote = findViewById(R.id.submit_vote);
                                    submitVote.setAlpha(.5f);
                                    submitVote.setClickable(false);
                                    foodRating.setIsIndicator(true);
                                    hostRating.setIsIndicator(true);
                                    eveningRating.setIsIndicator(true);
                                }
                                totalFoodRating += rating.foodRating;
                                totalHostRating += rating.hostRating;
                                totalEveningRating += rating.eveningRating;
                            }
                            foodRating.setRating(totalFoodRating / (float) ratings.size());
                            hostRating.setRating(totalHostRating / (float) ratings.size());
                            eveningRating.setRating(totalEveningRating / (float) ratings.size());
                        }
                    }
                } else {
                    Toast.makeText(PastGameEveningDetailActivity.this, "Fehler beim Laden von Daten", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}