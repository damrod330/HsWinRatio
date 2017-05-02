package com.a16mb.damrod.hearthstonewinratio;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Damrod on 03.05.2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME ="hswinration.db";
    public static final String TABLE_NAME ="deck_list";

    public static final String COL_1 ="id";
    public static final String COL_2 ="deck_name";
    public static final String COL_3 ="victory_count";
    public static final String COL_4 ="lost_count";
    public static final String COL_5 ="image_id";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
db.execSQL("create table if not exists"+ TABLE_NAME+ "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
        "DECK_NAME TEXT, VICTORY_COUNT INTEGER, LOST_COUNT INTEGER, IMAGE_ID INTEGER)");
        Log.v("DB", "Database created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public boolean insertData(String deckName, int victoryCount, int lostCount, int imageId ){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, deckName);
        contentValues.put(COL_3, victoryCount);
        contentValues.put(COL_4, lostCount);
        contentValues.put(COL_5, imageId);
    long result = db.insert(TABLE_NAME, null, contentValues);
        if(result==-1) {
            Log.v("DB", "Data not inserted");
            return false;
        }
        else {
            Log.v("DB", "Data inserted");
            return true;
        }
    }
}
