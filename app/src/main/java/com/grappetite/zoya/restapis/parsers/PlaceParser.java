package com.grappetite.zoya.restapis.parsers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.grappetite.zoya.dataclasses.PlaceData;

import java.util.ArrayList;

public class PlaceParser extends Parser{
    public PlaceParser(String json) {
        super(json);
    }


    public ArrayList<PlaceData> getDoctorTypes() {
        ArrayList<PlaceData> list = new ArrayList<>();
        JsonArray ja = this.getData().getAsJsonArray();

        for(JsonElement je : ja) {
            list.add(new Gson().fromJson(je,PlaceData.class));
        }
        return list;
    }
}
