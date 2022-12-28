package com.hanway.game.evening.app.persistence;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.hanway.game.evening.app.persistence.dao.GameEveningDao;
import com.hanway.game.evening.app.persistence.dao.GameEveningRatingDao;
import com.hanway.game.evening.app.persistence.dao.GameSuggestionDao;
import com.hanway.game.evening.app.persistence.dao.UserDao;
import com.hanway.game.evening.app.persistence.dao.UserMessageDao;
import com.hanway.game.evening.app.persistence.entities.GameEveningEntity;
import com.hanway.game.evening.app.persistence.entities.GameEveningRatingEntity;
import com.hanway.game.evening.app.persistence.entities.GameSuggestionEntity;
import com.hanway.game.evening.app.persistence.entities.UserEntity;
import com.hanway.game.evening.app.persistence.entities.UserGameSuggestionEntity;
import com.hanway.game.evening.app.persistence.entities.UserMessageEntity;

@Database(entities = {GameEveningEntity.class, UserEntity.class, GameSuggestionEntity.class,
        UserGameSuggestionEntity.class, GameEveningRatingEntity.class, UserMessageEntity.class}
        , version = 1)
public abstract class GameEveningDatabase extends RoomDatabase {

    public abstract GameEveningDao gameEveningDao();

    public abstract GameEveningRatingDao gameEveningRatingDao();

    public abstract UserDao userDao();

    public abstract GameSuggestionDao gameSuggestionDao();

    public abstract UserMessageDao userMessageDao();

    public static final String NAME = "GameEveningDatabase";

}