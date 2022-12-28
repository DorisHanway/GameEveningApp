package com.hanway.game.evening.app.persistence.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.TypeConverters;

import com.hanway.game.evening.app.persistence.converters.DateConverter;
import com.hanway.game.evening.app.persistence.entities.UserEntity;

import java.util.Date;

@Dao
public interface UserDao {

    @Query("SELECT * FROM UserEntity where deviceId = :id")
    UserEntity getByDeviceId(String id);

    @Query("SELECT userId FROM UserEntity ORDER BY lastHosted ASC LIMIT 1")
    Long getNextHostId();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(UserEntity entity);

    @Query("UPDATE UserEntity SET lastHosted=:lastHosted WHERE userId = :userId")
    @TypeConverters(DateConverter.class)
    void updateLastHostedForUserId(Long userId, Date lastHosted);

}
