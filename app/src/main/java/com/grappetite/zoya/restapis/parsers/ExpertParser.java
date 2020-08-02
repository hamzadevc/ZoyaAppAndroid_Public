package com.grappetite.zoya.restapis.parsers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.grappetite.zoya.dataclasses.ExpertData;

import java.util.ArrayList;

public class ExpertParser extends Parser{
    public ExpertParser(String json) {
        super(json);
    }


    public ArrayList<ExpertData> getExperts() {
        ArrayList<ExpertData> list = new ArrayList<>();
        JsonArray ja = this.getData().getAsJsonArray();

        for(JsonElement je : ja) {
            list.add(new Gson().fromJson(je,ExpertData.class));
        }
        return list;
    }
}
