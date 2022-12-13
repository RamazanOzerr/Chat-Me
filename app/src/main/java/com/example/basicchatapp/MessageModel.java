package com.example.basicchatapp;

import java.util.List;

public class MessageModel {

    String userID, otherID, textType, date, message, from;
    Boolean seen;
    List<MessageModel> messageModelList;

    public MessageModel(){}

    public MessageModel(String userID, String otherID, String textType, String date,
                        String message, Boolean seen, List<MessageModel> messageModelList, String from) {

        this.userID = userID;
        this.otherID = otherID;
        this.textType = textType;
        this.date = date;
        this.message = message;
        this.seen = seen;
        this.messageModelList = messageModelList;
        this.from = from;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public List<MessageModel> getMessageModelList() {
        return messageModelList;
    }

    public void setMessageModelList(List<MessageModel> messageModelList) {
        this.messageModelList = messageModelList;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getOtherID() {
        return otherID;
    }

    public void setOtherID(String otherID) {
        this.otherID = otherID;
    }

    public String getTextType() {
        return textType;
    }

    public void setTextType(String textType) {
        this.textType = textType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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
                "userID='" + userID + '\'' +
                ", otherID='" + otherID + '\'' +
                ", textType='" + textType + '\'' +
                ", date='" + date + '\'' +
                ", message='" + message + '\'' +
                ", from='" + from + '\'' +
                ", seen=" + seen +
                ", messageModelList=" + messageModelList +
                '}';
    }
}
