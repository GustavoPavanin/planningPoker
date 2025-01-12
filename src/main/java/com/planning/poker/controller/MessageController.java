package com.planning.poker.controller;

import com.planning.poker.model.*;
import com.planning.poker.model.dto.RoomDto;
import com.planning.poker.services.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class MessageController {
    @Autowired
    private SimpMessageSendingOperations message;
    @Autowired
    private ContentService contentService;

    @MessageMapping("/createRoom")
    @SendTo("/topic/roomCreated")
    public Room createRoom(@Payload Room room){
        return contentService.createRoom(room, message);
    }

    @MessageMapping("/getRoomInfo")
    @SendTo("/topic/response")
    public Room getRoomInfo(@Payload Integer roomId){
        return contentService.getRoomInfo(roomId);
    }



}
