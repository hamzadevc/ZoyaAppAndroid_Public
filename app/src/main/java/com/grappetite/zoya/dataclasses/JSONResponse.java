package com.grappetite.zoya.dataclasses;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class JSONResponse {

    @SerializedName("data")
    private ArrayList<NewsFeedData> data = new ArrayList<>();

    @SerializedName("code")
    private int code;

    @SerializedName("status")
    private String status;

    @SerializedName("total_records")
    private String total_records;

    public ArrayList<NewsFeedData> getData() {
        return data;
    }

    public int getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }

    public String getTotal_records() {
        return total_records;
    }
}


