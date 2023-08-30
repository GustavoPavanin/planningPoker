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

    Room(String name, Integer voteSystem){
         this.name = name;
         this.voteSystem = voteSystem;
         this.result = new Result();
    }
}

