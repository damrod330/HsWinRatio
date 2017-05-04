package com.a16mb.damrod.hearthstonewinratio;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

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
        onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    db.execSQL("CREATE TABLE IF NOT EXISTS "+ TABLE_NAME+ "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
        "DECK_NAME TEXT, VICTORY_COUNT INTEGER, LOST_COUNT INTEGER, IMAGE_ID INTEGER)");
        Log.v("DB", " CREATE TABLE IF NOT EXISTS (sql executed)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }


    public void dropTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_NAME + "'");
        Log.v("DB", " DROP TABLE IF EXISTS (sql executed)");
    }

    public boolean createDeck(String deckName, int victoryCount, int lostCount, int imageId ){
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
            Log.v("DB", "Data inserted for "+ deckName);
            return true;
        }
    }
    public boolean removeDeck(String deckName){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, COL_2 + "=? ", new String[]{deckName});
        if(result==-1) {
            Log.v("DB", "COULDN'T DELETE DECK");
            return false;
        }
        else {
            Log.v("DB", "DECK: "+ deckName+" DELETED");
            return true;
        }
    }

    public boolean updateDeck(String deckName, int victoryCount, int lostCount){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_3, victoryCount);
        contentValues.put(COL_4, lostCount);
        long result = db.update(TABLE_NAME, contentValues,COL_2 + "=? ", new String[]{deckName} );
        if(result==-1) {
            Log.v("DB", "Data not updated");
            return false;
        }
        else {
            Log.v("DB", "Data updated for "+ deckName);
            return true;
        }
    }

    public List<deck> getDeckList() {
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db  = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        List<deck> data = new ArrayList<deck>();

        if (cursor.moveToFirst()) {
           do{

                deck d = new deck(cursor.getString(1), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4));
                data.add(0, d);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return data;
    }
}
