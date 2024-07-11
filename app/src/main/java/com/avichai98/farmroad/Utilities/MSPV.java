package com.avichai98.farmroad.Utilities;

import static android.content.Context.MODE_PRIVATE;
import android.content.Context;
import android.content.SharedPreferences;

public class MSPV {
    private static MSPV mspv;
    private SharedPreferences prefs;
    public static final String RECORDS_FILE = "RECORDS_FILE";

    private MSPV(Context context) {
        prefs = context.getSharedPreferences(RECORDS_FILE, MODE_PRIVATE);
    }

    public static void init(Context context) {
        if (mspv == null) {
            mspv = new MSPV(context);
        }
    }

    public static MSPV getInstance() {
        return mspv;
    }

    public void saveString(String key, String value) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String readString(String key, String def) {
        return prefs.getString(key, def);
    }
}