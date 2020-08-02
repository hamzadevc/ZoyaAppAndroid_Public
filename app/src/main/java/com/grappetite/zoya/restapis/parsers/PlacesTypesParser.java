package com.grappetite.zoya.restapis.parsers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.grappetite.zoya.dataclasses.PlacesTypeData;

import java.util.ArrayList;

public class PlacesTypesParser extends Parser{
    public PlacesTypesParser(String json) {
        super(json);
    }


    public ArrayList<PlacesTypeData> getPlacesTypes() {
        ArrayList<PlacesTypeData> list = new ArrayList<>();
        JsonArray ja = this.getData().getAsJsonArray();

        for(JsonElement je : ja) {
            list.add(new Gson().fromJson(je,PlacesTypeData.class));
        }
        return list;
    }
}
