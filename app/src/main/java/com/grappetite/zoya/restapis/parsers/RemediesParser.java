package com.grappetite.zoya.restapis.parsers;


import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.grappetite.zoya.dataclasses.RemediesData;

import java.util.ArrayList;

public class RemediesParser extends Parser{
    public RemediesParser(String json) {
        super(json);
    }

    public ArrayList<RemediesData>  getRemedies() {
        ArrayList<RemediesData> list = new ArrayList<>();
        for(JsonElement je : this.getData().getAsJsonArray()) {
            list.add(new Gson().fromJson(je,RemediesData.class));
        }
        return list;
    }
}
