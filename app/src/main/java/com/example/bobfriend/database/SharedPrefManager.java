package com.example.bobfriend.database;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.bobfriend.models.User;

public class SharedPrefManager {

    private static final String SHARED_PREF_NAME = "bobfriend_pref";
    private static final String KEY_ID = "key_id";
    private static final String KEY_USERNAME = "key_username";
    private static final String KEY_NAME = "key_name";

    private static SharedPrefManager mInstance;
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    private SharedPrefManager(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    public void saveUser(User user) {
        editor.putInt(KEY_ID, user.getId());
        editor.putString(KEY_USERNAME, user.getUsername());
        editor.putString(KEY_NAME, user.getName());
        editor.apply();
    }

    public User getUser() {
        int id = sharedPreferences.getInt(KEY_ID, -1);
        String username = sharedPreferences.getString(KEY_USERNAME, null);
        String name = sharedPreferences.getString(KEY_NAME, null);
        return new User(id, username, name);
    }

    public void clear() {
        editor.clear();
        editor.apply();
    }

    // ✅ static wrapper 메서드들
    public static void saveUser(Context context, User user) {
        getInstance(context).saveUser(user);
    }

    public static int getCurrentUserId(Context context) {
        return getInstance(context).sharedPreferences.getInt(KEY_ID, -1);
    }

    public static String getCurrentUsername(Context context) {
        return getInstance(context).sharedPreferences.getString(KEY_USERNAME, null);
    }

    public static boolean isLoggedIn(Context context) {
        return getInstance(context).sharedPreferences.getInt(KEY_ID, -1) != -1;
    }

    public static void clearUser(Context context) {
        getInstance(context).editor.clear().apply();
    }
}
