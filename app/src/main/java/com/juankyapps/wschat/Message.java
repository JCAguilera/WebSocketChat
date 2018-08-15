package com.juankyapps.wschat;

import com.google.gson.Gson;

import java.util.Date;

public class Message {
    String username;
    Date timestamp;
    String data;
    public Message(String username, Date timestamp, String data) {
        this.username = username;
        this.timestamp = timestamp;
        this.data = data;
    }
    public MessageReciver toMessageReciever() {
        Long mili = this.timestamp.getTime();
        return new MessageReciver(this.username, mili.toString(), this.data);
    }
}

/**
 * Message with propeties as String
 */
class MessageReciver {
    String username;
    String timestamp;
    String data;

    /**
     * Builds a MessageReciever Object. Use when parsing json.
     * @param username Username
     * @param timestamp Date in milliseconds
     * @param data Data
     */
    public MessageReciver(String username, String timestamp, String data) {
        this.username = username;
        this.timestamp = timestamp;
        this.data = data;
    }
    public Message toMessage() {
        return new Message(this.username, new Date(Long.parseLong(this.timestamp)), this.data);
    }
}