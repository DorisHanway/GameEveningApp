package com.hanway.game.evening.app.models;

import java.io.Serializable;
import java.util.Date;

public class GameEvening implements Serializable {
    public Long id;
    public String host;
    public String location;
    public Date date;

    public GameEvening(Long id, String host, String location, Date date) {
        this.id = id;
        this.host = host;
        this.location = location;
        this.date = date;
    }

    public boolean isUpcoming() {
        return date.after(new Date());
    }
}
