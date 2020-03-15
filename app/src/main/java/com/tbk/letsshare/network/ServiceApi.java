package com.tbk.letsshare.network;

import com.tbk.letsshare.Comm_Data.JoinData;
import com.tbk.letsshare.Comm_Data.JoinResponse;
import com.tbk.letsshare.Comm_Data.LoginData;
import com.tbk.letsshare.Comm_Data.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ServiceApi {
    @POST("/user/login")
    Call<LoginResponse> userLogin(@Body LoginData data);

    @POST("/user/join")
    Call<JoinResponse> userJoin(@Body JoinData data);
}
