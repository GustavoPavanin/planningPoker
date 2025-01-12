package com.planning.poker.services;

import com.planning.poker.model.*;
import com.planning.poker.model.dto.RoomDto;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.planning.poker.util.MathUtils.*;
import static com.planning.poker.util.Utils.*;

@Service
public class ContentService {

    private final Content content = new Content();

    public Room createRoom(Room room, SimpMessageSendingOperations message){
        room.setId(content.nextId());
        room.setResult(new Result());
        content.getRooms().add(room);
        return room;
    }

    public Room getRoomInfo(Integer roomId){
        return content.getRoomById(roomId);
    }

    public Room vote(String vote, SimpMessageHeaderAccessor accessor) {
        User user = (User) Objects.requireNonNull(accessor.getSessionAttributes()).get("user");
        Room room = content.getRoomById(user.getRoomId());
        for (User currentUser: room.getUsers()) {
            if(currentUser.getId().equals(user.getId())){
                currentUser.setVote(vote);
            }
        }
        return room;
    }
    public Room revealResult(Integer roomId){
        Room room = content.getRoomById(roomId);
        List<Double> votes = new ArrayList<>();
        if(isNull(room.getResult()) || !room.getResult().hasValue()){
            getValidVotes(room, votes);
            room.setResult(new Result(calculateMean(votes),calculateMedian(votes), modeStringify(votes), true));
        } else{
            resetResultAndVotes(room);
        }
        return room;
    }

    private void getValidVotes(Room room, List<Double> votes) {
        for (User user: room.getUsers()) {
            Double vote = user.getVote();
            if(isValidVote(vote)){
                votes.add(vote);
            }
        }
    }

    public Room joinRoom(UserJoin userJoin, SimpMessageHeaderAccessor accessor) {
        Integer roomId = userJoin.getRoomIdInteger();
        Room room = content.getRoomById(roomId);
        if(isNotNull(room)){
            User user = userJoin.getUser();
            if(room.isEmpty()){
                user.setOwner();
            }
            room.getUsers().add(user);
            accessor.getSessionAttributes().put("user", user);
        }
        return room;
    }

    public Room disconnect(StompHeaderAccessor wrap, SimpMessageSendingOperations message) {
        if(wrap.getSessionAttributes().containsKey("user")){
            User user = (User) wrap.getSessionAttributes().get("user");
            Room room = content.getRoomById(user.getRoomId());
            if(isNull(room)){
                return null;
            }
            User userToRemove;
            userToRemove = getUserToRemove(room, user);
            room.getUsers().remove(userToRemove);
            room.verifyOwner();
            closeRoomIfNeeds(room, message);
            return room;
        }
        return null;
    }

    public List<RoomDto> getAllRooms(){
        return content.getRooms().stream().map(RoomDto::new).collect(Collectors.toList());
    }

    private User getUserToRemove(Room room, User user) {
        User userToRemove = null;
        for (User currentUser: room.getUsers()) {
            if(currentUser.getId().equals(user.getId())){
                userToRemove = currentUser;
            }
        }
        return userToRemove;
    }

    private void closeRoomIfNeeds(Room room, SimpMessageSendingOperations message){
        if(room.getUsers().isEmpty()){
            content.getRooms().remove(room);
        }
    }

    private boolean isValidVote(Double vote) {
        return  isNotNull(vote) && vote > -1;
    }

    private void resetResultAndVotes(Room room) {
        room.setResult(new Result());
        for (User user: room.getUsers()) {
            user.setVote(STRING_NULL);
        }
    }
}
