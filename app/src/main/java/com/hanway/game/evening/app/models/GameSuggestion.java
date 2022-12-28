package com.hanway.game.evening.app.models;

import java.io.Serializable;
import java.util.List;

public class GameSuggestion implements Serializable {
    public Long id;
    public Long gameEveningId;
    public String gameName;
    public List<Long> votedByUsers;

    public GameSuggestion(Long id, Long gameEveningId, String gameName, List<Long> votedByUsers){
        this.id = id;
        this.gameEveningId = gameEveningId;
        this.gameName = gameName;
        this.votedByUsers = votedByUsers;
    }
}
