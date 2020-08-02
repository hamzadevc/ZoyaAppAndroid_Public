package com.grappetite.zoya.restapis.parsers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.grappetite.zoya.dataclasses.CommentData;

import java.util.ArrayList;

public class NewsFeedCommentParser extends Parser{
    public NewsFeedCommentParser(String json) {
        super(json);
    }

    public ArrayList<CommentData> getComments() {
        ArrayList<CommentData> list = new ArrayList<>();
        JsonArray ja = this.getData().getAsJsonArray();

        for(JsonElement je : ja) {
            list.add(new Gson().fromJson(je,CommentData.class));
        }
        return list;
    }

    public CommentData getCommentData() {
        return  new Gson().fromJson(this.getData(),CommentData.class);
    }
}
