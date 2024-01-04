package com.planning.poker.model;

import lombok.Data;

@Data
public class UserJoin {
    private String id;
    private String nickName;
    private String roomId;

    public Integer getRoomIdInteger(){
        return Integer.parseInt(this.roomId);
    }

    public User getUser(){
        return new User(id,nickName,null,getRoomIdInteger( ),false, false);
    }
}


