package com.example.project_two_grosko;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class NotificationTableHelper extends SQLiteOpenHelper {
    // Database params
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "InventoryManagementDatabase.db";

    // Table params
    private static final String TABLE_NAME = "notification";
    private static final String ID_COL = "id";
    private static final String USERNAME_COL = "username";
    private static final String NOTIFICATION_COL = "notificationSetting";

    // Base table queries
    private static final String SQL_CREATE_ENTRIES = (
            "CREATE TABLE "
            + TABLE_NAME
            + " ("
            + ID_COL
            + " INTEGER PRIMARY KEY,"
            + USERNAME_COL
            + " TEXT,"
            + NOTIFICATION_COL
            + " INTEGER)"
    );

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TABLE_NAME;

    // Constructor
    public NotificationTableHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // OnUpgrade
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        Log.d(
                "NotificationTableHelper",
                "onUpgrade: " + DATABASE_NAME + ": " + TABLE_NAME
        );
        onCreate(db);
    }

    // OnCreate
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(
                "NotificationTableHelper",
                "onCreate: " + DATABASE_NAME + ": " + TABLE_NAME
        );
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    // Make sure table exists
    public void CreateTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        String sqlCreateTable = (
                "CREATE TABLE IF NOT EXISTS "
                + TABLE_NAME
                + " ("
                + ID_COL
                + " INTEGER PRIMARY KEY,"
                + USERNAME_COL
                + " TEXT,"
                + NOTIFICATION_COL
                + " INTEGER)"
        );
        db.execSQL(sqlCreateTable);
    }

    // Check if user has SMS settings
    public void SetNotificationCol(String username, int notificationSetting) {
        SQLiteDatabase db = this.getWritableDatabase();
        CreateTable();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USERNAME_COL, username);
        contentValues.put(NOTIFICATION_COL, notificationSetting);

        Log.d(
                "NotificationTableHelper",
                "SetNotificationCol: " + username + DATABASE_NAME + ": " + TABLE_NAME
        );

        String sqlUpdateNotification = "UPDATE "
                + TABLE_NAME
                + " SET "
                + NOTIFICATION_COL
                + " = "
                + notificationSetting
                + " WHERE "
                + USERNAME_COL
                + " = \""
                + username
                + "\"";

        db.execSQL(sqlUpdateNotification);
    }

    // Create a new user
    public void CreateUser(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USERNAME_COL, username);
        contentValues.put(NOTIFICATION_COL, 0);
        db.insert(TABLE_NAME, null, contentValues);
    }

    // Check if user already exists
    public int UserExists(String username) {
        Log.d(
                "NotificationTableHelper",
                "UserExists: " + username + DATABASE_NAME + ": " + TABLE_NAME
        );
        int exists = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        String sqlUserExists = "SELECT CASE WHEN EXISTS(SELECT 1 FROM "
                + TABLE_NAME
                + " WHERE "
                + USERNAME_COL
                + " = \""
                + username
                + "\") THEN 1 ELSE 0 END AS DoesUserExist";

        // Run query
        try {
            Cursor c;
            c = db.rawQuery(sqlUserExists, null);
            c.moveToFirst();
            exists = c.getInt(c.getColumnIndex("DoesUserExist"));
            c.close();
        } catch(Exception e) {
            e.printStackTrace();
        }

        return exists;
    }

    // Check if user has SMS settings
    public int GetNotificationCol(String username) {
        Log.d(
                "NotificationTableHelper",
                "GetNotificationCol: " + username + DATABASE_NAME + ": " + TABLE_NAME
        );

        if (UserExists(username) == 0) {
            CreateUser(username);
        }
        int notificationPermissions = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        String sqlUserExists = "SELECT "
                + NOTIFICATION_COL
                + " FROM "
                + TABLE_NAME
                + " WHERE "
                + USERNAME_COL
                + " = \""
                + username
                + "\"";

        // Run query
        try {
            Cursor c;
            c = db.rawQuery(sqlUserExists, null);
            c.moveToFirst();
            notificationPermissions = c.getInt(c.getColumnIndex(NOTIFICATION_COL));
            c.close();
        } catch(Exception e) {
            e.printStackTrace();
        }

        return notificationPermissions;
    }

}
