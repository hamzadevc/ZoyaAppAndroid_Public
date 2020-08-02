package com.grappetite.zoya.utils;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YoutubeUtils {
    public static String getVideoId(String url) {
        String pattern = "(?<=watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";

        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(url);

        if(matcher.find()){
            return matcher.group();
        }
        else return null;
    }
}
