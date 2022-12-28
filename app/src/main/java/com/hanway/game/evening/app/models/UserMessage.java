package com.hanway.game.evening.app.models;

import java.io.Serializable;
import java.util.Date;

public class UserMessage implements Serializable {
    public Long userMessageId;
    public String senderName;
    public String message;
    public Date timestamp;

    public UserMessage(Long userMessageId, String senderName, String message, Date timestamp) {
        this.userMessageId = userMessageId;
        this.senderName = senderName;
        this.message = message;
        this.timestamp = timestamp;
    }
}
