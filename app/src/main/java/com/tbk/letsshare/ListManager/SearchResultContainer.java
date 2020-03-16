package com.tbk.letsshare.ListManager;

public class SearchResultContainer {

    private int imageNum;
    private String Name;
    private String Price;
    private String Date;

    public SearchResultContainer(int imageNum) {
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

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public SearchResultContainer(int image, String name, String price, String date) {
        this.imageNum = image;
        this.Name = name;
        this.Price = price;
        this.Date = date;
    }
}