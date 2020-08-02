package com.grappetite.zoya.restapis.webmaps;


import androidx.annotation.Nullable;

import com.google.firebase.iid.FirebaseInstanceId;
import com.grappetite.zoya.utils.DateUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PostMaps {
    public static final String KEY_IMAGE = "image";
    public static Map<String,String> createUser(
            String name,
            String email,
            String password,
            boolean isMale,
            @Nullable String dob,
            boolean isSingle)
    {
        HashMap<String,String> map  = new HashMap<>();
        map.put("fullname",name);
        map.put("email",email);
        map.put("password",password);
        map.put("gender",isMale?"male":"female");
        if (dob!=null)
            map.put("dob",dob);
        map.put("marital_status",isSingle?"single":"married");
        map.put("device","ANDROID");
        map.put("device_id", FirebaseInstanceId.getInstance().getToken()!=null?FirebaseInstanceId.getInstance().getToken():"");
        return map;
    }
    public static Map<String,String> updateUser(
            String name,
            String email,
            boolean isMale,
            @Nullable String dob,
            boolean isSingle,
            boolean removeProfilePic)
    {
        HashMap<String,String> map  = new HashMap<>();
        map.put("fullname",name);
        map.put("email",email);
        map.put("gender",isMale?"male":"female");
        if (dob!=null)
            map.put("dob",dob);
        map.put("marital_status",isSingle?"single":"married");
        map.put("device","ANDROID");
        map.put("device_id", FirebaseInstanceId.getInstance().getToken()!=null?FirebaseInstanceId.getInstance().getToken():"");
        if (removeProfilePic)
            map.put("delete_old_file","1");
        return map;
    }
    public static Map<String,String> updateUser(String deviceId)
    {
        HashMap<String,String> map  = new HashMap<>();
        map.put("device","ANDROID");
        map.put("device_id",deviceId);
        return map;
    }
    public static Map<String,String> loginUser(String email, String password) {
        HashMap<String,String> map  = new HashMap<>();
        map.put("email",email);
        map.put("password",password);
        map.put("device","ANDROID");
        map.put("device_id", FirebaseInstanceId.getInstance().getToken()!=null?FirebaseInstanceId.getInstance().getToken():"");
        return map;
    }

    public static Map<String,String> ratePlace(String placeID, String rating, String review) {
        HashMap<String,String> map  = new HashMap<>();
        map.put("place_id",placeID);
        map.put("rating",rating);
        map.put("review",review);
        return map;
    }
    public static Map<String,String> checkRting(String placeID, String userID) {
        HashMap<String,String> map  = new HashMap<>();
        map.put("place_id",placeID);
        map.put("user_id",userID);
        return map;
    }

    public static Map<String,String> postComment(long newsFeedId, String message) {
        HashMap<String,String> map  = new HashMap<>();
        map.put("newsfeed_id",String.valueOf(newsFeedId));
        map.put("message",message);
        return map;
    }

    public static Map<String,String> flagComment(long commentId, boolean flag) {
        HashMap<String,String> map  = new HashMap<>();
        map.put("comment_id",String.valueOf(commentId));
        map.put("flag",String.valueOf(flag?1:0));
        return map;
    }

    public static Map<String,String> likeNewsFeed(long newsFeedId, boolean like) {
        HashMap<String,String> map  = new HashMap<>();
        map.put("newsfeed_id",String.valueOf(newsFeedId));
        map.put("like",String.valueOf(like?1:0));
        return map;
    }

    public static Map<String,String> contactUs(String subject, String email) {
        HashMap<String,String> map  = new HashMap<>();
        map.put("subject",subject);
        map.put("message",email);
        return map;
    }
    public static Map<String,String> changePassword(String newPassword, String oldPassword) {
        HashMap<String,String> map  = new HashMap<>();
        map.put("password",newPassword);
        map.put("old_password",oldPassword);
        return map;
    }

    public static Map<String,String> updatePeriodTracker(Date lastPeriodDate, int periodLast, int periodMenstrualCycle) {
        HashMap<String,String> map  = new HashMap<>();
        map.put("last_period_date", DateUtils.toServerDate(lastPeriodDate));
        map.put("no_of_period_days",String.valueOf(periodLast));
        map.put("menstrual_cycle_days",String.valueOf(periodMenstrualCycle));
        return map;
    }

    public static Map<String,String> googleLogin(String socialToken) {
        HashMap<String,String> map  = new HashMap<>();
        map.put("google_token",socialToken);
        map.put("device","ANDROID");
        map.put("device_id", FirebaseInstanceId.getInstance().getToken()!=null?FirebaseInstanceId.getInstance().getToken():"");
        return map;
    }

    public static Map<String,String> facebookLogin(String socialToken) {
        HashMap<String,String> map  = new HashMap<>();
        map.put("facebook_token",socialToken);
        map.put("device","ANDROID");
        map.put("device_id", FirebaseInstanceId.getInstance().getToken()!=null?FirebaseInstanceId.getInstance().getToken():"");
        return map;
    }

    public static Map<String,String> updatePeriodData(Date lastPeriodDate, int periodLast, int periodMenstrualCycle,String data) {
        HashMap<String,String> map  = new HashMap<>();
        map.put("data",data);
        map.put("last_period_date", DateUtils.toServerDate(lastPeriodDate));
        map.put("no_of_period_days",String.valueOf(periodLast));
        map.put("menstrual_cycle_days",String.valueOf(periodMenstrualCycle));
        return map;
    }
}
