package com.planning.poker.controller;

import com.planning.poker.model.Room;
import com.planning.poker.model.UserJoin;
import com.planning.poker.services.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import static com.planning.poker.util.Utils.isNotNull;

@Controller
public class UserController {

    @Autowired
    private SimpMessageSendingOperations message;
    @Autowired
    private ContentService contentService;

    @MessageMapping("/joinRoom")
    @SendTo("/topic/joinned")
    public Room join(UserJoin userJoin, SimpMessageHeaderAccessor accessor){
        return contentService.joinRoom(userJoin, accessor);
    }

    @MessageMapping("/vote")
    @SendTo("/topic/response")
    public Room vote(@Payload String vote, SimpMessageHeaderAccessor accessor){
        return contentService.vote(vote, accessor);
    }

    @MessageMapping("/revealResult")
    @SendTo("/topic/response")
    public Room revealResult(@Payload Integer roomId){
        return contentService.revealResult(roomId);
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
        Room room = contentService.disconnect(wrap, message);
        if(isNotNull(room)){
            message.convertAndSend("/topic/response", room);
        }
    }


}
