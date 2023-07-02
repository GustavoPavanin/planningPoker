package com.planning.poker.model;

import com.planning.poker.enums.ActivityType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Activity {
    private User user;
    private ActivityType type;
}
