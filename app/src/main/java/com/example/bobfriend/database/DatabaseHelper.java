package com.example.bobfriend.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.bobfriend.models.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "bobfriend.db";
    private static final int DB_VERSION = 1;

    // 테이블 명
    private static final String TABLE_USERS = "users";
    private static final String TABLE_RESTAURANTS = "restaurants";
    private static final String TABLE_SOLO_POSTS = "solo_posts";
    private static final String TABLE_MESSAGES = "messages";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 사용자
        db.execSQL("CREATE TABLE " + TABLE_USERS + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nickname TEXT," +
                "profileImage TEXT)");

        // 식당
        db.execSQL("CREATE TABLE " + TABLE_RESTAURANTS + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "category TEXT," +
                "latitude TEXT," +
                "longitude TEXT," +
                "rating REAL)");

        // 혼밥 모집글
        db.execSQL("CREATE TABLE " + TABLE_SOLO_POSTS + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nickname TEXT," +
                "age INTEGER," +
                "gender TEXT," +
                "dateTime TEXT," +
                "mealStyle TEXT," +
                "restaurantId INTEGER," +
                "restaurantName TEXT," +
                "createdAt TEXT)");

        // 채팅 메시지
        db.execSQL("CREATE TABLE " + TABLE_MESSAGES + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "sender TEXT," +
                "receiver TEXT," +
                "content TEXT," +
                "timestamp TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 버전 업그레이드 시 테이블 삭제 후 재생성
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESTAURANTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SOLO_POSTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGES);
        onCreate(db);
    }

    // 사용자
    public long insertUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nickname", user.getNickname());
        values.put("profileImage", user.getProfileImage());
        return db.insert(TABLE_USERS, null, values);
    }

    public User getUser(String nickname) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE nickname=?", new String[]{nickname});
        if (cursor.moveToFirst()) {
            User user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            user.setNickname(cursor.getString(cursor.getColumnIndexOrThrow("nickname")));
            user.setProfileImage(cursor.getString(cursor.getColumnIndexOrThrow("profileImage")));
            cursor.close();
            return user;
        }
        cursor.close();
        return null;
    }

    // 식당
    public long insertRestaurant(Restaurant restaurant) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", restaurant.getName());
        values.put("category", restaurant.getCategory());
        values.put("latitude", restaurant.getLatitude());
        values.put("longitude", restaurant.getLongitude());
        values.put("rating", restaurant.getRating());
        return db.insert(TABLE_RESTAURANTS, null, values);
    }

    public List<Restaurant> getAllRestaurants() {
        List<Restaurant> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_RESTAURANTS, null);
        if (cursor.moveToFirst()) {
            do {
                Restaurant r = new Restaurant();
                r.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                r.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                r.setCategory(cursor.getString(cursor.getColumnIndexOrThrow("category")));
                r.setLatitude(cursor.getString(cursor.getColumnIndexOrThrow("latitude")));
                r.setLongitude(cursor.getString(cursor.getColumnIndexOrThrow("longitude")));
                r.setRating(cursor.getFloat(cursor.getColumnIndexOrThrow("rating")));
                list.add(r);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    // 혼밥 모집글
    public long insertSoloPost(SoloPost post) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nickname", post.getNickname());
        values.put("age", post.getAge());
        values.put("gender", post.getGender());
        values.put("dateTime", post.getDateTime());
        values.put("mealStyle", post.getMealStyle());
        values.put("restaurantId", post.getRestaurantId());
        values.put("restaurantName", post.getRestaurantName());
        values.put("createdAt", post.getCreatedAt());
        return db.insert(TABLE_SOLO_POSTS, null, values);
    }

    public List<SoloPost> getSoloPostsByRestaurant(int restaurantId) {
        List<SoloPost> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SOLO_POSTS + " WHERE restaurantId=?", new String[]{String.valueOf(restaurantId)});
        if (cursor.moveToFirst()) {
            do {
                SoloPost p = new SoloPost();
                p.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                p.setNickname(cursor.getString(cursor.getColumnIndexOrThrow("nickname")));
                p.setAge(cursor.getInt(cursor.getColumnIndexOrThrow("age")));
                p.setGender(cursor.getString(cursor.getColumnIndexOrThrow("gender")));
                p.setDateTime(cursor.getString(cursor.getColumnIndexOrThrow("dateTime")));
                p.setMealStyle(cursor.getString(cursor.getColumnIndexOrThrow("mealStyle")));
                p.setRestaurantId(cursor.getInt(cursor.getColumnIndexOrThrow("restaurantId")));
                p.setRestaurantName(cursor.getString(cursor.getColumnIndexOrThrow("restaurantName")));
                p.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow("createdAt")));
                list.add(p);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    // 채팅 메시지
    public long insertMessage(Message message) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("sender", message.getSender());
        values.put("receiver", message.getReceiver());
        values.put("content", message.getContent());
        values.put("timestamp", message.getTimestamp());
        return db.insert(TABLE_MESSAGES, null, values);
    }

    public Restaurant getRestaurant(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_RESTAURANTS + " WHERE id=?", new String[]{String.valueOf(id)});
        if (cursor.moveToFirst()) {
            Restaurant r = new Restaurant();
            r.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            r.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
            r.setCategory(cursor.getString(cursor.getColumnIndexOrThrow("category")));
            r.setLatitude(cursor.getString(cursor.getColumnIndexOrThrow("latitude")));
            r.setLongitude(cursor.getString(cursor.getColumnIndexOrThrow("longitude")));
            r.setRating(cursor.getFloat(cursor.getColumnIndexOrThrow("rating")));
            cursor.close();
            return r;
        }
        cursor.close();
        return null;
    }


    public List<Message> getMessagesBetweenUsers(String user1, String user2) {
        List<Message> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_MESSAGES + " WHERE " +
                        "(sender=? AND receiver=?) OR (sender=? AND receiver=?) ORDER BY id ASC",
                new String[]{user1, user2, user2, user1});
        if (cursor.moveToFirst()) {
            do {
                Message m = new Message();
                m.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                m.setSender(cursor.getString(cursor.getColumnIndexOrThrow("sender")));
                m.setReceiver(cursor.getString(cursor.getColumnIndexOrThrow("receiver")));
                m.setContent(cursor.getString(cursor.getColumnIndexOrThrow("content")));
                m.setTimestamp(cursor.getString(cursor.getColumnIndexOrThrow("timestamp")));
                list.add(m);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
}
