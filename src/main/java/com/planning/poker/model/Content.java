package com.planning.poker.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Content {
    private List<Activity> activities = new ArrayList<>();
    private List<User> users = new ArrayList<>();
    private Double value;

}
