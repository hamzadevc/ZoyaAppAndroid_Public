package com.grappetite.zoya.dataclasses;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class PeriodData {


    @SerializedName("period_data_id")
    private String period_data_id;

    @SerializedName("id")
    private String id;

    @SerializedName("startDate")
    private String startDate;

    @SerializedName("periodSize")
    private String periodSize;

    @SerializedName("lateByDays")
    private String lateByDays;

    @SerializedName("uniqueId")
    private String uniqueId;


    public PeriodData(String id, String uniqueId, String periodSize, String startDate) {
        this.id = id;
        this.startDate = startDate;
        this.periodSize = periodSize;
        this.uniqueId = uniqueId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getPeriodSize() {
        return periodSize;
    }

    public void setPeriodSize(String periodSize) {
        this.periodSize = periodSize;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getLateByDays() {
        return lateByDays;
    }

    public void setLateByDays(String lateByDays) {
        this.lateByDays = lateByDays;
    }

    public String getPeriod_data_id() {
        return period_data_id;
    }

    public void setPeriod_data_id(String period_data_id) {
        this.period_data_id = period_data_id;
    }
}
