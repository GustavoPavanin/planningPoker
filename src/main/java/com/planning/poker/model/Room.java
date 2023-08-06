package com.planning.poker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Room {
    public Integer id;
    public String name;
    public Integer voteSystem;
    public List<User> users = new ArrayList<>();
    private List<Activity> activities = new ArrayList<>();
    public void generateId(Integer value) {
        this.setId(value+1);
    }

    public Room(String name, Integer voteSystem, Integer roomNumber){
         this.name = name;
         this.voteSystem = voteSystem;
    }
}

