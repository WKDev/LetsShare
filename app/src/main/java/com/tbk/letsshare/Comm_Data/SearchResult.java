package com.tbk.letsshare.Comm_Data;

import com.google.gson.annotations.SerializedName;

public class SearchResult {
    @SerializedName("search_result_title")
    private String resultTitle;

    @SerializedName("search_result_price")
    private String resultPrice;

    @SerializedName("search_result_writer")
    private String resultWriter;

    @SerializedName("search_result_date")
    private String resultDate;

    @SerializedName("search_result_desc")
    private String resultDescription;

    public String getResultDate() {
        return resultDate;
    }

    public String getResultDescription() {
        return resultDescription;
    }

    public String getResultTitle() {
        return resultTitle;
    }

    public String getResultPrice() {
        return resultPrice;
    }

    public String getResultWriter() {
        return resultWriter;
    }
}
