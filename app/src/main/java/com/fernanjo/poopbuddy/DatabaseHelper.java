package com.fernanjo.poopbuddy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Anthony on 03/06/2017.
 * Edited 7/6/17 to add date column to table
 */

class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "PoopEvents.db";
    private static final String TABLE_NAME = "PoopTable";
    private static final String COL_ID = "ID";
    private static final String COL_DAY = "Day";
    private static final String COL_MONTH = "Month";
    private static final String COL_YEAR = "Year";
    private static final String COL_HOUR = "Hour";
    private static final String COL_MINUTE = "Minute";
    private static final String COL_LOCATION = "Location";
    private static final String COL_QUALITY = "Quality";
    private static final String COL_PAIN = "Pain";
    private static final String COL_BLOOD = "Blood";
    private static final String COL_COMMENT = "Comment";

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create table query
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_DAY + " INTEGER, " +
                COL_MONTH + " INTEGER, " +
                COL_YEAR + " INTEGER, " +
                COL_HOUR + " INTEGER, " +
                COL_MINUTE + " INTEGER, " +
                COL_LOCATION + " TEXT, " +
                COL_QUALITY + " TEXT, " +
                COL_PAIN + " TEXT, " +
                COL_BLOOD + " TEXT, " +
                COL_COMMENT + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //Methods to return Column names for use outside of class
    String getColId() {
        return COL_ID;
    }

    String getColDay() {
        return COL_DAY;
    }

    String getColMonth() {
        return COL_MONTH;
    }

    String getColYear() {
        return COL_YEAR;
    }

    String getColHour() {
        return COL_HOUR;
    }

    String getColMinute() {
        return COL_MINUTE;
    }

    String getColLocation() {
        return COL_LOCATION;
    }

    String getColQuality() {
        return COL_QUALITY;
    }

    String getColPain() {
        return COL_PAIN;
    }

    String getColBlood() {
        return COL_BLOOD;
    }

    String getColComment() {
        return COL_COMMENT;
    }

    boolean insertData(String day, String month, String year, String hour, String minute, String location, String quality, String pain, String blood, String comment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_DAY, day);
        contentValues.put(COL_MONTH, month);
        contentValues.put(COL_YEAR, year);
        contentValues.put(COL_HOUR, hour);
        contentValues.put(COL_MINUTE, minute);
        contentValues.put(COL_LOCATION, location);
        contentValues.put(COL_QUALITY, quality);
        contentValues.put(COL_PAIN, pain);
        contentValues.put(COL_BLOOD, blood);
        contentValues.put(COL_COMMENT, comment);
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        /* TODO Check order format
        Check that order method correctly displays records in date order (may need to also add time order?)
        */
        return db.rawQuery("SELECT * FROM '" + TABLE_NAME + "' ORDER BY " + COL_YEAR + " ASC, " + COL_MONTH + " ASC, " + COL_DAY + " ASC", null);
    }

    Cursor getAllLocations() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT DISTINCT " + COL_LOCATION + " FROM " + TABLE_NAME + " ORDER BY " + COL_LOCATION, null);
    }

    boolean clearAllRecords() {
        SQLiteDatabase db = this.getWritableDatabase();
        //First part to delete records -- Second part to delete sequence so count restarts at 1
        return (db.delete(TABLE_NAME, null, null) > 0) && (db.delete("sqlite_sequence", "name = '" + TABLE_NAME + "'", null) > 0);
    }

    boolean clearRecord(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return (db.delete(TABLE_NAME, "ID = " + id, null) > 0);
    }

    int tableCount() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM '" + TABLE_NAME + "'", null);
        int result = res.getCount();
        res.close();
        return result;
    }

    int monthTotal(int month, int year) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM '" + TABLE_NAME + "' " +
                "where MONTH = '" + month + "' " +
                "and YEAR = '" + year + '"', null);
        int result = res.getCount();
        res.close();
        return result;
    }

    int monthTotal(int month) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM '" + TABLE_NAME + "' " +
                "where MONTH = '" + month + "' " +
                "and YEAR = '" + Calendar.getInstance().get(Calendar.YEAR) + "'", null);
        int result = res.getCount();
        res.close();
        return result;
    }

    String mostPopLoc() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT " + COL_LOCATION + ", count(*) FROM '" + TABLE_NAME + "' group by " + COL_LOCATION + " order by count(*) desc", null);
        if (res.getCount() > 0) {
            res.moveToFirst();
            String result = res.getString(res.getColumnIndex(COL_LOCATION)) + " (" + res.getString(res.getColumnIndex("count(*)")) + ")";
            res.close();
            return result;
        }
        return "";
    }

    String poopPerDay(int month, int year) {
        SQLiteDatabase db = this.getWritableDatabase();
        /*TODO Check digit format for month
        Confirm no errors when not formating month as two digit - e.g. 01 for JAN
         */
        Cursor res = db.rawQuery("SELECT COUNT(*) FROM '" + TABLE_NAME + "' WHERE " + COL_MONTH + " = '" + month + "'", null);
        res.moveToFirst();
        int count = res.getInt(res.getColumnIndex("COUNT(*)"));
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, 1);
        int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        float result = count / daysInMonth;
        res.close();
        return String.format(Locale.ENGLISH, "%.02f", result);
    }

    int minID() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT MIN(" + COL_ID + ") from " + TABLE_NAME, null);
        res.moveToFirst();
        int result = res.getInt(res.getColumnIndex("MIN(" + COL_ID + ")"));
        res.close();
        return result;
    }

    int maxID() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT MAX(" + COL_ID + ") from " + TABLE_NAME, null);
        res.moveToFirst();
        int result = res.getInt(res.getColumnIndex("MAX(" + COL_ID + ")"));
        res.close();
        return result;
    }
}
