package com.grappetite.zoya.restapis.parsers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.grappetite.zoya.dataclasses.NewsFeedData;

import java.util.ArrayList;

public class NewsFeedParser extends Parser{
    public NewsFeedParser(String json) {
        super(json);
    }


    public ArrayList<NewsFeedData> getNewsFeeds() {
        ArrayList<NewsFeedData> list = new ArrayList<>();
//        JsonArray ja = this.getData().getAsJsonArray();

        JsonArray ja = this.getData().getAsJsonArray();


        for(JsonElement je : ja) {
            list.add(new Gson().fromJson(je,NewsFeedData.class));
        }
        return list;
    }

    public ArrayList<NewsFeedData> getFeaturedNewsFeeds() {
        ArrayList<NewsFeedData> list = new ArrayList<>();
        JsonArray ja = this.getResponse().getAsJsonObject().get("she_rocks").getAsJsonArray();

        for(JsonElement je : ja) {
            list.add(new Gson().fromJson(je,NewsFeedData.class));
        }
        return list;
    }

    public NewsFeedData getNewsfeed(){
        return new Gson().fromJson(this.getData().getAsJsonObject(),NewsFeedData.class);
    }
}
