package com.grappetite.zoya.restapis.parsers;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.grappetite.zoya.dataclasses.MessageData;

import java.util.ArrayList;

public class IncomingMessageParser {
    private static final String TAG = "IncomingMessageParser";
    private String json;

    public IncomingMessageParser(String json) {
        this.json = json;
    }

    public Purpose getPurpose() {
        String purpose;
        try {
            JsonObject jo = new JsonParser().parse(json).getAsJsonObject();
            purpose = jo.get("purpose").getAsString();
        }catch (Exception e){
            Log.e(TAG, "getPurpose: " + e.toString());
            purpose="";
        }
        switch (purpose) {
            case "attached":
                return Purpose.ATTACHED;
            case "chat":
                return Purpose.CHAT;
            default:
                return null;
        }
    }

    public boolean hasMessagesKey() {
        JsonObject jo = new JsonParser().parse(json).getAsJsonObject();
        return jo.has("messages");
    }

    public ArrayList<MessageData> getMessageHistory() {
        ArrayList<MessageData> list = new ArrayList<>();
        JsonObject jo = new JsonParser().parse(json).getAsJsonObject();
        JsonArray ja = jo.get("messages").getAsJsonArray();
        for (JsonElement je : ja) {
            list.add(new Gson().fromJson(je,MessageData.class));
        }
        return list;
    }

    public MessageData getMessage() {
        return new Gson().fromJson(json,MessageData.class);
    }
    public enum Purpose {
        CHAT, ATTACHED
    }
}
