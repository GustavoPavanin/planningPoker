package com.planning.poker.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Result {
    private Double media;
    private Double mediana;
    private String moda;
    private boolean viewResults = false;

    public boolean hasValue(){
        return media != null;
    }

    public Result(){}
}
