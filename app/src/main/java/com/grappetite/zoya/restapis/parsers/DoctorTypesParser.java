package com.grappetite.zoya.restapis.parsers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.grappetite.zoya.dataclasses.DoctorData;

import java.util.ArrayList;

public class DoctorTypesParser extends Parser{
    public DoctorTypesParser(String json) {
        super(json);
    }


    public ArrayList<DoctorData> getDoctorTypes() {
        ArrayList<DoctorData> list = new ArrayList<>();
        JsonArray ja = this.getData().getAsJsonArray();

        for(JsonElement je : ja) {
            list.add(new Gson().fromJson(je,DoctorData.class));
        }
        return list;
    }
}
