package com.planning.poker.controller;

import com.planning.poker.enums.ActivityType;
import com.planning.poker.model.Activity;
import com.planning.poker.model.Content;
import com.planning.poker.model.Room;
import com.planning.poker.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
public class MessageController {

    private static final Content CONTENT = new Content();

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/join")
    @SendTo("/topic/response")
    @CrossOrigin(origins = "http://localhost:3000")
    public void join(@Payload User user, SimpMessageHeaderAccessor accessor){
        //,@Payload Integer roonId
        System.out.println(user.getUserName());
//        Room room = CONTENT.getRoomById(roonId);
//        room.getUsers().add(user);
//        room.getActivities().add(new Activity(user, ActivityType.CONNECT));
//        System.out.println(CONTENT.toString());
    }

    @MessageMapping("/createRoom")
    @SendTo("/room/roomCreated")
    public Integer createRoom(@Payload Room room, SimpMessageHeaderAccessor accessor){
        room.generateId(CONTENT.getRooms().size());
        CONTENT.getRooms().add(room);
        System.out.println(CONTENT.toString());
//        room.getUsers().add(user);
//        room.getActivities().add(new Activity(user, ActivityType.CONNECT));
//        System.out.println(CONTENT.toString());
        return room.getId();
    }

}
