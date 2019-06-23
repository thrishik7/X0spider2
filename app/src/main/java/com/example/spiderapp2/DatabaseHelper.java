package com.example.spiderapp2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Game.db";
    public static final String TABLE_NAME = "game_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "WINNER";
    public static final String COL_3 = "TIMER ";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, WINNER TEXT , TIMER TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

   public boolean insertData(String winner, int timer)
   {
       SQLiteDatabase db = this.getWritableDatabase();
       ContentValues contentValues= new ContentValues();
       contentValues.put(COL_2,winner);
       contentValues.put(COL_3,timer);
      long result= db.insert(TABLE_NAME,null, contentValues);
       if(result==-1)
           return false;
       else
           return true;
   }


   public Cursor getAllData(){

        SQLiteDatabase db= this.getWritableDatabase();
        Cursor res= db.rawQuery("select * from " + TABLE_NAME, null);
        return res;
   }

   public void deleteData (){

        SQLiteDatabase db= this. getWritableDatabase();
       db.execSQL("DELETE FROM game_table"); //delete all rows in a table
       db.close();

   }


}