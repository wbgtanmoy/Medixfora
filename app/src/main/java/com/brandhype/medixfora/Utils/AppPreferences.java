package com.brandhype.medixfora.Utils;

/**
 * Created by Tanmoy Banerjee on 11-07-2017.
 */

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public final class AppPreferences
{
    private  final String appPreferences = Storage.codopoliz_main.name();
    private SharedPreferences appShared;
    private Editor appEditor;

    public AppPreferences(Context context)
    {
        this.appShared=context.getSharedPreferences(appPreferences,Activity.MODE_PRIVATE);
        this.appEditor=appShared.edit();
    }

    public void clearAllPreferences(){
        appEditor.clear();
        appEditor.commit();
    }

    public void removePreferences(String key) {
        appEditor.remove(key);
        appEditor.commit();
    }

    public void SavePreferences(String key, String value) {

        appEditor.putString(key, value);
        appEditor.commit();
    }

    public String LoadPreferences(String Key) {

        return appShared.getString(Key, "");
    }

    public enum Storage {
        codopoliz_main,
        DISPLAY_WIDTH,
        DISPLAY_HEIGHT,
        USER_ID,
        USER_NAME,
        HAS_LOGGEDIN,
        DOCTOR_ID,
        DOCTOR_NAME,
        BOOKING_DATE,
        SLOT_ID,
        SLOT_DATETIME,
        APPOINTMENTDATA,
        PATIENTDATA,
        DOCTORDATA,
        DEVICE_FCMID,
    }

}

//###########################---Usage Example---#################################
/*
 //-----------------------declare-----------------------
 AppPreferences pref;

 //---------------Retrieve user id----------------------
 AppPreferences pref = new AppPreferences(this);
 String userId = pref.LoadPreferences(AppPreferences.Storage.USER_ID.name());

 //---------------Store user id-------------------------
 AppPreferences pref = new AppPreferences(this);
 pref.SavePreferences(AppPreferences.Storage.USER_ID.name(),"");

 */
//----------------------------------------------------------------
