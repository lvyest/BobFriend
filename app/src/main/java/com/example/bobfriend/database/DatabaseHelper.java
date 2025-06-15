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
    private static final int DB_VERSION = 3; // 버전 업그레이드

    // 테이블 명
    private static final String TABLE_USERS = "users";
    private static final String TABLE_RESTAURANTS = "restaurants";
    private static final String TABLE_SOLO_POSTS = "solo_posts";
    private static final String TABLE_MESSAGES = "messages";
    private static final String TABLE_REVIEWS = "reviews"; // 리뷰 테이블 추가

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
                "address TEXT," +
                "phone TEXT," +
                "rate REAL)");

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

        // 리뷰 테이블
        db.execSQL("CREATE TABLE " + TABLE_REVIEWS + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT," +
                "restaurantId TEXT," +
                "restaurantName TEXT," +
                "rating REAL," +
                "comment TEXT," +
                "createdAt TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 3) {
            // ✅ 기존 restaurants 테이블 삭제 후 재생성
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESTAURANTS);
            db.execSQL("CREATE TABLE " + TABLE_RESTAURANTS + "(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT," +
                    "category TEXT," +
                    "address TEXT," +
                    "phone TEXT," +
                    "rate REAL)");

            // ✅ 리뷰 테이블도 마찬가지로 생성
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_REVIEWS + "(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "username TEXT," +
                    "restaurantId TEXT," +
                    "restaurantName TEXT," +
                    "rating REAL," +
                    "comment TEXT," +
                    "createdAt TEXT)");
        }
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
        values.put("address", restaurant.getAddress());
        values.put("phone", restaurant.getPhone());
        values.put("rate", restaurant.getRate());
        return db.insert(TABLE_RESTAURANTS, null, values);
    }

    public List<Restaurant> getAllRestaurants() {
        List<Restaurant> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_RESTAURANTS, null);
        if (cursor.moveToFirst()) {
            do {
                Restaurant r = new Restaurant(
                        cursor.getString(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("category")),
                        cursor.getString(cursor.getColumnIndexOrThrow("address")),
                        cursor.getString(cursor.getColumnIndexOrThrow("phone")),
                        cursor.getFloat(cursor.getColumnIndexOrThrow("rate"))
                );
                list.add(r);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public Restaurant getRestaurant(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_RESTAURANTS + " WHERE id=?", new String[]{String.valueOf(id)});
        if (cursor.moveToFirst()) {
            Restaurant r = new Restaurant(
                    cursor.getString(cursor.getColumnIndexOrThrow("id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("name")),
                    cursor.getString(cursor.getColumnIndexOrThrow("category")),
                    cursor.getString(cursor.getColumnIndexOrThrow("address")),
                    cursor.getString(cursor.getColumnIndexOrThrow("phone")),
                    cursor.getFloat(cursor.getColumnIndexOrThrow("rate"))
            );
            cursor.close();
            return r;
        }
        cursor.close();
        return null;
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

    public List<SoloPost> getSoloPostsByRestaurant(String restaurantId) {
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

    // 리뷰 관련 메서드
    public long insertReview(Review review) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", review.getUsername());
        values.put("restaurantId", review.getRestaurantId());
        values.put("restaurantName", review.getRestaurantName());
        values.put("rating", review.getRating());
        values.put("comment", review.getComment());
        values.put("createdAt", review.getCreatedAt());
        return db.insert(TABLE_REVIEWS, null, values);
    }

    public List<Review> getReviewsByRestaurant(String restaurantId) {
        List<Review> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_REVIEWS + " WHERE restaurantId=? ORDER BY id DESC",
                new String[]{restaurantId});
        if (cursor.moveToFirst()) {
            do {
                Review review = new Review();
                review.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                review.setUsername(cursor.getString(cursor.getColumnIndexOrThrow("username")));
                review.setRestaurantId(cursor.getString(cursor.getColumnIndexOrThrow("restaurantId")));
                review.setRestaurantName(cursor.getString(cursor.getColumnIndexOrThrow("restaurantName")));
                review.setRating(cursor.getFloat(cursor.getColumnIndexOrThrow("rating")));
                review.setComment(cursor.getString(cursor.getColumnIndexOrThrow("comment")));
                review.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow("createdAt")));
                list.add(review);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public float getAverageRating(String restaurantId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT AVG(rating) as avgRating FROM " + TABLE_REVIEWS + " WHERE restaurantId=?",
                new String[]{restaurantId});
        float avgRating = 0f;
        if (cursor.moveToFirst()) {
            avgRating = cursor.getFloat(cursor.getColumnIndexOrThrow("avgRating"));
        }
        cursor.close();
        return avgRating;
    }

    public boolean hasUserReviewed(String username, String restaurantId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_REVIEWS + " WHERE username=? AND restaurantId=?",
                new String[]{username, restaurantId});
        boolean hasReviewed = false;
        if (cursor.moveToFirst()) {
            hasReviewed = cursor.getInt(0) > 0;
        }
        cursor.close();
        return hasReviewed;
    }
}