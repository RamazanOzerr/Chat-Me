package com.example.basicchatapp.Utils;

import java.util.List;

public class MessageModel {

    String type, time, text, from, otherUser;
    Boolean seen;

    public MessageModel(){}

    public MessageModel(String from, Boolean seen, String text,String time, String type) {

        this.from = from;
        this.seen = seen;
        this.text = text;
        this.time = time;
        this.type = type;

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public Boolean getSeen() {
        return seen;
    }

    public void setSeen(Boolean seen) {
        this.seen = seen;
    }

    @Override
    public String toString() {
        return "MessageModel{" +
                "type='" + type + '\'' +
                ", time='" + time + '\'' +
                ", text='" + text + '\'' +
                ", from='" + from + '\'' +
                ", seen=" + seen +
                '}';
    }
}
