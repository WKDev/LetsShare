package com.tbk.letsshare.Comm_Data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ItemRequest {

    @SerializedName("Username")
    private String userName;

    public ItemRequest(String userName){
        this.userName = userName;
    }
}