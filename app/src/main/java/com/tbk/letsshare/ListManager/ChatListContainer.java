package com.tbk.letsshare.ListManager;

public class ChatListContainer {
//req : buyer_id, mode
//res : chat_room_id, seller_id,last_statement

    private String roomId;
    private String roomTitle; //seller_id
    private String last_statement;

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomTitle() {
        return roomTitle;
    }

    public void setRoomTitle(String roomTitle) {
        this.roomTitle = roomTitle;
    }

    public String getLast_statement() {
        return last_statement;
    }

    public void setLast_statement(String last_statement) {
        this.last_statement = last_statement;
    }

}