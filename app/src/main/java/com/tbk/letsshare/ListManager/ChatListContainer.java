package com.tbk.letsshare.ListManager;

public class ChatListContainer {

    private int imageNum;
    private String Name;
    private String Contents;
    private String Date;

    public ChatListContainer(int imageNum) {
    }

    public int getThumbnail() {
        return imageNum;
    }

    public void setThumbnail(int image) {
        this.imageNum = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getContents() {
        return Contents;
    }

    public void setContents(String content) {
        Contents = content;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public ChatListContainer(int image, String name, String content, String date) {
        this.imageNum = image;
        this.Name = name;
        this.Contents = content;
        this.Date = date;
    }
}