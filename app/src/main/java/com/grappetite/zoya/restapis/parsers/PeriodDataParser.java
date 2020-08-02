package com.grappetite.zoya.restapis.parsers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.grappetite.zoya.dataclasses.PeriodData;

import java.util.ArrayList;

public class PeriodDataParser extends Parser {
    public PeriodDataParser(String json) {
        super(json);
    }

    public ArrayList<PeriodData> getPeriodData() {
        ArrayList<PeriodData> list = new ArrayList<>();
        JsonArray ja = this.getData().getAsJsonArray();

        for(JsonElement je : ja) {
            list.add(new Gson().fromJson(je,PeriodData.class));
        }
        return list;
    }
}
