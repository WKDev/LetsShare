package com.tbk.letsshare.Comm_Data;

import com.google.gson.annotations.SerializedName;

public class verifyStateReq {
    private String use;
    @SerializedName("_id")
    private String userId;
    @SerializedName("set_state")
    private String stateToSet;

    public String getUse() {
        return use;
    }

    public String getStateToSet() {
        return stateToSet;
    }

    public verifyStateReq(String userId, String stateToSet, String use) {
        this.userId = userId;
        this.stateToSet = stateToSet;
        this.use = use;
    }
}

