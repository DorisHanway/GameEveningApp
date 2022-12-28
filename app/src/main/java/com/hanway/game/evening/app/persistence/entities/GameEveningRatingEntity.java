package com.hanway.game.evening.app.persistence.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = {
        @ForeignKey(entity = GameEveningEntity.class, parentColumns = "gameEveningId", childColumns = "gameEvening"),
        @ForeignKey(entity = UserEntity.class, parentColumns = "userId", childColumns = "user")}
)
public class GameEveningRatingEntity {
    @ColumnInfo
    @PrimaryKey(autoGenerate = true)
    public Long gameEveningRatingId;

    @ColumnInfo
    public Long gameEvening;

    @ColumnInfo
    public Long user;

    @ColumnInfo
    public float foodRating;

    @ColumnInfo
    public float hostRating;

    @ColumnInfo
    public float eveningRating;

    public GameEveningRatingEntity(Long gameEvening, Long user, float foodRating, float hostRating, float eveningRating) {
        this.gameEvening = gameEvening;
        this.user = user;
        this.foodRating = foodRating;
        this.hostRating = hostRating;
        this.eveningRating = eveningRating;
    }
}
