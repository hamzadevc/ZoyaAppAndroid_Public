package com.grappetite.zoya.dataclasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Newsfeed {

    @SerializedName("code")
    private int code;

    @SerializedName("status")
    private String status;

    @SerializedName("total_records")
    private String total_records;

    @SerializedName("data")
    private List<NewsFeedData> data = new ArrayList<>();


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotal_records() {
        return total_records;
    }

    public void setTotal_records(String total_records) {
        this.total_records = total_records;
    }

    public List<NewsFeedData> getData() {
        return data;
    }

    public void setData(List<NewsFeedData> data) {
        this.data = data;
    }
}
