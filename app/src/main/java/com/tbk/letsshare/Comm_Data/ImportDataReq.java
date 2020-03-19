package com.tbk.letsshare.Comm_Data;

import com.google.gson.annotations.SerializedName;

public class ImportDataReq {
    @SerializedName("_id")
    private String userId;

    public ImportDataReq(String userId) {
        this.userId = userId;
    }
}
