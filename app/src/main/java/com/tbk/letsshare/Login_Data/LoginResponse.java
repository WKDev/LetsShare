package com.tbk.letsshare.Login_Data;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private String message;

    @SerializedName("userId")
    private String userId;

    public int getCode(){
        return code;
    }

    public String getMessage(){
        return message;
    }

    public String getUserId(){
        return userId;
    }

}
