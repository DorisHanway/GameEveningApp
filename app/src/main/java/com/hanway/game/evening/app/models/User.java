package com.hanway.game.evening.app.models;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {

    public Long userId;
    public String firstName;
    public String surname;
    public String address;
    public Date lastHosted;
    public String deviceId;

    public User(Long userId, String firstName, String surname, String address, Date lastHosted, String deviceId) {
        this.userId = userId;
        this.firstName = firstName;
        this.surname = surname;
        this.address = address;
        this.lastHosted = lastHosted;
        this.deviceId = deviceId;
    }
}
