package com.mentobile.grabbit.Utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

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
    private final String PREF_UESR_IMAGE_URL = "user_image";
    private final String PREF_NOTIFICATION = "notification";


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

    public String getImageUrl() {
        return sPreferences.getString(PREF_UESR_IMAGE_URL, "");
    }

    public void setImageUrl(String imageUrl) {
        sEditor.putString(PREF_UESR_IMAGE_URL, imageUrl);
        sEditor.commit();
    }

    public String getTakeATour() {
        return sPreferences.getString(PREF_USER_TAKEATOUR, "");
    }

    public void setTakeATour(String takeATour) {
        sEditor.putString(PREF_USER_TAKEATOUR, takeATour);
        sEditor.commit();
    }

    public String getNotification() {
        return sPreferences.getString(PREF_NOTIFICATION, "");
    }

    public void setNotification(String notification) {
        sEditor.putString(PREF_NOTIFICATION, notification);
        sEditor.commit();
    }

    public boolean isLogin() {
        Log.d("App Pref","::::User Id"+getUserID());
        if (getUserID() != null && getUserID().length() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public void clearSharedPreferenceFile() {
        SharedPreferences sharedPreferences = GrabbitApplication.getInstance().getApplicationContext().
                getSharedPreferences(SG_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().commit();
    }
}
