package com.example.younes.authentificationproject.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import static com.example.younes.authentificationproject.utils.PreferencesUtility.ACCESS_TOKEN_IN_PREF;
import static com.example.younes.authentificationproject.utils.PreferencesUtility.LOGGED_IN_PREF;

/**
 * Created by younes on 8/20/2018.
 */

public class SaveSharedPreference {
    static SharedPreferences getPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * Set the Login Status
     * @param context
     * @param loggedIn
     */
    public static void setLoggedIn(Context context, boolean loggedIn) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putBoolean(LOGGED_IN_PREF, loggedIn);
        editor.apply();
    }
    public static void putAccessToken(Context context, String accessToken) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putString(ACCESS_TOKEN_IN_PREF, accessToken);
        editor.apply();
    }


    /**
     * Get the Login Status
     * @param context
     * @return boolean: login status
     */
    public static boolean getLoggedStatus(Context context) {
        return getPreferences(context).getBoolean(LOGGED_IN_PREF, false);
    }
    public static String getAccessToken(Context context) {
        return getPreferences(context).getString(ACCESS_TOKEN_IN_PREF, null);
    }
}
