package com.planning.poker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    private Double media;
    private Double mediana;
    private String moda;
    private boolean viewResults = false;

    public boolean hasValue(){
        return media != null;
    }
}
