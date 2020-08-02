package com.grappetite.zoya.dataclasses;


import com.google.gson.annotations.SerializedName;

public class SettingData {
    @SerializedName("flag_push_notification")
    private int isNotificationEnabled = 1;

    public boolean isNotificationEnabled() {
        return isNotificationEnabled==1;
    }

    public void setNotificationEnabled(boolean notificationEnabled) {
        isNotificationEnabled = notificationEnabled?1:0;
    }
}
