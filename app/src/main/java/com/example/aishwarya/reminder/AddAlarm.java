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
import android.os.AsyncTask;
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
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class AddAlarm extends AppCompatActivity implements TimeDistanceCalculation.TaskCompleted, AdapterView.OnItemSelectedListener {
    TextView btnDate, datetext, btnTime, timetext, destination_text;
    private Calendar calendar;
    Spinner spinner1;
    EditText reminder_title;
    FloatingActionButton addalarm;
    String mtime;
    String mode;
    StringBuilder mdate;
    Calendar cal;
    double latitude;
    double longitude;
    String Address;
    int flag = 0;
    long current_timestamp = 0;
    int id = 0;
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
        spinner1 = (Spinner) findViewById(R.id.spinner);
        calendar = Calendar.getInstance();
        cal = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        Bundle b = getIntent().getExtras();
        if(b!=null){
            reminder_title.setText(b.getString("title"));
            datetext.setText(b.getString("date"));
            timetext.setText(b.getString("time"));
            destination_text.setText(b.getString("address"));
            current_timestamp = b.getLong("timestamp");
            calendar.setTimeInMillis(current_timestamp);
            latitude = b.getDouble("lat");
            longitude = b.getDouble("long");
            flag = b.getInt("flag");
            id = b.getInt("id");
            mode = b.getString("mode");

        }
        spinner1.setOnItemSelectedListener(AddAlarm.this);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        mode= parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }


    //date picker dialog
    @SuppressWarnings("deprecation")
    public void onClickDate(View view) {
        showDialog(999);
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    myDateListener, year, month, day);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            return datePickerDialog;
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

    private StringBuilder showDate(int year, int month, int day) {
        mdate = new StringBuilder().append(day).append("-")
                .append(month).append("-").append(year);
            calendar.set(Calendar.DATE, day);  //1-31
            calendar.set(Calendar.MONTH, month-1);  //first month is 0!!! January is zero!!!
            calendar.set(Calendar.YEAR, year);

            datetext.setText(mdate);
            return mdate;
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

                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        if (calendar.get(Calendar.AM_PM) == Calendar.AM)
                            am_pm = "AM";
                        else if (calendar.get(Calendar.AM_PM) == Calendar.PM)
                            am_pm = "PM";
                        String strHrsToShow = (calendar.get(Calendar.HOUR) == 0) ? "12" : calendar.get(Calendar.HOUR) + "";
                        mtime = String.format("%s:%02d %s",strHrsToShow,calendar.get(Calendar.MINUTE), am_pm);

                        if((calendar.getTime().getTime()< System.currentTimeMillis()))
                        {
                            Toast.makeText(AddAlarm.this, " Please add valid time", Toast.LENGTH_SHORT).show();
                            timetext.setText("");
                        }
                        else {
                            (timetext).setText(mtime);
                        }
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

    //add reminder button
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onClickAddReminder(View v) throws ParseException, ExecutionException, InterruptedException {
        //if any fields is missing
        if((reminder_title.getText().toString().equals("") || (datetext.getText().toString().equals("Date")
                || timetext.getText().toString().equals("Time")
                || destination_text.getText().toString().equals("Destination"))) && flag == 0){
            Toast.makeText(this, "Please add all fields",Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Validation" + calendar.getTimeInMillis());

        }
        else {
            String title = reminder_title.getText().toString();
            String date = datetext.getText().toString();
            String time = timetext.getText().toString();
            String address = destination_text.getText().toString();
            try {

                if(current_timestamp!=calendar.getTimeInMillis()) {
                    current_timestamp = calendar.getTimeInMillis();
                }
                Log.d(TAG, "Timestamp" + calendar.getTimeInMillis());
            } catch (Exception e) {
            }
            Alarms alarms = new Alarms(title, date, time, latitude, longitude, address, current_timestamp, mode);
            AlarmsDBHandler handler = new AlarmsDBHandler(this);

            Alarms previous_alarm = handler.findPreviousAlarm(alarms);
            AlarmsDBHandler handler2 = new AlarmsDBHandler(this);
            Alarms next_alarm = handler2.findNextAlarm(alarms);

            //same alarm exists
            if(handler.isRecordClashing(alarms.getMdate(),alarms.getMtime()) && flag==0)
            {
                Toast.makeText(this, "Already Alarm exist at same time and date",Toast.LENGTH_SHORT).show();
                return;
            }

            //clash with previous existing alarm
            else if(previous_alarm!=null ) {
                long previous_timestamp = previous_alarm.getMtimestamp();
                long actual_time_seconds = Math.abs(previous_timestamp - current_timestamp);

                Log.d(TAG, " previous alarm fetched" + (actual_time_seconds / 1000.0));
                long fetched_time1 = ValidateTime(previous_alarm,"prev");
                Log.d(TAG, " actual alarm fetched" + fetched_time1);
                Log.d(TAG, " Difference" + (fetched_time1 - (actual_time_seconds / 1000.0)));
                if (((actual_time_seconds / 1000.0) - fetched_time1) <= 0) {
                    Toast.makeText(this, "Selected Alarm clashes with previous set Alarm", Toast.LENGTH_SHORT).show();
                    return;
                }

                else{
                    if(flag==1){

                        handler.updateAlarm(alarms,id);
                    }
                    else {
                        handler.addAlarm(alarms);
                    }
                    Intent resultIntent = new Intent();
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                }
            }

            //clash with next existing alarm
            else if(next_alarm!=null){
                long next_timestamp = next_alarm.getMtimestamp();
                long actual_time_seconds = Math.abs(next_timestamp - current_timestamp);

                Log.d(TAG, " next alarm fetched" + (actual_time_seconds/1000.0));
                long fetched_time12 = ValidateTime(next_alarm,"next");
                Log.d(TAG, " actual alarm fetched" + fetched_time12);
                Log.d(TAG, " Difference" + (fetched_time12-(actual_time_seconds/1000.0)));
                if(((actual_time_seconds/1000.0)-fetched_time12)<=0){
                    Toast.makeText(this, "Selected Alarm clashes with next set Alarm",Toast.LENGTH_SHORT).show();
                }
                else{
                    if(flag==1){
                        handler.updateAlarm(alarms,id);
                    }
                    else {
                        handler.addAlarm(alarms);
                    }

                    Intent resultIntent = new Intent();
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                }
            }

            else {
                if(flag==1){
                    handler.updateAlarm(alarms,id);
                }
                else {
                    handler.addAlarm(alarms);
                }

                Intent resultIntent = new Intent();
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
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

    public long ValidateTime(Alarms alarms,String flag) throws ExecutionException, InterruptedException {
        String str_origin= null;
        String str_dest=null;
        //set source as the previous alarm fetched and destination as the current set alarm
        if(flag.equals("prev")) {
            double source_lat = alarms.getMlatitude();
            double source_long = alarms.getMlongitude();
            str_origin = "origin=" + source_lat + "," + source_long;
            // Destination of route
            str_dest = "destination=" + latitude + "," + longitude;
        }
        //next alarm will be my destination and current fetched alarm will be my source
        else if(flag.equals("next"))
        {
            double source_lat = latitude;
            double source_long = longitude;
            mode = alarms.getMmode();

            str_origin = "origin=" + source_lat + "," + source_long;

            // Destination of route
            str_dest = "destination=" + alarms.getMlatitude() + "," + alarms.getMlongitude();
        }

        // Sensor enabled
        String sensor = "sensor=false";
        // Travelling mode enable
        String mode1 = "mode="+mode;

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&"+ mode1;

        // Output format
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        System.out.println("url :: " + url);
        String response = new String();
        long result1 = new TimeDistanceCalculation(this).execute(url).get();
        return result1;

    }

    @Override
    public void onTaskComplete(Long result) {

        Log.e(TAG, "Seconds"+ result );
    }

}

