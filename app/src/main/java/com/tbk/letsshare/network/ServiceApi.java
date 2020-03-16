package com.tbk.letsshare.network;

import com.google.gson.annotations.SerializedName;
import com.tbk.letsshare.Comm_Data.ItemAddData;
import com.tbk.letsshare.Comm_Data.ItemAddResponse;
import com.tbk.letsshare.Comm_Data.ItemDataResponse;
import com.tbk.letsshare.Comm_Data.JoinData;
import com.tbk.letsshare.Comm_Data.JoinResponse;
import com.tbk.letsshare.Comm_Data.LoginData;
import com.tbk.letsshare.Comm_Data.LoginResponse;
import com.tbk.letsshare.Comm_Data.SearchRequest;
import com.tbk.letsshare.Comm_Data.SearchResult;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ServiceApi {

    @POST("/user/join")
    Call<JoinResponse> userJoin(@Body JoinData data);

    @POST("/user/board")
    Call<ItemAddResponse> itemAdd(@Body ItemAddData data);

    @GET("/user/find")
    Call<List<ItemDataResponse>> importItem();

    @POST("/user/login")
    Call<LoginResponse> userLogin(@Body LoginData data);

    @POST("/user/search")
    Call<List<SearchResult>> searchItem(@Body SearchRequest data);
}
