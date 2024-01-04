package com.planning.poker.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import static com.planning.poker.util.Utils.isNull;

@Data
@AllArgsConstructor
public class User {
    private String id;
    private String userName;
    private Double vote;
    private Integer roomId;
    private boolean revealCard;
    private boolean owner;

    public void setVote(String vote){
        vote = vote.replace("\"", "");
        if(isNull(vote)){
            this.vote = null;
        } else if (vote.equals("?")) {
            this.vote = -1d;
        } else if(vote.equals("1/2")) {
            this.vote = 0.5;
        }else{
            this.vote = Double.valueOf(vote);
        }
    }

    public void setOwner() {
        this.owner = true;
    }
}
