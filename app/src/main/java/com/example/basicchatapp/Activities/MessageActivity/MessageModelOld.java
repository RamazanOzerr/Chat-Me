package com.example.basicchatapp.Activities.MessageActivity;

public class MessageModelOld {

    private String sender, text, time, type;
    private boolean seen;

    public MessageModelOld(boolean seen, String sender, String text, String time, String type) {
        this.seen = seen;
        this.sender = sender;
        this.text = text;
        this.time = time;
        this.type = type;
    }

    public MessageModelOld() {
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

    @Override
    public String toString() {
        return "MessageModel{" +
                "sender='" + sender + '\'' +
                ", text='" + text + '\'' +
                ", time='" + time + '\'' +
                ", type='" + type + '\'' +
                ", seen=" + seen +
                '}';
    }
}
