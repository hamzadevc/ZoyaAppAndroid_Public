package com.grappetite.zoya.restapis.parsers;

import com.google.gson.Gson;
import com.grappetite.zoya.dataclasses.ProfileData;
import com.grappetite.zoya.dataclasses.SettingData;

public class SignupParser extends Parser {
    public SignupParser(String json) {
        super(json);
    }


    public ProfileData getProfileData() {
        return new Gson().fromJson(this.getData(),ProfileData.class);
    }

    public SettingData getSettingData() {
        return new Gson().fromJson(this.getData(),SettingData.class);
    }

    public String getAuthToken() {
        return this.getData().getAsJsonObject().get("authtoken").getAsString();
    }
}
