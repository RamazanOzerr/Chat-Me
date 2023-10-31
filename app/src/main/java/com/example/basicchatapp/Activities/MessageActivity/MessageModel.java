package com.example.basicchatapp.Activities.MessageActivity;

import java.util.Map;

public class MessageModel {

    private String sender, text, time, type, timeStamp;
    private Long timeS;  // bunu primitive olarak tanımladığımızda db ye gitmiyordu bu değer xD
    private boolean seen;

    public MessageModel(boolean seen, String sender, String text
            , String time, String type, String timeStamp, Long timeS) {
        this.sender = sender;
        this.text = text;
        this.time = time;
        this.type = type;
        this.seen = seen;
        this.timeStamp = timeStamp;
        this.timeS = timeS;
    }

    public Long getTimeS() {
        return timeS;
    }

    public void setTimeS(Long timeS) {
        this.timeS = timeS;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }
}
