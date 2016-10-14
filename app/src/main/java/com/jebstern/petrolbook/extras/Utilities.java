package com.jebstern.petrolbook.extras;


import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Utilities {

    public static void writeUsernameIntoPreferences(Activity activity, String value){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("username", value);
        editor.putBoolean("registered", true);
        editor.apply();
    }

    public static String readUsernameFromPreferences(Activity activity, String key){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
        return sp.getString(key, "");
    }

    public static boolean readAccountRegisteredFromPreferences(Activity activity){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
        return sp.getBoolean("registered", false);
    }

}
