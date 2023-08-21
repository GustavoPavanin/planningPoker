package com.planning.poker.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Room {
    private Integer id;
    private String name;
    private Integer voteSystem;
    private Result result;
    private List<User> users = new ArrayList<>();
    public void generateId(Integer value) {
        this.setId(value+1);
    }

    Room(String name, Integer voteSystem, Integer roomNumber){
         this.name = name;
         this.voteSystem = voteSystem;
         this.result = new Result();
    }
}

