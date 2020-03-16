package com.tbk.letsshare.Comm_Data;

import com.google.gson.annotations.SerializedName;

public class SearchRequest {
    @SerializedName("search_title")
    private String searchTitle;

    public String getSearchTitle() {
        return searchTitle;
    }

    public SearchRequest(String searchTitle){
        this.searchTitle = searchTitle;
    }
}
