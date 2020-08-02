package com.grappetite.zoya.utils;


public class NumberUtils {
    public static int toInt(String number) {
        try {
            return Integer.parseInt(number);
        }
        catch (NumberFormatException e) {
            return 0;
        }
    }
}
