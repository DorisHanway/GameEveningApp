package com.hanway.game.evening.app.persistence.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(primaryKeys = {"userId", "gameSuggestionId"})
public class UserGameSuggestionEntity {
    @NonNull
    @ColumnInfo
    public Long userId;

    @NonNull
    @ColumnInfo
    public Long gameSuggestionId;

    public UserGameSuggestionEntity(@NonNull Long userId, @NonNull Long gameSuggestionId) {
        this.userId = userId;
        this.gameSuggestionId = gameSuggestionId;
    }
}
