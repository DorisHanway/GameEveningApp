package com.hanway.game.evening.app.persistence.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity=GameEveningEntity.class, parentColumns="gameEveningId", childColumns="gameEvening"))
public class GameSuggestionEntity {
    @ColumnInfo
    @PrimaryKey(autoGenerate=true)
    public Long gameSuggestionId;

    @ColumnInfo
    public Long gameEvening;

    @ColumnInfo
    public String gameName;

    public GameSuggestionEntity(Long gameEvening, String gameName){
        this.gameEvening = gameEvening;
        this.gameName = gameName;
    }

}
