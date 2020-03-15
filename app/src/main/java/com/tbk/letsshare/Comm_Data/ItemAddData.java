package com.tbk.letsshare.Comm_Data;

import com.google.gson.annotations.SerializedName;

public class ItemAddData {
    @SerializedName("itemTitle")
    private String itemTitle;
    @SerializedName("itemPrice")
    private String itemPrice;
    @SerializedName("itemDescription")
    private String itemDescription;
    @SerializedName("writer")
    private String writer;

    public ItemAddData(String itemTitle, String itemPrice, String itemDescription, String writer){
        this.itemTitle = itemTitle;
        this.itemPrice = itemPrice;
        this.itemDescription = itemDescription;
        this.writer = writer;
    }
}
