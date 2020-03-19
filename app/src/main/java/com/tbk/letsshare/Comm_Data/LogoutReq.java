package com.tbk.letsshare.Comm_Data;

import com.google.gson.annotations.SerializedName;

public class LogoutReq {
    @SerializedName("_id")
    private String userId;



    public LogoutReq(String userId) {
        this.userId = userId;
    }
}
