package com.planning.poker.controller;

import com.planning.poker.enums.ActivityType;
import com.planning.poker.model.*;
import com.planning.poker.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import static com.planning.poker.util.Utils.isNotNull;

@Controller
public class MessageController {

    private static final Content CONTENT = new Content();

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private SimpMessageSendingOperations message;

    @MessageMapping("/joinRoom")
    @SendTo("/topic/joinned")
    public Room join(UserJoin userJoin, SimpMessageHeaderAccessor accessor){
        Integer roomId = userJoin.getRoomIdInteger();
        Room room = CONTENT.getRoomById(roomId);
        if(isNotNull(room)){
            User user = userJoin.getUser();
            room.getUsers().add(user);
            room.getActivities().add(new Activity(user, ActivityType.CONNECT));
            accessor.getSessionAttributes().put("user", user);
        }
        return room;
    }

    @MessageMapping("/createRoom")
    @SendTo("/topic/roomCreated")
    public Room createRoom(@Payload Room room, SimpMessageHeaderAccessor accessor){
        room.generateId(CONTENT.getRooms().size());
        CONTENT.getRooms().add(room);
        return room;
    }

    @MessageMapping("/getRoomInfo")
    @SendTo("/topic/response")
    public Room getRoomInfo(@Payload Integer roomId, SimpMessageHeaderAccessor accessor){
        return CONTENT.getRoomById(roomId);
    }

    @EventListener
    public void socketDisconnect(SessionDisconnectEvent event){
        StompHeaderAccessor wrap = StompHeaderAccessor.wrap(event.getMessage());
        disconnectUser(wrap);
    }

    @EventListener
    public void socketUnsubscribe(SessionUnsubscribeEvent event){
        StompHeaderAccessor wrap = StompHeaderAccessor.wrap(event.getMessage());
        disconnectUser(wrap);
    }

    private void disconnectUser(StompHeaderAccessor wrap) {
        if(wrap.getSessionAttributes().containsKey("user")){
            Room room = new Room();
            User user = (User) wrap.getSessionAttributes().get("user");
            room = CONTENT.getRoomById(user.getRoomId());
            User userToRemove = null;
            for (User currentUser: room.getUsers()) {
                if(currentUser.getId().equals(user.getId())){
                    userToRemove = currentUser;
                    room.getActivities().add(new Activity(user, ActivityType.DISCONNECT));
                }
            }
            room.getUsers().remove(userToRemove);
            message.convertAndSend("/topic/response", room);
        }
    }
}
