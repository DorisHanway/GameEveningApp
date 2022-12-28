package com.hanway.game.evening.app.persistence.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.hanway.game.evening.app.persistence.entities.GameSuggestionEntity;
import com.hanway.game.evening.app.persistence.entities.UserGameSuggestionEntity;

import java.util.List;

@Dao
public interface GameSuggestionDao {

    @Query("SELECT * FROM GameSuggestionEntity where gameEvening = :id")
    List<GameSuggestionEntity> getSuggestionsByGameEveningId(Long id);

    @Query("SELECT * FROM UserGameSuggestionEntity where gameSuggestionId = :id")
    List<UserGameSuggestionEntity> getSuggestionVotesByUsers(Long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUserGameSuggestionEntity(UserGameSuggestionEntity entity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertGameSuggestion(GameSuggestionEntity entity);

}
