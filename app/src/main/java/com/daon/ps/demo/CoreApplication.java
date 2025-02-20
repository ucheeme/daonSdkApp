package com.daon.ps.demo;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.daon.fido.client.sdk.IXUAF;


public class CoreApplication {
    public static final String TAG = "IXUAF";
    private static IXUAF fido;
    private static boolean uafInitialised;
    public static final String PREF_USER_HREF = "pref_user_href";
    public static final String PREF_REG_HREF = "pref_reg_href";
    public static final String PREF_USERNAME = "pref_username";

    public static void setFido(IXUAF service) {
        CoreApplication.fido = service;
    }

    public static IXUAF getFido() {
        return fido;
    }

    public static boolean isUafInitialised() {
        return uafInitialised;
    }

    public static void setUafInitialised(boolean uafInitialised) {
        CoreApplication.uafInitialised = uafInitialised;
    }

    public static void setUserHref(Context context,  String value){
        storeString(context, PREF_USER_HREF, value);
    }

    public static String getUserHref(Context context, String defaultValue){
        return getStoredString(context, PREF_USER_HREF, "");
    }

    public static void setRegHref(Context context,  String value){
        storeString(context, PREF_REG_HREF, value);
    }

    public static String getRegHref(Context context, String defaultValue){
        return getStoredString(context, PREF_REG_HREF, "");
    }

    public static void setUsername(Context context,  String value){
        storeString(context, PREF_USERNAME, value);
    }

    public static String getUsername(Context context, String defaultValue){
        return getStoredString(context, PREF_USERNAME, null);
    }



    public static void storeString(Context context, String key, String value){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putString(key, value).apply();
    }

    public static String getStoredString(Context context, String key, String defaultValue){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(key,defaultValue);
    }

    public static void storeBoolean(Context context, String key, boolean value){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putBoolean(key, value).apply();
    }

    public static boolean getStoredBoolean(Context context, String key, boolean defaultValue){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(key,defaultValue);
    }
}
