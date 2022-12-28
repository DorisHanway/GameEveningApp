package com.hanway.game.evening.app.services;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import com.hanway.game.evening.app.models.GameEvening;
import com.hanway.game.evening.app.models.GameEveningRating;
import com.hanway.game.evening.app.models.GameSuggestion;
import com.hanway.game.evening.app.GameEveningApplication;
import com.hanway.game.evening.app.models.UserMessage;
import com.hanway.game.evening.app.persistence.GameEveningDatabase;
import com.hanway.game.evening.app.persistence.entities.GameEveningEntity;
import com.hanway.game.evening.app.persistence.entities.GameEveningRatingEntity;
import com.hanway.game.evening.app.persistence.entities.GameEveningWithUser;
import com.hanway.game.evening.app.persistence.entities.GameSuggestionEntity;
import com.hanway.game.evening.app.persistence.entities.UserGameSuggestionEntity;
import com.hanway.game.evening.app.persistence.entities.UserMessageEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DataProviderService extends IntentService {

    public static final String GENERAL_RECEIVER = "com.hanway.game.evening.app.services.receiver";

    // Actions the intent can perform
    public static final String GET_ALL_GAME_EVENING = "com.hanway.game.evening.app.services.action.get.game.evening.entries";
    public static final String GET_GAME_EVENING_SUGGESTIONS = "com.hanway.game.evening.app.services.action.get.game.evening.suggestions";
    public static final String GET_RATINGS_FOR_EVENING = "com.hanway.game.evening.app.services.action.get.game.evening.ratings";
    public static final String GET_USER_MESSAGES = "com.hanway.game.evening.app.services.action.get.game.user.messages";
    public static final String CREATE_GAME_EVENING_RATING = "com.hanway.game.evening.app.services.action.create.game.evening.rating";
    public static final String CREATE_GAME_SUGGESTION = "com.hanway.game.evening.app.services.action.create.game.evening.suggestion";
    public static final String CREATE_USER_MESSAGE = "com.hanway.game.evening.app.services.action.create.user.message";
    public static final String VOTE_FOR_GAME_SUGGESTION = "com.hanway.game.evening.app.services.action.create.game.evening.suggestion.vote";
    public static final String GENERATE_NEXT_GAME_EVENING = "com.hanway.game.evening.app.services.action.create.game.evening";

    // Parameters that the intent will accept
    public static final String GAME_EVENING_ID = "com.hanway.game.evening.app.services.extra.game.evening.id";
    public static final String GAME_EVENING_SUGGESTION = "com.hanway.game.evening.app.services.extra.game.evening.suggestion.create";
    public static final String GAME_EVENING_RATING = "com.hanway.game.evening.app.services.extra.game.evening.rating.create";
    public static final String GAME_SUGGESTION_ID = "com.hanway.game.evening.app.services.extra.game.evening.suggestion.id";
    public static final String USER_MESSAGE = "com.hanway.game.evening.app.services.extra.game.evening.user.message";
    public static final String USER_ID = "com.hanway.game.evening.app.services.extra.game.evening.user.id";

    // Results that can be gotten with a receiver
    public static final String GET_ALL_GAME_EVENING_RESULT = "com.hanway.game.evening.app.services.action.get.game.evening.entries.result";
    public static final String GET_GAME_EVENING_SUGGESTIONS_RESULT = "com.hanway.game.evening.app.services.action.get.game.evening.suggestions.result";
    public static final String GET_GAME_EVENING_RATINGS_RESULT = "com.hanway.game.evening.app.services.action.get.game.evening.ratings.result";
    public static final String GET_USER_MESSAGES_RESULT = "com.hanway.game.evening.app.services.action.get.game.user.messages.result";

    public DataProviderService() {
        super("DataProviderService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            switch (action) {
                case GET_ALL_GAME_EVENING:
                    getAllGameEveningEntries(intent);
                    break;
                case GET_GAME_EVENING_SUGGESTIONS:
                    getAllGameSuggestions(intent);
                    break;
                case CREATE_GAME_SUGGESTION:
                    createGameSuggestion(intent);
                    break;
                case VOTE_FOR_GAME_SUGGESTION:
                    setVoteForGameSuggestion(intent);
                    break;
                case GET_RATINGS_FOR_EVENING:
                    getRatingsForGameEvening(intent);
                    break;
                case CREATE_GAME_EVENING_RATING:
                    createGameEveningRating(intent);
                    break;
                case GET_USER_MESSAGES:
                    getUserMessages(intent);
                    break;
                case CREATE_USER_MESSAGE:
                    createUserMessage(intent);
                    break;
                case GENERATE_NEXT_GAME_EVENING:
                    generateNextGameEvening();
                    break;
            }
        }
    }

    private void generateNextGameEvening() {
        GameEveningDatabase database = getDatabase();
        Date latestGameEveningDate =  database.gameEveningDao().getLatestGameEveningDate();
        Long nextHostId = database.userDao().getNextHostId();

        Calendar calender = Calendar.getInstance();
        calender.setTime(latestGameEveningDate);
        calender.add(Calendar.DATE, 7);
        GameEveningEntity gameEvening = new GameEveningEntity(nextHostId, calender.getTime());
        database.gameEveningDao().insertGameEvening(gameEvening);
        database.userDao().updateLastHostedForUserId(nextHostId, calender.getTime());
    }

    private void createUserMessage(Intent intent) {
        GameEveningDatabase database = getDatabase();
        UserMessage userMessage = (UserMessage) intent.getExtras().get(USER_MESSAGE);
        UserMessageEntity entity = new UserMessageEntity(userMessage.senderName, userMessage.message, userMessage.timestamp);
        database.userMessageDao().insertUserMessage(entity);
    }

    private void getUserMessages(Intent intent) {
        GameEveningDatabase database = getDatabase();
        ArrayList<UserMessage> userMessages = new ArrayList<>();
        List<UserMessageEntity> userMessageEntities = database.userMessageDao().getAll();
        for (UserMessageEntity entity : userMessageEntities) {
            userMessages.add(new UserMessage(entity.userMessageId, entity.senderName, entity.message, entity.timestamp));
        }
        sendResponse(GET_USER_MESSAGES_RESULT, intent, userMessages);
    }

    private void createGameEveningRating(Intent intent) {
        GameEveningDatabase database = getDatabase();
        GameEveningRating rating = (GameEveningRating) intent.getExtras().get(GAME_EVENING_RATING);
        GameEveningRatingEntity entity = new GameEveningRatingEntity(rating.gameEveningId, rating.userId, rating.foodRating, rating.hostRating, rating.eveningRating);
        database.gameEveningRatingDao().insertGameEveningRating(entity);
    }

    private void getRatingsForGameEvening(Intent intent) {
        GameEveningDatabase database = getDatabase();
        ArrayList<GameEveningRating> ratings = new ArrayList<>();
        Long gameEveningId = (Long) intent.getExtras().get(GAME_EVENING_ID);
        List<GameEveningRatingEntity> ratingsList = database.gameEveningRatingDao().getAllForGameEveningId(gameEveningId);

        for (GameEveningRatingEntity entity : ratingsList) {
            ratings.add(new GameEveningRating(entity.gameEveningRatingId, entity.gameEvening, entity.user, entity.foodRating,
                    entity.hostRating, entity.eveningRating));
        }
        sendResponse(GET_GAME_EVENING_RATINGS_RESULT, intent, ratings);
    }

    private void setVoteForGameSuggestion(Intent intent) {
        GameEveningDatabase database = getDatabase();
        Long userId = (Long) intent.getExtras().get(USER_ID);
        Long gameSuggestionId = (Long) intent.getExtras().get(GAME_SUGGESTION_ID);
        database.gameSuggestionDao().insertUserGameSuggestionEntity(new UserGameSuggestionEntity(userId, gameSuggestionId));
    }

    private void createGameSuggestion(Intent intent) {
        GameEveningDatabase database = getDatabase();
        GameSuggestion gameSuggestion = (GameSuggestion) intent.getExtras().get(GAME_EVENING_SUGGESTION);
        database.gameSuggestionDao().insertGameSuggestion(new GameSuggestionEntity(gameSuggestion.gameEveningId, gameSuggestion.gameName));
    }

    private void getAllGameSuggestions(Intent intent) {
        ArrayList<GameSuggestion> gameSuggestionList = new ArrayList<>();
        GameEveningDatabase database = getDatabase();
        Long gameEveningId = (Long) intent.getExtras().get(GAME_EVENING_ID);

        List<GameSuggestionEntity> gameSuggestionEntities = database.gameSuggestionDao().getSuggestionsByGameEveningId(gameEveningId);
        for (GameSuggestionEntity entity : gameSuggestionEntities) {
            List<UserGameSuggestionEntity> userGameSuggestions = database.gameSuggestionDao().getSuggestionVotesByUsers(entity.gameSuggestionId);
            List<Long> userIdVotes = new ArrayList<>();
            for (UserGameSuggestionEntity voteEntity : userGameSuggestions) {
                userIdVotes.add(voteEntity.userId);
            }
            gameSuggestionList.add(new GameSuggestion(entity.gameSuggestionId, entity.gameEvening, entity.gameName, userIdVotes));
        }
        sendResponse(GET_GAME_EVENING_SUGGESTIONS_RESULT, intent, gameSuggestionList);
    }

    private void getAllGameEveningEntries(Intent intent) {
        ArrayList<GameEvening> gameEveningList = new ArrayList<>();
        GameEveningDatabase database = getDatabase();

        List<GameEveningWithUser> gameEveningEntities = database.gameEveningDao().getAll();
        for (GameEveningWithUser entity : gameEveningEntities) {
            gameEveningList.add(new GameEvening(entity.gameEveningEntity.gameEveningId,
                    entity.userEntity.firstName + " " + entity.userEntity.surname,
                    entity.userEntity.address,
                    entity.gameEveningEntity.date));
        }
        sendResponse(GET_ALL_GAME_EVENING_RESULT, intent, gameEveningList);
    }

    private void sendResponse(String resultKey, Intent intent, Serializable payload) {
        ResultReceiver rec = intent.getParcelableExtra(DataProviderService.GENERAL_RECEIVER);
        Bundle bundle = new Bundle();
        bundle.putSerializable(resultKey, payload);
        rec.send(Activity.RESULT_OK, bundle);
    }

    private GameEveningDatabase getDatabase() {
        GameEveningApplication context = (GameEveningApplication) this.getApplicationContext();
        return context.getDatabase();
    }

}