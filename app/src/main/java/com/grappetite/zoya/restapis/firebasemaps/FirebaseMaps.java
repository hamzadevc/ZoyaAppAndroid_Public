package com.grappetite.zoya.restapis.firebasemaps;

import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

public class FirebaseMaps {


    public static Map<String,Object> LoginUser(String deviceId)
    {
        Map<String,Object> map  = new HashMap<>();
        map.put("deviceToken",deviceId);
        map.put("isOnline","true");
        return map;
    }

    public static Map<String,Object> SendImageMessage(String message,String expID,String senderRef,String recieverRef)
    {
        Map<String,Object> body  = new HashMap<>();
        body.put("message", message);
        body.put("seen", false);
        body.put("type", "image");
        body.put("time", ServerValue.TIMESTAMP);
        body.put("from", expID);

        Map<String,Object> details  = new HashMap<>();
        details.put(senderRef, body);
        details.put(recieverRef, body);

        return details;
    }

    public static Map<String,Object> SendTextMessage(String message,String expID,String senderRef,String recieverRef)
    {
        Map<String,Object> map  = new HashMap<>();
        map.put("message", message);
        map.put("seen", false);
        map.put("type", "text");
        map.put("time", ServerValue.TIMESTAMP);
        map.put("from", expID);

        Map<String,Object> details  = new HashMap<>();
        details.put(senderRef, map);
        details.put(recieverRef, map);

        return details;
    }


    public static Map<String,Object> Notification(String uID,String type,String sender)
    {
        Map<String,Object> map  = new HashMap<>();
        map.put("from", uID);
        map.put("type", type);
        map.put("sender", sender);
        return map;
    }


}
