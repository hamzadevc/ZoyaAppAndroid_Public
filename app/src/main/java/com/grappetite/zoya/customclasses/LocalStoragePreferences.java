package com.grappetite.zoya.customclasses;


import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.grappetite.zoya.dataclasses.ProfileData;
import com.grappetite.zoya.dataclasses.SettingData;

public class LocalStoragePreferences
{
    private static final String TAG = "LocalStoragePreferences";


    private static final String PROFILE_DATA = "profileData";
    private static final String SETTINGS_DATA = "settingsData";
    private static final String REGISTRATION_ID = "registrationId";
    private static final String SELECTED_CITY = "selectedCity";
    private static final String AUTH_TOKEN = "authToken";
    private static final String SHOW_SYMPTOMS_DISCLAIMER = "symptomsDisclaimer";
    private static final String SHOW_CHAT_HISTORY_DISCLAIMER = "showChatHistoryDisclaimer";
    private static final String SHOW_NEWS_FEED_HELP= "showNewsFeedHelp";
    private static final String USER_EMAIL = "userEmail";
    private static final String USER_AGE = "userAge";
    private static final String USER_PASS = "userPass";
    private static final String IS_ANONYMOUS = "isAnonymous";
    private static final String CHECK_PERIOD_SET = "checkPeriod";
    private static final String CHECK_PROMO = "checkPromo";
    private static final String CHECK_UPDATED = "checkUpdated";

    private static SharedPreferences sp;

    public static void initialize(Context context) {
        if (sp!=null)
            throw new IllegalStateException("LocalStoragePreferences is already initialized");
        else
            sp = context.getSharedPreferences("profile_info", Context.MODE_PRIVATE);
    }

    private static void save(String key, String value)
    {
        sp.edit().putString(key,value).apply();
    }
    private static void save(String key, int value)
    {
        sp.edit().putInt(key, value).apply();
    }
    private static void save(String key, boolean value)
    {
        sp.edit().putBoolean(key, value).apply();
    }
    private static void save(String key, float value)
    {
        sp.edit().putFloat(key, value).apply();
    }
    private static void save(String key, long value)
    {
        sp.edit().putLong(key, value).apply();
    }

    private static String get(String key,String def)
    {
        return sp.getString(key,def);
    }
    private static int get(String key, int def)
    {
        return sp.getInt(key, def);
    }
    private static boolean get(String key,boolean def)
    {
        return sp.getBoolean(key, def);
    }
    private static float get(String key, float def)
    {
        return sp.getFloat(key, def);
    }
    private static long get(String key, long def)
    {
        return sp.getLong(key, def);
    }


    public static String getUserEmail() {
        return get(USER_EMAIL, null);
    }
    public static void setUserEmail(String email)
    {
        save(USER_EMAIL,email);
    }

    public static String getUserAge() {
        return get(USER_AGE, null);
    }
    public static void setUserAge(String userAge)
    {
        save(USER_AGE,userAge);
    }


    public static String getUserPass() {
        return get(USER_PASS, null);
    }
    public static void setUserPass(String pass)
    {
        save(USER_PASS,pass);
    }

    public static String getAuthToken() {
        return get(AUTH_TOKEN, null);
    }
    public static void setAuthToken(String token)
    {
        save(AUTH_TOKEN,token);
    }

    public static String getSelectedCity() {
        return get(SELECTED_CITY, null);
    }
    public static void setSelectedCity(String city) {
        save(SELECTED_CITY,city);
    }


    public static String getRegistrationId() {
        return get(REGISTRATION_ID,null);
    }
    public static void setRegistrationId(String registrationId) {
        save(REGISTRATION_ID,registrationId);
    }

    public static ProfileData getProfileData()
    {
        String spd = get(PROFILE_DATA,null);
        if (spd ==null)
            return null;
        else
            return new Gson().fromJson(spd,ProfileData.class);
    }

    public static void setProfileData(ProfileData profileData) {
        save(PROFILE_DATA,new Gson().toJson(profileData));
    }

    public static SettingData getSettingsData()
    {
        String sd = get(SETTINGS_DATA,null);
        if (sd ==null)
            return new SettingData();
        else
            return new Gson().fromJson(sd,SettingData.class);
    }

    public static void setSettingsData(SettingData settingData) {
        save(SETTINGS_DATA,new Gson().toJson(settingData));
    }

    public static boolean showChatHistoryDisclaimer()
    {
        return get(SHOW_CHAT_HISTORY_DISCLAIMER,true);
    }

    public static void setShowChatHistoryDisclaimer(boolean show) {
        save(SHOW_CHAT_HISTORY_DISCLAIMER,show);
    }

    public static boolean showSymptomsDisclaimer()
    {
        return get(SHOW_SYMPTOMS_DISCLAIMER,true);
    }

    public static void setShowSymptomsDisclaimer(boolean show) {
        save(SHOW_SYMPTOMS_DISCLAIMER,show);
    }

    public static boolean showNewsFeedHelp()
    {
        return get(SHOW_NEWS_FEED_HELP,true);
    }

    public static void setShowNewsFeedHelp(boolean show) {
        save(SHOW_NEWS_FEED_HELP,show);
    }

    public static void clear() {
        String reg = sp.getString(REGISTRATION_ID, null);
        sp.edit().clear().apply();
        if (reg!=null)
            save(REGISTRATION_ID,reg);
    }

    public static boolean getIsAnonymous() {
       return get(IS_ANONYMOUS,false);
    }

    public static void setIsAnonymous(boolean check) {
        save(IS_ANONYMOUS,check);
    }

    public static boolean getCheckPeriodSet() {
        return get(CHECK_PERIOD_SET,false);
    }

    public static void setCheckPeriodSet(boolean check) {
        save(CHECK_PERIOD_SET,check);
    }

    public static boolean getCheckPromo() {
        return get(CHECK_PROMO,false);
    }

    public static void setCheckPromo(boolean check) {
        save(CHECK_PROMO,check);
    }

    public static boolean getCheckUpdated() {
        return get(CHECK_UPDATED,false);
    }

    public static void setCheckUpdated(boolean check) {
        save(CHECK_UPDATED,check);
    }

}
