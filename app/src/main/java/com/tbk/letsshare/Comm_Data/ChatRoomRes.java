package com.tbk.letsshare.Comm_Data;

import com.google.gson.annotations.SerializedName;

//req : buyer_id, mode
//res : chat_room_id, seller_id,last_statement
public class ChatRoomRes {
    @SerializedName("chat_room_id")
    private String chatRoomId;

    @SerializedName("seller_id")
    private String seller_id;

    @SerializedName("last_statement")
    private String lastStatement;

    public String getChatRoomId() {
        return chatRoomId;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public String getLastStatement() {
        return lastStatement;
    }
}
