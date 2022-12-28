package com.hanway.game.evening.app.models;

import java.io.Serializable;

public class GameEveningRating implements Serializable {
    public Long id;
    public Long gameEveningId;
    public Long userId;
    public float foodRating;
    public float hostRating;
    public float eveningRating;

    public GameEveningRating(Long id, Long gameEveningId, Long userId, float foodRating, float hostRating, float eveningRating) {
        this.id = id;
        this.gameEveningId = gameEveningId;
        this.userId = userId;
        this.foodRating = foodRating;
        this.hostRating = hostRating;
        this.eveningRating = eveningRating;
    }
}
