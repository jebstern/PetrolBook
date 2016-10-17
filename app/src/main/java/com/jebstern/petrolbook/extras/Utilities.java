package com.jebstern.petrolbook.extras;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Utilities {

    public static void writeUsernameIntoPreferences(Activity activity, String value) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("username", value);
        editor.putBoolean("registered", true);
        editor.apply();
    }

    public static String readUsernameFromPreferences(Activity activity) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activity);
        return sp.getString("username", "");
    }

    public static boolean readAccountRegisteredFromPreferences(Activity activity) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activity);
        return sp.getBoolean("registered", false);
    }


    public static boolean isAcceptableUsername(String username) {
        if (!username.isEmpty()) {
            if (username.length() > 3) {
                return true;
            }
        }
        return false;
    }

    public static boolean isAcceptablePassword(String password) {
        if (!password.isEmpty()) {
            if (password.length() > 5) {
                return true;
            }
        }
        return false;
    }


    public static boolean isAcceptableAmount(String amountString) {
        if (!amountString.isEmpty()) {
            float amount = Float.parseFloat(amountString);
            return amount > 0;
        } else {
            return false;
        }

    }

    public static boolean isAcceptablePrice(String priceString) {
        if (!priceString.isEmpty()) {
            float price = Float.parseFloat(priceString);
            return price > 0;
        } else {
            return false;
        }
    }

}
