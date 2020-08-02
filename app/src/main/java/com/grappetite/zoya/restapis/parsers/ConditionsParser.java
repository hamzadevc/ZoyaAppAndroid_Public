package com.grappetite.zoya.restapis.parsers;


import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.grappetite.zoya.dataclasses.ConditionData;

import java.util.ArrayList;

public class ConditionsParser extends Parser {

    public ConditionsParser(String json) {
        super(json);
    }

    public ArrayList<ConditionData> getConditions() {
        ArrayList<ConditionData> list = new ArrayList<>();
        for (JsonElement je : this.getData().getAsJsonArray())
            list.add(new Gson().fromJson(je,ConditionData.class));
        return list;
    }
    public ConditionData getCondition() {
        ArrayList<ConditionData> list = new ArrayList<>();
        for (JsonElement je : this.getData().getAsJsonArray())
            list.add(new Gson().fromJson(je,ConditionData.class));
        if (list.size()>0)
            return list.get(0);
        else
            return null;
    }
}
