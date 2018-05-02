package com.example.aishwarya.reminder.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by aishwarya on 24/04/18.
 */

public class AlarmsDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "alarmsDB.db";

    private  static final String TABLE_ALARMS = "Alarms";
    private static final String COLUMN_ID = "alarm_id";
    private static final String COLUMN_TITLE = "alarm_title";
    private static final String COLUMN_TIME = "alarm_time";
    private static final String COLUMN_DATE = "alarm_date";
    private static final String COLUMN_LATITUDE = "alarm_latitude";
    private static final String COLUMN_LONGITUDE = "alarm_longitude";
    private static final String COLUMN_ADDRESS = "alarm_address";
    private static final String COLUMN_TIMESTAMP = "alarm_timestamp";
    private static final String COLUMN_ETIMESTAMP = "end_timestamp";

    public AlarmsDBHandler(Context context) {
        super(context, DATABASE_NAME, null , DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_ALARM_TABLE = "CREATE TABLE " +
                TABLE_ALARMS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, "+
                COLUMN_DATE + " TEXT, "+
                COLUMN_TIME + " TEXT, "+
                COLUMN_LATITUDE+ " TEXT, "+
                COLUMN_LONGITUDE+ " TEXT, "+
                COLUMN_ADDRESS+ " TEXT, " +
                COLUMN_TIMESTAMP+ " LONG)";

        db.execSQL(CREATE_ALARM_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALARMS);

        onCreate(db);
    }

    private boolean isRecordExistInDatabase(String title, String date, String time, String address) {
        String query = "SELECT * FROM " + TABLE_ALARMS + " WHERE " + COLUMN_TITLE + " = '" + title + "'" + " AND "
                + COLUMN_DATE + " = '" + date + "'" + " AND "
                + COLUMN_TIME + " = '" + time + "'" + " AND "
                + COLUMN_ADDRESS + " =  '" + address + "'";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            //Record exist
            c.close();
            return true;
        }
        //Record available
        c.close();
        return false;
    }

    public boolean isRecordClashing(String date, String time) {
        String query = "SELECT * FROM " + TABLE_ALARMS + " WHERE "
                + COLUMN_DATE + " = '" + date + "'" + " AND "
                + COLUMN_TIME + " = '" + time + "'";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            //Record exist
            c.close();
            return true;
        }
        //Record available
        c.close();
        return false;
    }

    public void addAlarm(Alarms alarm){

        if(!isRecordExistInDatabase(alarm.getMtitle(), alarm.getMdate(), alarm.getMtime(), alarm.getMaddress())) {

            ContentValues values = new ContentValues();
            values.put(COLUMN_TITLE, alarm.getMtitle());
            values.put(COLUMN_DATE, alarm.getMdate());
            values.put(COLUMN_TIME, alarm.getMtime());
            values.put(COLUMN_LATITUDE, alarm.getMlatitude());
            values.put(COLUMN_LONGITUDE, alarm.getMlongitude());
            values.put(COLUMN_ADDRESS, alarm.getMaddress());
            values.put(COLUMN_TIMESTAMP, (alarm.getMtimestamp()));

            SQLiteDatabase db = this.getWritableDatabase();

            db.insert(TABLE_ALARMS, null, values);

            db.close();
        }
    }

    public void updateAlarm(Alarms alarm,int i){

        if(!isRecordExistInDatabase(alarm.getMtitle(), alarm.getMdate(), alarm.getMtime(), alarm.getMaddress())) {

            ContentValues values = new ContentValues();
            values.put(COLUMN_TITLE, alarm.getMtitle());
            values.put(COLUMN_DATE, alarm.getMdate());
            values.put(COLUMN_TIME, alarm.getMtime());
            values.put(COLUMN_LATITUDE, alarm.getMlatitude());
            values.put(COLUMN_LONGITUDE, alarm.getMlongitude());
            values.put(COLUMN_ADDRESS, alarm.getMaddress());
            values.put(COLUMN_TIMESTAMP, (alarm.getMtimestamp()));

            SQLiteDatabase db = this.getWritableDatabase();

            db.update(TABLE_ALARMS,values,COLUMN_ID +"="+ i,null);

            db.close();
        }
    }

    public void deleteAlarm(Alarms alarms){

        String query = "DELETE FROM " + TABLE_ALARMS + " WHERE " + COLUMN_TITLE + " = '" + alarms.getMtitle() + "'" + " AND "
                + COLUMN_DATE + " = '" + alarms.getMdate() + "'" + " AND "
                + COLUMN_TIME + " = '" + alarms.getMtime() + "'" + " AND "
                + COLUMN_ADDRESS + " =  '" + alarms.getMaddress() + "'";

        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL(query);

        db.close();


    }

    public Alarms findPreviousAlarm(Alarms alarms){
        String countQuery = "SELECT  * FROM " + TABLE_ALARMS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();

        if(count >= 1) {
            String sqlQuery = "SELECT * FROM " + " ( SELECT * FROM " + TABLE_ALARMS + " ORDER BY " + COLUMN_TIMESTAMP + " DESC ) " + " WHERE (" + COLUMN_TIMESTAMP + " < " + alarms.getMtimestamp() + ") LIMIT 1";


            Cursor myCursor = db.rawQuery(sqlQuery, null);
            Alarms alarm = null;
            if (myCursor.moveToFirst()) {
                String title = myCursor.getString(1);
                String date = myCursor.getString(2);
                String time = myCursor.getString(3);
                Double latitude = myCursor.getDouble(4);
                Double longitude = myCursor.getDouble(5);
                String address = myCursor.getString(6);
                long timestmp = (myCursor.getLong(7));

                alarm = new Alarms(title, date, time, latitude, longitude, address, timestmp);
            }
            myCursor.close();
            db.close();
            return alarm;
        }
        return null;
    }


    public Alarms findNextLocAlarm(long current_time){
        String countQuery = "SELECT  * FROM " + TABLE_ALARMS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();

        if(count >= 1) {
            String sqlQuery = "SELECT * FROM " + " ( SELECT * FROM " + TABLE_ALARMS + " ORDER BY " + COLUMN_TIMESTAMP + " ) " + " WHERE (" + COLUMN_TIMESTAMP + " > " + current_time + ") LIMIT 1";


            Cursor myCursor = db.rawQuery(sqlQuery, null);
            Alarms alarm = null;
            if (myCursor.moveToFirst()) {
                String title = myCursor.getString(1);
                String date = myCursor.getString(2);
                String time = myCursor.getString(3);
                Double latitude = myCursor.getDouble(4);
                Double longitude = myCursor.getDouble(5);
                String address = myCursor.getString(6);
                long timestmp = (myCursor.getLong(7));

                alarm = new Alarms(title, date, time, latitude, longitude, address, timestmp);
            }
            myCursor.close();
            db.close();
            return alarm;
        }
        return null;
    }

    public Alarms findNextAlarm(Alarms alarms){
        String countQuery = "SELECT  * FROM " + TABLE_ALARMS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();

        if(count >= 1) {
            String sqlQuery = "SELECT * FROM " + " ( SELECT * FROM " + TABLE_ALARMS + " ORDER BY " + COLUMN_TIMESTAMP + " ) " + " WHERE (" + COLUMN_TIMESTAMP + " > " + alarms.getMtimestamp() + ") LIMIT 1";


            Cursor myCursor = db.rawQuery(sqlQuery, null);
            Alarms alarm = null;
            if (myCursor.moveToFirst()) {
                String title = myCursor.getString(1);
                String date = myCursor.getString(2);
                String time = myCursor.getString(3);
                Double latitude = myCursor.getDouble(4);
                Double longitude = myCursor.getDouble(5);
                String address = myCursor.getString(6);
                long timestmp = (myCursor.getLong(7));

                alarm = new Alarms(title, date, time, latitude, longitude, address, timestmp);
            }
            myCursor.close();
            db.close();
            return alarm;
        }
        return null;
    }

    public List<Alarms> findAll(){
        String sqlQuery = "SELECT * FROM " + TABLE_ALARMS + " ORDER BY "+ COLUMN_TIMESTAMP;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor myCursor = db.rawQuery(sqlQuery, null);

        Alarms alarm=null;

        ArrayList<Alarms> AllAlarms = new ArrayList<>();
        if(myCursor.moveToFirst()) {
            while (!myCursor.isAfterLast()) {
                int id = myCursor.getInt(0);
                String title = myCursor.getString(1);
                String date = myCursor.getString(2);
                String time = myCursor.getString(3);
                Double latitude = myCursor.getDouble(4);
                Double longitude = myCursor.getDouble(5);
                String address = myCursor.getString(6);
                long timestmp = (myCursor.getLong(7));

                alarm = new Alarms(id,title,date,time,latitude,longitude,address,timestmp);
                AllAlarms.add(alarm);
                myCursor.moveToNext();
            }
        }
        myCursor.close();
        db.close();
        return AllAlarms;
    }
}
