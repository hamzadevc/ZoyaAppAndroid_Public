package com.grappetite.zoya.restapis.parsers;

import com.google.gson.Gson;
import com.grappetite.zoya.dataclasses.RatingData;

public class RatingParser extends Parser {

    public RatingParser(String json) {
        super(json);
    }

    public RatingParser getRatingData() {
        return null;
//        return new Gson().fromJson(this.getData(),RatingData.class);
    }

}
