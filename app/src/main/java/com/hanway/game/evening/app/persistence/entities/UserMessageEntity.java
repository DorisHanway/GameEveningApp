package com.hanway.game.evening.app.persistence.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.hanway.game.evening.app.persistence.converters.DateConverter;

import java.util.Date;

@Entity
public class UserMessageEntity {

    @ColumnInfo
    @PrimaryKey(autoGenerate = true)
    public Long userMessageId;

    @ColumnInfo
    public String senderName;

    @ColumnInfo
    public String message;

    @ColumnInfo
    @TypeConverters(DateConverter.class)
    public java.util.Date timestamp;

    public UserMessageEntity(String senderName, String message, Date timestamp) {
        this.senderName = senderName;
        this.message = message;
        this.timestamp = timestamp;
    }
}
