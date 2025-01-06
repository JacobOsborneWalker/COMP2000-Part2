package com.example.comp20002;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Name and Version
    private static final String DATABASE_NAME = "user_data.db";
    private static final int DATABASE_VERSION = 2;  // Incremented version for holiday_requests table

    // User table columns
    private static final String TABLE_USER = "users";
    private static final String COLUMN_FIRST_NAME = "firstname";
    private static final String COLUMN_LAST_NAME = "lastname";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_JOIN_DATE = "joining_date";
    private static final String COLUMN_LEAVES = "leaves";
    private static final String COLUMN_SALARY = "salary";
    private static final String COLUMN_DEPARTMENT = "department";

    // Holiday request table columns
    public static final String TABLE_HOLIDAY_REQUESTS = "holiday_requests";
    public static final String COLUMN_REQUEST_ID = "request_id";
    private static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_START_DATE = "start_date";
    public static final String COLUMN_END_DATE = "end_date";
    public static final String COLUMN_STATUS = "status";  // Assuming you might need a status for requests

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create users table
        String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_USER + "("
                + COLUMN_FIRST_NAME + " TEXT,"
                + COLUMN_LAST_NAME + " TEXT,"
                + COLUMN_EMAIL + " TEXT,"
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_JOIN_DATE + " TEXT,"
                + COLUMN_LEAVES + " INTEGER,"
                + COLUMN_SALARY + " INTEGER,"
                + COLUMN_DEPARTMENT + " TEXT" + ")";
        db.execSQL(CREATE_USER_TABLE);

        // Create holiday requests table
        String CREATE_HOLIDAY_REQUESTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_HOLIDAY_REQUESTS + " ("
                + COLUMN_REQUEST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_USER_ID + " INTEGER, "
                + COLUMN_START_DATE + " TEXT, "
                + COLUMN_END_DATE + " TEXT, "
                + COLUMN_STATUS + " TEXT);";
        db.execSQL(CREATE_HOLIDAY_REQUESTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the old tables if they exist and recreate them
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOLIDAY_REQUESTS);
        onCreate(db);
    }

    // Method to clear all user data from the database
    public void clearUserData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USER, null, null);  // Deletes all rows from the user table
        db.close();
    }

    // Method to insert user data into the users table
    public void insertUserData(ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_USER, null, values);  // Insert user data into the users table
        db.close();
    }

    // Method to insert a holiday request into the holiday_requests table
    public boolean insertHolidayRequest(int userId, String startDate, String endDate, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", userId);
        values.put("start_date", startDate);
        values.put("end_date", endDate);
        values.put("status", status);

        long result = db.insert("holiday_requests", null, values);
        db.close();

        // If result == -1, the insert failed
        return result != -1;
    }

    // Method to fetch all holiday requests (without specifying user)
    public Cursor getAllHolidayRequests() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_HOLIDAY_REQUESTS;
        return db.rawQuery(query, null);
    }

    // Method to fetch holiday requests for a specific user (based on user ID)
    public Cursor getHolidayRequestsForUser(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_HOLIDAY_REQUESTS + " WHERE " + COLUMN_USER_ID + " = ?";
        return db.rawQuery(query, new String[]{String.valueOf(userId)});
    }
}
