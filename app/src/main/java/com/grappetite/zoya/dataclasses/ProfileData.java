package com.grappetite.zoya.dataclasses;


import android.text.TextUtils;
import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.grappetite.zoya.enums.Gender;
import com.grappetite.zoya.enums.MaritalStatus;
import com.grappetite.zoya.utils.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ProfileData {
    private static final String TAG = "ProfileData";

    @SerializedName("user_id")
    private long id;

    @SerializedName("fullname")
    private String fullName;

    @SerializedName("email")
    private String email;

    @SerializedName("age")
    private String age;

    @SerializedName("dob")
    private String dateOfBirth;

    @SerializedName("marital_status")
    private String maritalStatus;

    @SerializedName("gender")
    private String gender;

    @SerializedName("image_url")
    private String profilePicUrl;

    @SerializedName("last_period_date")
    private String lastPeriodStartDate;

    @SerializedName("no_of_period_days")
    private int periodLast;

    @SerializedName("menstrual_cycle_days")
    private int periodMenstrualCycle;

    @SerializedName("facebook_id")
    private String facebookId;

    @SerializedName("is_activated")
    private int isActive;

    @SerializedName("is_updated")
    private int isUpdated;

    @SerializedName("is_new")
    private int isNew;

    public boolean isActive() {
        return isActive == 1;
    }

    public long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public Date getDateOfBirth() {
        if (!TextUtils.isEmpty(dateOfBirth)) {
            try {
                return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dateOfBirth);
            } catch (ParseException e) {
                Log.e(TAG, "getDateOfBirth: ", e);
            }
        }
        return null;
    }

    public MaritalStatus getMaritalStatus() {
        return MaritalStatus.fromString(maritalStatus);
    }

    public Gender getGender() {
        return Gender.fromString(gender);
    }

    public String getProfilePicUrl() {
        return TextUtils.isEmpty(profilePicUrl) ? null : profilePicUrl;
    }

    public Date getLastPeriodStartDate() {
        if (TextUtils.isEmpty(lastPeriodStartDate))
            return null;
        else
            return DateUtils.toServerDate("yyyy-MM-dd", lastPeriodStartDate);
    }

    public int getPeriodLast() {
        return periodLast;
    }

    public int getPeriodMenstrualCycle() {
        return periodMenstrualCycle;
    }

    public boolean isSocialLogin() {
        return !facebookId.equals("0");
    }

    public boolean getIsNew() {
        return isNew == 1;
    }


    public void setId(long id) {
        this.id = id;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public void setLastPeriodStartDate(String lastPeriodStartDate) {
        this.lastPeriodStartDate = lastPeriodStartDate;
    }

    public void setPeriodLast(int periodLast) {
        this.periodLast = periodLast;
    }

    public void setPeriodMenstrualCycle(int periodMenstrualCycle) {
        this.periodMenstrualCycle = periodMenstrualCycle;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public void setIsNew(int isNew) {
        this.isNew = isNew;
    }

    public int getIsUpdated() {
        return isUpdated;
    }

    public void setIsUpdated(int isUpdated) {
        this.isUpdated = isUpdated;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
