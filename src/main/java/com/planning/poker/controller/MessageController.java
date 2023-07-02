package com.planning.poker.controller;

import com.planning.poker.enums.ActivityType;
import com.planning.poker.model.Activity;
import com.planning.poker.model.Content;
import com.planning.poker.model.User;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
public class MessageController {

    private static final Content CONTENT = new Content();

    @MessageMapping("/join")
    @SendTo("/topic/response")
    @CrossOrigin(origins = "http://localhost:3000")
    public void join(@Payload User user, SimpMessageHeaderAccessor accessor){
        CONTENT.getUsers().add(user);
        CONTENT.getActivities().add(new Activity(user, ActivityType.CONNECT));
        System.out.println(CONTENT.toString());
    }

}
