package com.grappetite.zoya.dataclasses;

public class Expert {

    String expName;
    String expEmail;
    String expPassword;
    String expSpecial;
    String expImage;
    String deviceToken;
    String isOnline;

    public Expert(String expName, String expEmail, String expPassword, String expSpecial, String expImage, String deviceToken, String isOnline) {
        this.expName = expName;
        this.expEmail = expEmail;
        this.expPassword = expPassword;
        this.expSpecial = expSpecial;
        this.expImage = expImage;
        this.deviceToken = deviceToken;
        this.isOnline = isOnline;
    }

    public Expert() {
    }

    public String getExpName() {
        return expName;
    }

    public void setExpName(String expName) {
        this.expName = expName;
    }

    public String getExpEmail() {
        return expEmail;
    }

    public void setExpEmail(String expEmail) {
        this.expEmail = expEmail;
    }

    public String getExpPassword() {
        return expPassword;
    }

    public void setExpPassword(String expPassword) {
        this.expPassword = expPassword;
    }

    public String getExpSpecial() {
        return expSpecial;
    }

    public void setExpSpecial(String expSpecial) {
        this.expSpecial = expSpecial;
    }

    public String getExpImage() {
        return expImage;
    }

    public void setExpImage(String expImage) {
        this.expImage = expImage;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(String isOnline) {
        this.isOnline = isOnline;
    }
}
