package com.tbk.letsshare.Comm_Data;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private String message;

    public String getEmail() {
        return email;
    }

    @SerializedName("email")
    private String email;

    @SerializedName("_id")
    private String userId;

    @SerializedName("nickname")
    private String nickname;

    @SerializedName("auto_login")
    private String autoLogin;

    public String getAutoLogin() {
        return autoLogin;
    }

    public int getCode(){
        return code;
    }

    public String getMessage(){
        return message;
    }

    public String getUserId(){
        return userId;
    }

    public String getNickname() {
        return nickname;
    }
}
