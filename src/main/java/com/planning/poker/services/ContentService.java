package com.planning.poker.services;

import com.planning.poker.model.*;
import com.planning.poker.model.dto.RoomDto;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.planning.poker.util.Utils.*;

@Service
public class ContentService {

    public Room createRoom(Content content, Room room, SimpMessageSendingOperations message){
        room.setId(content.nextId());
        room.setResult(new Result());
        content.getRooms().add(room);
        updateHall(content, message);
        return room;
    }

    public Room getRoomInfo(Content content, Integer roomId){
        return content.getRoomById(roomId);
    }

    public Room vote(Content content, String vote, SimpMessageHeaderAccessor accessor) {
        User user = (User) Objects.requireNonNull(accessor.getSessionAttributes()).get("user");
        Room room = content.getRoomById(user.getRoomId());
        for (User currentUser: room.getUsers()) {
            if(currentUser.getId().equals(user.getId())){
                currentUser.setVote(vote);
            }
        }
        return room;
    }
    public Room revealResult(Content content, Integer roomId){
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

    public Room joinRoom(Content content, UserJoin userJoin, SimpMessageHeaderAccessor accessor) {
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

    public Room disconnect(Content content,StompHeaderAccessor wrap, SimpMessageSendingOperations message) {
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
            closeRoomIfNeeds(content, room, message);
            return room;
        }
        return null;
    }

    public void updateHall(Content content, SimpMessageSendingOperations message){
        message.convertAndSend("/topic/getRoomForHall", getRoomForHall(content));
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

    private void closeRoomIfNeeds(Content content, Room room, SimpMessageSendingOperations message){
        if(room.getUsers().isEmpty()){
            content.getRooms().remove(room);
            updateHall(content, message);
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

    private Double calculateMean(List<Double> votes) {
        double sum = 0;
        for (Double vote : votes) {
            sum += vote;
        }
        return formatDouble(divide(sum, votes.size()));
    }

    private Double calculateMedian(List<Double> votes) {

        List<Double> sortedValues = votes.stream()
                .sorted().collect(Collectors.toList());
        int size = sortedValues.size();
        int midIndex = getMidOfSize(size);
        return votes.size() > 0 ? calculateMedianPairOrOdd(sortedValues, size, midIndex) : 0;
    }

    private double calculateMedianPairOrOdd(List<Double> sortedValues, int size, int midIndex) {
        return size % 2 == 0 ? medianOdd(sortedValues, midIndex) : sortedValues.get(midIndex);
    }

    private double medianOdd(List<Double> sortedValues, int midIndex) {
        return (sortedValues.get(midIndex - 1) + sortedValues.get(midIndex)) / 2;
    }
    private double divide(Double sumVotes, int votesQuantity) {
        return sumVotes / (double) Math.max(votesQuantity, 1);
    }

    private int getMidOfSize(int size) {
        return size / 2;
    }

    private Map<Double, Long> getFrequencyOfVotes(List<Double> votes){
        return votes.stream()
                .collect(Collectors.groupingBy(vote -> vote, Collectors.counting()));
    }

    private String modeStringify(List<Double> votes){
        Map<Double, Long> FrequencyOfVotes = getFrequencyOfVotes(votes);

        List<Map.Entry<Double, Long>> moda = new ArrayList<>();
        Long maxValue = Long.MIN_VALUE;

        for (Map.Entry<Double, Long> entry : FrequencyOfVotes.entrySet()) {
            Long value = entry.getValue();
            processMode(maxValue, value, moda, entry);

        }
        return generateStringMode(moda);
    }

    private void processMode(Long maxValue, Long value, List<Map.Entry<Double, Long>> moda, Map.Entry<Double, Long> entry){
        if (value.equals(maxValue)) {
            moda.add(entry);
        }
        if (value > maxValue) {
            moda.clear();
            moda.add(entry);
            maxValue = value;
        }
    }
    private String generateStringMode(List<Map.Entry<Double, Long>> moda){
        String stringMode = "";
        if(moda.isEmpty() || moda.size() > 2){
            stringMode = "N/A";
        }else{
            stringMode = generateStringModes(moda, stringMode);
        }
        return stringMode;
    }

    private static String generateStringModes(List<Map.Entry<Double, Long>> moda, String stringMode) {
        for (Map.Entry<Double, Long> entry : moda) {
            if(!stringMode.equals("")){
                stringMode = stringMode.concat(" e ");
            }
            stringMode = stringMode.concat(String.valueOf(entry.getKey()));
        }
        return stringMode;
    }

    public List<RoomDto> getRoomForHall(Content content) {
        return content.getRooms().stream().map(RoomDto::new).collect(Collectors.toList());
    }
}
