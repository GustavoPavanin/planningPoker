package com.planning.poker.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class Utils {

    public static String STRING_NULL = "null";
    public static boolean isNull(Object object){
        return object == null || object.equals("null");
    }

    public static boolean isNotNull(Object object){
        return !isNull(object);
    }

    public static double formatDouble(double value) {
        Locale locale = new Locale("en", "US"); // Defina o Locale desejado (neste caso, inglês dos EUA)
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale);
        symbols.setDecimalSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat("#.00", symbols);
        return Double.parseDouble(decimalFormat.format(value));
    }
}
