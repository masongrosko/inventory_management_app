package com.example.project_two_grosko;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class InventoryTableHelper extends SQLiteOpenHelper {
    // Database params
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "InventoryManagementDatabase.db";

    // Table params
    private static final String TABLE_NAME = "inventory";
    private static final String ID_COL = "id";
    private static final String USERNAME_COL = "username";
    private static final String ITEM_ID_COL = "item_id";
    private static final String ITEM_NAME_COL = "item_name";
    private static final String ITEM_STOCK_COL = "item_stock";
    private static final String ITEM_PRICE_COL = "item_price";


    // Base table queries
    private static final String SQL_CREATE_ENTRIES = (
            "CREATE TABLE "
                    + TABLE_NAME
                    + " ("
                    + ID_COL
                    + " INTEGER PRIMARY KEY,"
                    + USERNAME_COL
                    + " TEXT, "
                    + ITEM_ID_COL
                    + " TEXT, "
                    + ITEM_NAME_COL
                    + " TEXT, "
                    + ITEM_STOCK_COL
                    + " INTEGER, "
                    + ITEM_PRICE_COL
                    + " INTEGER)"
    );

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TABLE_NAME;

    // Constructor
    public InventoryTableHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // OnUpgrade
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        Log.d(
                "InventoryTableHelper",
                "onUpgrade: " + DATABASE_NAME + ": " + TABLE_NAME
        );
        onCreate(db);
    }

    // OnCreate
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(
                "InventoryTableHelper",
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
                        + " TEXT, "
                        + ITEM_ID_COL
                        + " TEXT, "
                        + ITEM_NAME_COL
                        + " TEXT, "
                        + ITEM_STOCK_COL
                        + " INTEGER, "
                        + ITEM_PRICE_COL
                        + " INTEGER)"
        );
        db.execSQL(sqlCreateTable);
        Log.d(
                "InventoryTableHelper",
                "CreateTable: " + DATABASE_NAME + ": " + TABLE_NAME
        );
    }

    // SetRow
    public void SetRow(
            String username,
            String item_id,
            String item_name,
            String item_stock,
            String item_price
    ) {
        Log.d(
                "InventoryTableHelper",
                "SetRow: " + username + DATABASE_NAME + ": " + TABLE_NAME
        );
        CreateTable();
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USERNAME_COL, username);
        contentValues.put(ITEM_ID_COL, item_id);
        contentValues.put(ITEM_NAME_COL, item_name);
        contentValues.put(ITEM_STOCK_COL, item_stock);
        contentValues.put(ITEM_PRICE_COL, item_price);

        Log.d(
                "InventoryTableHelper",
                "SetRow: " + username + DATABASE_NAME + ": " + TABLE_NAME
        );

        db.insert(TABLE_NAME, null, contentValues);
    }

    // Check if item already exists
    public int ItemIdExists(String itemId) {
        Log.d(
                "InventoryTableHelper",
                "ItemIdExists: " + itemId + DATABASE_NAME + ": " + TABLE_NAME
        );
        int exists = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        String sqlUserExists = "SELECT CASE WHEN EXISTS(SELECT 1 FROM "
                + TABLE_NAME
                + " WHERE "
                + ITEM_ID_COL
                + " = \""
                + itemId
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

    // Change stock by n
    public void updateStock(String itemId, String amountChanged) {
        SQLiteDatabase db = this.getWritableDatabase();

        Log.d(
                "InventoryTableHelper",
                "updateStock: " + itemId + DATABASE_NAME + ": " + TABLE_NAME
        );

        String sqlUpdateNotification = "UPDATE "
                + TABLE_NAME
                + " SET "
                + ITEM_STOCK_COL
                + " = "
                + ITEM_STOCK_COL
                + amountChanged
                + " WHERE "
                + ITEM_ID_COL
                + " = \""
                + itemId
                + "\"";

        db.execSQL(sqlUpdateNotification);
    }

    // Increment stock
    public void incrementStock(String itemId) {
        Log.d(
                "InventoryTableHelper",
                "incrementStock: " + itemId
        );

        updateStock(itemId, "+ 1");
    }

    // Decrement stock
    public void decrementStock(String itemId) {
        Log.d(
                "InventoryTableHelper",
                "decrementStock: " + itemId
        );
        updateStock(itemId, "- 1");
    }

    // Remove row
    public Integer removeRow(String itemId) {
        Log.d(
                "InventoryTableHelper",
                "removeRow: " + itemId
        );
        SQLiteDatabase db=this.getWritableDatabase();
        return db.delete(TABLE_NAME,"item_id = ?",new String[]{itemId});
    }

    // Return all data from database
    public Cursor getData() {
        CreateTable();
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM  " + TABLE_NAME;
        return db.rawQuery(query, null);
    }
}
