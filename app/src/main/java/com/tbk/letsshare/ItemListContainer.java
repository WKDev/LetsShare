package com.tbk.letsshare;

public class ItemListContainer {

    private String Name;
    private String Price;
    private String Date;

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

    public ItemListContainer(String name, String price, String date) {
        this.Name = name;
        Price = price;
        Date = date;
    }
}