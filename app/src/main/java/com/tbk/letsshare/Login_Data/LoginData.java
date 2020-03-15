package com.tbk.letsshare.Login_Data;

import com.google.gson.annotations.SerializedName;

public class LoginData {
    @SerializedName("userEmail")
    private String userEmail;
    @SerializedName("userPwd")
    private String userPwd;

    public LoginData(String userEmail, String userPwd){
        this.userEmail = userEmail;
        this.userPwd = userPwd;
    }
}
