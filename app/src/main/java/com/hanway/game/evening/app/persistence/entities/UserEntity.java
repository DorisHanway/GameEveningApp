package com.hanway.game.evening.app.persistence.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.hanway.game.evening.app.persistence.converters.DateConverter;

import java.util.Date;

@Entity
public class UserEntity {

    @ColumnInfo
    @PrimaryKey(autoGenerate=true)
    public Long userId;

    @ColumnInfo
    public String firstName;

    @ColumnInfo
    public String surname;

    @ColumnInfo
    public String address;

    @ColumnInfo
    @TypeConverters(DateConverter.class)
    public Date lastHosted;

    @ColumnInfo
    public String deviceId;

    public UserEntity(String firstName, String surname, String address, Date lastHosted, String deviceId){
        this.firstName = firstName;
        this.surname = surname;
        this.address = address;
        this.lastHosted = lastHosted;
        this.deviceId = deviceId;
    }

}
