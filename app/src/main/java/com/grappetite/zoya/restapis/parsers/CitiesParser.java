package com.grappetite.zoya.restapis.parsers;


import com.google.gson.JsonElement;

import java.util.ArrayList;

public class CitiesParser extends Parser {
    public CitiesParser(String json) {
        super(json);
    }

    public ArrayList<String> getCities() {
        ArrayList<String> list = new ArrayList<>();
        for(JsonElement je : this.getData().getAsJsonArray()) {
            list.add(je.getAsJsonObject().get("city").getAsString());
        }

        return list;
    }
}
