package com.grappetite.zoya.restapis.parsers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.grappetite.zoya.dataclasses.SymptomData;

import java.util.ArrayList;

public class SymptomsParser extends Parser {
    public SymptomsParser(String json) {
        super(json);
    }

    public ArrayList<SymptomData> getSymptoms() {
        ArrayList<SymptomData> list = new ArrayList<>();
        JsonArray ja = this.getData().getAsJsonArray();
        for(JsonElement je : ja) {
            list.add(new Gson().fromJson(je,SymptomData.class));
        }
        return list;
    }


    public SymptomData.SymptomCoverData getSymptomCoverData() {
        JsonArray ja = this.getResponse().getAsJsonObject().get("bodyparts").getAsJsonArray();
        JsonElement je = ja.get(0);
        return new Gson().fromJson(je, SymptomData.SymptomCoverData.class);
    }
}
