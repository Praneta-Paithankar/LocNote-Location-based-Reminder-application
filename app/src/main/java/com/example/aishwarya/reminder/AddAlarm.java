package com.example.aishwarya.reminder;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;

import com.example.aishwarya.reminder.Database.Alarms;
import com.example.aishwarya.reminder.Database.AlarmsDBHandler;
import com.example.aishwarya.reminder.Maps.MapActivity;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class AddAlarm extends AppCompatActivity  {
    TextView btnDate, datetext, btnTime, timetext, destination_text;
    private Calendar calendar;
    EditText reminder_title;
    Bundle bundle;
    FloatingActionButton addalarm;
    String mtime;
    StringBuilder mdate;
    Calendar cal;
    final static int req1 = 1;
    double latitude;
    double longitude;
    String Address;
    public String a = "0";
    int day, year, month, mHour, mMinute;
    private static final String TAG = "MainActivity";

    private static final int ERROR_DIALOG_REQUEST = 9001;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_add_alarm);
        btnDate = (TextView) findViewById(R.id.set_date);
        datetext = (TextView) findViewById(R.id.date_text);
        btnTime = (TextView) findViewById(R.id.set_time);
        timetext = (TextView) findViewById(R.id.time_text);
        reminder_title = (EditText) findViewById(R.id.reminder_title);
        addalarm = (FloatingActionButton) findViewById(R.id.starred2);
        destination_text = (TextView) findViewById(R.id.repeat_no_text);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        cal = Calendar.getInstance();
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month + 1, day);
    }

    //date picker dialog
    @SuppressWarnings("deprecation")
    public void onClickDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "date set",
                Toast.LENGTH_SHORT)
                .show();
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }
    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    showDate(arg1, arg2 + 1, arg3);
                }
            };

    private void showDate(int year, int month, int day) {
        mdate = new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year);
        cal.set(Calendar.DATE, day);  //1-31
        cal.set(Calendar.MONTH, month);  //first month is 0!!! January is zero!!!
        cal.set(Calendar.YEAR, year);
        datetext.setText(mdate);
    }

    //time picker dialog
    public void onClickTime(View view) {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        cal.set(Calendar.HOUR_OF_DAY, mHour);  //HOUR
        cal.set(Calendar.MINUTE, mMinute);
        cal.set(Calendar.SECOND, 0);
        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        String am_pm = "";
                        Calendar datetime = Calendar.getInstance();
                        datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        datetime.set(Calendar.MINUTE, minute);
                        if (datetime.get(Calendar.AM_PM) == Calendar.AM)
                            am_pm = "AM";
                        else if (datetime.get(Calendar.AM_PM) == Calendar.PM)
                            am_pm = "PM";
                        String strHrsToShow = (datetime.get(Calendar.HOUR) == 0) ? "12" : datetime.get(Calendar.HOUR) + "";
                        mtime = strHrsToShow + ":" + datetime.get(Calendar.MINUTE) + " " + am_pm;
                        (timetext).setText(mtime);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }


    public void onClickMap(View view) {
        if (isServicesOK()) {
            Intent intent = new Intent(AddAlarm.this, MapActivity.class);
            startActivityForResult(intent, 1);
        }
    }

    public boolean isServicesOK() {
        Log.d(TAG, "isServicesOK: checking google services version");
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(AddAlarm.this);
        if (available == ConnectionResult.SUCCESS) {
            //everything is fine and user can make map requests
            Log.d(TAG, "isServices: Google Play Services is working");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            //an error occured but we can resolve it
            Log.d(TAG, "isServicesOK : an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(AddAlarm.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, " You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    //add reminder button
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onClickAddReminder(View v) {

        if (reminder_title != null) {
                String title = reminder_title.getText().toString();
                String date = datetext.getText().toString();
                String time = mtime;
                Alarms alarms = new Alarms(title, date, time, latitude, longitude, Address);

            AlarmsDBHandler handler = new AlarmsDBHandler(this);

            handler.addAlarm(alarms);

            Toast.makeText(this, date + time +title + latitude+ longitude + Address+" Alarm added in database", Toast.LENGTH_SHORT).show();
            Intent resultIntent = new Intent();
            setResult(Activity.RESULT_OK, resultIntent);
            finish();

//            // Create a new PendingIntent and add it to the AlarmManager
//            PendingIntent pendingIntent = PendingIntent.getService(this, 0,intent, 0);
//            //PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0,intent, 0);
//            AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//            am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
//            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//           // cal.set(Calendar.AM_PM, Calendar.PM);
//            Log.e("add alarm","alarm set");
//            Intent myIntent = new Intent(this, Alarm.class);
//            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, myIntent,0);
//            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0 || requestCode == 1){
                if (resultCode == Activity.RESULT_OK) {
                    latitude = data.getDoubleExtra("lat",0);
                    longitude = data.getDoubleExtra("long",0);
                    Address = data.getStringExtra("address");
                    destination_text.setText(Address);
                    Log.e(TAG, "onClick: NullPointerException: " +latitude + " "+ longitude );
                }
            }
    }
}



