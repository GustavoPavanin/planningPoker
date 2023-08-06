package com.planning.poker.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
    private String id;
    private String userName;
    private Double vote;
    private Integer roomId;
    private boolean revealCard;
}
