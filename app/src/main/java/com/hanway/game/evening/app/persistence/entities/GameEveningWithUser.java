package com.hanway.game.evening.app.persistence.entities;

import androidx.room.Embedded;

public class GameEveningWithUser {

    @Embedded
    public GameEveningEntity gameEveningEntity;

    @Embedded
    public UserEntity userEntity;
}
