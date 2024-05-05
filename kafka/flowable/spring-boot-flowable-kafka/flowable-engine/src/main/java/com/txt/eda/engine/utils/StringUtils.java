package com.txt.eda.engine.utils;

public class StringUtils {

    public static String getOrDefault(String s1, String defaultValue) {
        if(s1 != null && !s1.trim().isEmpty()) {
            return s1;
        }
        return defaultValue;
    }
}
