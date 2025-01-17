package com.planning.poker.model.dto;

import com.planning.poker.model.Room;
import lombok.Data;

@Data
public class RoomDto {
    private Integer id;
    private String name;

    public RoomDto(Room room) {
        this.id = room.getId();
        this.name = room.getName();
    }
}
