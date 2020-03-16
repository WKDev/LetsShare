package com.tbk.letsshare.Comm_Data;

import com.google.gson.annotations.SerializedName;

public class ItemDataResponse {

    @SerializedName("parsed_title")
    public String parsedTitle;

    @SerializedName("parsed_price")
    public String parsedPrice;

    @SerializedName("parsed_description")
    public String parsedDescription;

    public String getParsedTitle() {
        return parsedTitle;
    }

    public String getParsedPrice() {
        return parsedPrice;
    }

    public String getParsedDescription() {
        return parsedDescription;
    }

    public String getParsedWriter() {
        return parsedWriter;
    }

    @SerializedName("parsed_writer")
    public String parsedWriter;

}
