package com.tbk.letsshare.Comm_Data;

import com.google.gson.annotations.SerializedName;

public class ItemAddResponse {
    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private String message;

    private String path;

    public int getCode() {
        return code;
    }

    public String getAddMessage() {
        return message;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }

}

