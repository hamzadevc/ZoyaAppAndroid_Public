package com.grappetite.zoya.restapis.parsers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.grappetite.zoya.dataclasses.ReviewData;

import java.util.ArrayList;

public class ReviewParser extends Parser {

    public ReviewParser(String json) {
        super(json);
    }

    public ArrayList<ReviewData> getReview() {
        ArrayList<ReviewData> list = new ArrayList<>();
        JsonArray ja = this.getData().getAsJsonArray();

        for(JsonElement je : ja) {
            list.add(new Gson().fromJson(je,ReviewData.class));
        }
        return list;
    }
}
