package com.mentobile.grabbit.Utility;

import android.content.Context;
import android.content.SharedPreferences;

import com.mentobile.grabbit.GrabbitApplication;


/**
 * Created by Gokul on 9/14/2016.
 */
public class AppPref {
    private static AppPref instance;

    private SharedPreferences sPreferences;
    private SharedPreferences.Editor sEditor;
    private String SG_SHARED_PREFERENCES = "shared_preferences";


    // variables
    private final String PREF_USER_ID = "user_id";
    private final String PREF_USER_NAME = "user_name";
    private final String PREF_USER_EMAIL = "user_email";
    private final String PREF_USER_MOBILE = "user_mobile";
    private final String PREF_USER_TAKEATOUR = "take_a_tour";


    private AppPref(Context context) {

        sPreferences = context.getSharedPreferences(SG_SHARED_PREFERENCES,
                Context.MODE_PRIVATE);
        sEditor = sPreferences.edit();
    }


    public static AppPref getInstance() {
        if (instance == null) {
            synchronized (AppPref.class) {
                if (instance == null) {
                    instance = new AppPref(GrabbitApplication.getInstance().getApplicationContext());
                }
            }
        }
        return instance;
    }

    public void registerPre(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        sPreferences.registerOnSharedPreferenceChangeListener(listener);
    }


    public void unRegister(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        sPreferences.unregisterOnSharedPreferenceChangeListener(listener);
    }

    public String getUserID() {
        return sPreferences.getString(PREF_USER_ID, "");
    }

    public void setUserID(String userid) {
        sEditor.putString(PREF_USER_ID, userid);
        sEditor.commit();
    }

    public String getUserName() {
        return sPreferences.getString(PREF_USER_NAME, "");
    }

    public void setUserName(String userName) {
        sEditor.putString(PREF_USER_NAME, userName);
        sEditor.commit();
    }

    public String getUserEmail() {
        return sPreferences.getString(PREF_USER_EMAIL, "");
    }

    public void setUserEmail(String userEmail) {
        sEditor.putString(PREF_USER_EMAIL, userEmail);
        sEditor.commit();
    }


    public String getUserMobile() {
        return sPreferences.getString(PREF_USER_MOBILE, "");
    }

    public void setUserMobile(String userMobile) {
        sEditor.putString(PREF_USER_MOBILE, userMobile);
        sEditor.commit();
    }

    public String getTakeATour() {
        return sPreferences.getString(PREF_USER_TAKEATOUR, "");
    }

    public void setTakeATour(String takeATour) {
        sEditor.putString(PREF_USER_TAKEATOUR, takeATour);
        sEditor.commit();
    }


}
