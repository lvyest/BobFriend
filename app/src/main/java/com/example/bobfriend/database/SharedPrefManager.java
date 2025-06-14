package com.example.bobfriend.database;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.bobfriend.models.User;

public class SharedPrefManager {

    private static final String SHARED_PREF_NAME = "BobFriendPrefs";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_NICKNAME = "nickname";
    private static final String KEY_PROFILE_IMAGE = "profile_image";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";

    public static void saveUser(Context context, User user) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(KEY_USER_ID, user.getId());
        editor.putString(KEY_NICKNAME, user.getNickname());
        editor.putString(KEY_PROFILE_IMAGE, user.getProfileImage());
        editor.putBoolean(KEY_IS_LOGGED_IN, true);

        editor.apply();
    }

    public static boolean isLoggedIn(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public static String getCurrentUserNickname(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_NICKNAME, "");
    }

    public static User getCurrentUser(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        if (!sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)) {
            return null;
        }

        User user = new User();
        user.setId(sharedPreferences.getInt(KEY_USER_ID, -1));
        user.setNickname(sharedPreferences.getString(KEY_NICKNAME, ""));
        user.setProfileImage(sharedPreferences.getString(KEY_PROFILE_IMAGE, ""));

        return user;
    }

    public static void clearUser(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}