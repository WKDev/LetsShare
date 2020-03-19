package com.tbk.letsshare.Comm_Data;

import com.google.gson.annotations.SerializedName;

public class LogoutRes {
    @SerializedName("code")
    private int code;

    public int getCode() {
        return code;
    }
}
