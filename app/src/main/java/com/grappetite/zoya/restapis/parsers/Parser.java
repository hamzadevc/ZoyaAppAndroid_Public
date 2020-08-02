package com.grappetite.zoya.restapis.parsers;


import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class Parser {

    private JsonElement response;
    public Parser(String json) {
        this.response = new JsonParser().parse(json);
    }

    public JsonElement getResponse() {
        return response;
    }

    public final int getResponseCode() {
        return response.getAsJsonObject().get("code").getAsInt();
    }

    public final String getResponseStatus() {
        return response.getAsJsonObject().get("status").getAsString();
    }


    public final String getResponseMessage() {
        return response.getAsJsonObject().get("msg").getAsString();
    }

    public final String getRating() {
        return response.getAsJsonObject().get("rating").getAsString();
    }

    public final JsonElement getData() {
        return response.getAsJsonObject().get("data");
    }
}
