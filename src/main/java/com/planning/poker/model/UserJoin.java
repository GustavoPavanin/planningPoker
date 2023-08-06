package com.planning.poker.model;

import lombok.Data;

import java.util.UUID;

@Data
public class UserJoin {
    private String nickName;
    private String roomId;

    public Integer getRoomIdInteger(){
        return Integer.parseInt(this.roomId);
    }

    public User getUser(){
        return new User(UUID.randomUUID().toString(),nickName,null,getRoomIdInteger( ),false);
    }
}


