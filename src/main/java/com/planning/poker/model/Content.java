package com.planning.poker.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Data
public class Content {
    private List<Room> rooms = new ArrayList<>();

    public Room getRoomById(Integer id){
        Optional<Room> optionalRoom = rooms.stream().filter(room -> room.getId().equals(id)).findFirst();
        return optionalRoom.orElse(null);
    }
}
