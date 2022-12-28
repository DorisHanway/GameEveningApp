package com.hanway.game.evening.app.persistence.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.hanway.game.evening.app.persistence.converters.DateConverter;

@Entity(foreignKeys = @ForeignKey(entity = UserEntity.class, parentColumns = "userId", childColumns = "hostId"))
public class GameEveningEntity {
    @ColumnInfo
    @PrimaryKey(autoGenerate = true)
    public Long gameEveningId;

    @ColumnInfo
    public Long hostId;

    @ColumnInfo
    @TypeConverters(DateConverter.class)
    public java.util.Date date;

    public GameEveningEntity(Long hostId, java.util.Date date) {
        this.hostId = hostId;
        this.date = date;
    }

}
