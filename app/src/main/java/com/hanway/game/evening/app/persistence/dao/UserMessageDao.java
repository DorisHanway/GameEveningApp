package com.hanway.game.evening.app.persistence.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.hanway.game.evening.app.persistence.entities.UserMessageEntity;

import java.util.List;

@Dao
public interface UserMessageDao {

    @Query("SELECT * FROM UserMessageEntity")
    List<UserMessageEntity> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUserMessage(UserMessageEntity entity);
}
