package com.example.aishwarya.reminder.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.aishwarya.reminder.Alarm;

import java.util.ArrayList;
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
                COLUMN_ADDRESS+ " TEXT)";

        db.execSQL(CREATE_ALARM_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALARMS);

        onCreate(db);
    }

    public void addAlarm(Alarms alarm){

        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE,alarm.getMtitle());
        values.put(COLUMN_DATE,alarm.getMdate());
        values.put(COLUMN_TIME,alarm.getMtime());
        values.put(COLUMN_LATITUDE,alarm.getMlatitude());
        values.put(COLUMN_LONGITUDE,alarm.getMlongitude());
        values.put(COLUMN_ADDRESS,alarm.getMaddress());


        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_ALARMS, null, values);

        db.close();

    }

    public List<Alarms> findAll(){
        String sqlQuery = "SELECT * FROM " + TABLE_ALARMS ;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor myCursor = db.rawQuery(sqlQuery, null);

        Alarms alarm=null;

        ArrayList<Alarms> AllAlarms = new ArrayList<>();
        if(myCursor.moveToFirst()) {
            while (!myCursor.isAfterLast()) {
                String title = myCursor.getString(1);
                String date = myCursor.getString(2);
                String time = myCursor.getString(3);
                Double latitude = myCursor.getDouble(4);
                Double longitude = myCursor.getDouble(5);
                String address = myCursor.getString(6);

                alarm = new Alarms(title,date,time,latitude,longitude,address);
                AllAlarms.add(alarm);
                myCursor.moveToNext();
            }
        }
        db.close();
        return AllAlarms;
    }
}
