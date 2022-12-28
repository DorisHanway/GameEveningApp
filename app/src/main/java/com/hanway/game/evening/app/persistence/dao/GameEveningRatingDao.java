package com.hanway.game.evening.app.persistence.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.hanway.game.evening.app.persistence.entities.GameEveningRatingEntity;

import java.util.List;

@Dao
public interface GameEveningRatingDao {

    @Query("SELECT * FROM GameEveningRatingEntity where gameEvening = :id")
    List<GameEveningRatingEntity> getAllForGameEveningId(Long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertGameEveningRating(GameEveningRatingEntity entity);

}
