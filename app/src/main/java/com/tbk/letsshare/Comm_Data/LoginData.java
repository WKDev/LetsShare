package com.tbk.letsshare.Comm_Data;

import com.google.gson.annotations.SerializedName;

public class LoginData {
    @SerializedName("userEmail")
    private String userEmail;
    @SerializedName("userPwd")
    private String userPwd;
    @SerializedName("is_autologin")
    private String isAutoLogin;


    public LoginData(String userEmail, String userPwd,String isAutoLogin){
        this.userEmail = userEmail;
        this.userPwd = userPwd;
        this.isAutoLogin = isAutoLogin;

    }
}
