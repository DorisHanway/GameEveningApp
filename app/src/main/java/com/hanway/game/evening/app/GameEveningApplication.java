package com.hanway.game.evening.app;

import android.app.Application;
import android.provider.Settings;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.hanway.game.evening.app.models.User;
import com.hanway.game.evening.app.persistence.GameEveningDatabase;
import com.hanway.game.evening.app.persistence.entities.GameEveningEntity;
import com.hanway.game.evening.app.persistence.entities.GameEveningRatingEntity;
import com.hanway.game.evening.app.persistence.entities.GameSuggestionEntity;
import com.hanway.game.evening.app.persistence.entities.UserEntity;
import com.hanway.game.evening.app.persistence.entities.UserGameSuggestionEntity;
import com.hanway.game.evening.app.persistence.entities.UserMessageEntity;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executors;

public class GameEveningApplication extends Application {

    private GameEveningDatabase database;
    private User currentUser;
    private String currentDeviceId;

    @Override
    public void onCreate() {
        super.onCreate();
        currentDeviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        // setup the callback, which should populate the DB with mock data when it is first created
        RoomDatabase.Callback rdc = new RoomDatabase.Callback() {
            public void onCreate(SupportSQLiteDatabase db) {
                super.onCreate(db);
                populateDatabase();
            }

            public void onOpen(SupportSQLiteDatabase db) {
            }
        };

        database = Room.databaseBuilder(this, GameEveningDatabase.class, GameEveningDatabase.NAME)
                .addCallback(rdc)
                .build();

        // Get the current user from the DB
        Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
            @Override
            public void run() {
                UserEntity currentUserEntity = database.userDao().getByDeviceId(currentDeviceId);
                if (currentUserEntity != null) {
                    currentUser = new User(currentUserEntity.userId, currentUserEntity.firstName, currentUserEntity.surname, currentUserEntity.address,
                            null, currentUserEntity.deviceId);
                }
            }
        });
    }

    public GameEveningDatabase getDatabase() {
        return database;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    private void populateDatabase() {
        Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
            @Override
            public void run() {
                createUsers();
                createGameEveningEntries();
                createGameEveningSuggestions();
                setCurrentUser();
                createGameRatings();
                createUserMessages();
            }

            private void createGameEveningSuggestions() {
                GameSuggestionEntity suggestion1 = new GameSuggestionEntity(1L, "Monopoly");
                GameSuggestionEntity suggestion2 = new GameSuggestionEntity(2L, "Monopoly");
                GameSuggestionEntity suggestion3 = new GameSuggestionEntity(1L, "Scrabble");
                GameSuggestionEntity suggestion4 = new GameSuggestionEntity(2L, "Risiko");
                database.gameSuggestionDao().insertGameSuggestion(suggestion1);
                database.gameSuggestionDao().insertGameSuggestion(suggestion2);
                database.gameSuggestionDao().insertGameSuggestion(suggestion3);
                database.gameSuggestionDao().insertGameSuggestion(suggestion4);

                // Add votes
                UserGameSuggestionEntity vote1 = new UserGameSuggestionEntity(1L,1L);
                UserGameSuggestionEntity vote2 = new UserGameSuggestionEntity(1L,2L);
                UserGameSuggestionEntity vote3 = new UserGameSuggestionEntity(2L,1L);
                UserGameSuggestionEntity vote4 = new UserGameSuggestionEntity(3L,1L);
                UserGameSuggestionEntity vote5 = new UserGameSuggestionEntity(2L,2L);
                UserGameSuggestionEntity vote6 = new UserGameSuggestionEntity(4L,3L);
                database.gameSuggestionDao().insertUserGameSuggestionEntity(vote1);
                database.gameSuggestionDao().insertUserGameSuggestionEntity(vote2);
                database.gameSuggestionDao().insertUserGameSuggestionEntity(vote3);
                database.gameSuggestionDao().insertUserGameSuggestionEntity(vote4);
                database.gameSuggestionDao().insertUserGameSuggestionEntity(vote5);
                database.gameSuggestionDao().insertUserGameSuggestionEntity(vote6);
            }

            private void createUserMessages() {
                UserMessageEntity userMessage1 = new UserMessageEntity("Max", "Ich komme etwas später..", new Date(System.currentTimeMillis() - 3600 * 1000));
                UserMessageEntity userMessage2 = new UserMessageEntity("Erika", "Alles klar :)", new Date(System.currentTimeMillis() - 3600 * 500));
                database.userMessageDao().insertUserMessage(userMessage1);
                database.userMessageDao().insertUserMessage(userMessage2);
            }

            private void createGameRatings() {
                GameEveningRatingEntity rating1 = new GameEveningRatingEntity(1L, 2L, 4, 3, 5);
                GameEveningRatingEntity rating2 = new GameEveningRatingEntity(2L, 3L, 2, 4, 3);
                database.gameEveningRatingDao().insertGameEveningRating(rating1);
                database.gameEveningRatingDao().insertGameEveningRating(rating2);
            }

            private void createUsers() {
                UserEntity user1 = new UserEntity("Doris", "Hanway", "Gilching", null, currentDeviceId);
                UserEntity user2 = new UserEntity("Max", "Mustermann", "Germering", null, java.util.UUID.randomUUID().toString());
                UserEntity user3 = new UserEntity("Erika", "Mustermann", "Krailling", null, java.util.UUID.randomUUID().toString());
                UserEntity user4 = new UserEntity("Frank", "Mustermann", "Pasing", null, java.util.UUID.randomUUID().toString());
                database.userDao().insertUser(user1);
                database.userDao().insertUser(user2);
                database.userDao().insertUser(user3);
                database.userDao().insertUser(user4);
            }

            private void createGameEveningEntries() {
                Calendar calender = Calendar.getInstance();
                // Every friday at 20:00
                calender.set(
                        Calendar.DAY_OF_WEEK,
                        Calendar.FRIDAY);
                calender.set(Calendar.HOUR, 8);
                calender.set(Calendar.MINUTE, 0);
                calender.set(Calendar.SECOND, 0);
                calender.set(Calendar.MILLISECOND, 0);

                int daysOffset = calender.getTime().after(new Date()) ? -14 : -7;
                calender.add(Calendar.DATE, daysOffset);
                GameEveningEntity gameEvening1 = new GameEveningEntity(1L, calender.getTime());
                database.userDao().updateLastHostedForUserId(1L, calender.getTime());

                calender.add(Calendar.DATE, 7);
                GameEveningEntity gameEvening2 = new GameEveningEntity(2L, calender.getTime());
                database.userDao().updateLastHostedForUserId(2L, calender.getTime());
                database.gameEveningDao().insertGameEvening(gameEvening1);
                database.gameEveningDao().insertGameEvening(gameEvening2);
            }

            private void setCurrentUser() {
                UserEntity currentUserEntity = database.userDao().getByDeviceId(currentDeviceId);
                currentUser = new User(currentUserEntity.userId, currentUserEntity.firstName, currentUserEntity.surname, currentUserEntity.address,
                        null, currentUserEntity.deviceId);
            }
        });
    }

}

