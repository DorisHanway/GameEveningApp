package com.hanway.game.evening.app.persistence.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.TypeConverters;

import com.hanway.game.evening.app.persistence.converters.DateConverter;
import com.hanway.game.evening.app.persistence.entities.GameEveningEntity;
import com.hanway.game.evening.app.persistence.entities.GameEveningWithUser;

import java.util.Date;
import java.util.List;

@Dao
public interface GameEveningDao {

    @Query("SELECT GameEveningEntity.*, UserEntity.* FROM GameEveningEntity INNER JOIN UserEntity " +
            "ON GameEveningEntity.hostId = UserEntity.userId")
    List<GameEveningWithUser> getAll();

    @Query("SELECT date FROM GameEveningEntity ORDER BY date DESC LIMIT 1")
    @TypeConverters(DateConverter.class)
    Date getLatestGameEveningDate();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertGameEvening(GameEveningEntity entity);
}
