package com.tbk.letsshare.Comm_Data;

import com.google.gson.annotations.SerializedName;

//req : buyer_id, mode
//res : chat_room_id, seller_id,last_statement
public class ChatRoomReq {
    public ChatRoomReq(String buyerId, String mode) {
        this.buyerId = buyerId;
        this.mode = mode;
    }

    @SerializedName("buyer_id")
    private String buyerId;
    @SerializedName("mode")
    private String mode;
}
