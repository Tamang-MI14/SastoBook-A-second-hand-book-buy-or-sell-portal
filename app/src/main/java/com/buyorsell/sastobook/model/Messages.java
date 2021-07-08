package com.buyorsell.sastobook.model;

public class Messages {
    String sender;
    String reveiver;
    String message;
    String email;

    public Messages() {
    }

    public Messages(String sender, String reveiver, String message, String email) {
        this.sender = sender;
        this.reveiver = reveiver;
        this.message = message;
        this.email = email;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReveiver() {
        return reveiver;
    }

    public void setReveiver(String reveiver) {
        this.reveiver = reveiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}