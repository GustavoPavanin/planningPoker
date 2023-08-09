package com.planning.poker.util;

public class Utils {
    public static boolean isNull(Object object){
        return object == null;
    }

    public static boolean isNotNull(Object object){
        return !isNull(object);
    }
}
