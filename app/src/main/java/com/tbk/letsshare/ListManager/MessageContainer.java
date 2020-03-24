package com.tbk.letsshare.ListManager;

public class MessageContainer {
    private String message;
    private String date;
    private String writer;

    public MessageContainer(String message, String date, String writer) {
        this.message = message;
        this.date = date;
        this.writer = writer;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }
}
