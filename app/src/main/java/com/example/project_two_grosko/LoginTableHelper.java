package com.example.project_two_grosko;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class LoginTableHelper extends SQLiteOpenHelper {
    // Database params
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "InventoryManagementDatabase.db";

    // Table params
    private static final String TABLE_NAME = "login";
    private static final String ID_COL = "id";
    private static final String USERNAME_COL = "username";
    private static final String PASSWORD_COL = "password";

    // Base table queries
    private static final String SQL_CREATE_ENTRIES = (
            "CREATE TABLE "
            + TABLE_NAME
            + " ("
            + ID_COL
            + " INTEGER PRIMARY KEY,"
            + USERNAME_COL
            + " TEXT,"
            + PASSWORD_COL
            + " TEXT)"
    );

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TABLE_NAME;

    // Constructor
    public LoginTableHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // OnUpgrade
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    // OnCreate
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    // Attempt login
    public int UserLogin(String username, String password) {
        int loginSuccess = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        String sqlUserPassExists = "SELECT CASE WHEN EXISTS(SELECT 1 FROM "
                + TABLE_NAME
                + " WHERE "
                + USERNAME_COL
                + " = \""
                + username
                + "\" AND "
                + PASSWORD_COL
                + " = \""
                + password
                + "\") THEN 1 ELSE 0 END AS LoginSuccess";

        // Run query
        try {
            Cursor c;
            c = db.rawQuery(sqlUserPassExists, null);
            c.moveToFirst();
            loginSuccess = c.getInt(c.getColumnIndex("LoginSuccess"));
            c.close();
        } catch(Exception e) {
            e.printStackTrace();
        }

        return loginSuccess;
    }

    // Check if user already exists
    public int UserExists(String username) {
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

    // Create a new user
    public long CreateUser(String username, String password) {
        long userCreated = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USERNAME_COL, username);
        contentValues.put(PASSWORD_COL, password);

        Log.d(
                "LoginTableHelper",
                "createUser: " + username + DATABASE_NAME + ": " + TABLE_NAME
        );

        // If user does not exists then add the new user/pass combo
        if (UserExists(username) != 1) {
            userCreated = db.insert(TABLE_NAME, null, contentValues);
        }
        return userCreated;
    }
}
