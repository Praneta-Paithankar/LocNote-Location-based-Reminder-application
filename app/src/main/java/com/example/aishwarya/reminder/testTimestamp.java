package com.example.aishwarya.reminder;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by aishwarya on 25/04/18.
 */

public class testTimestamp {

    public static void main(String args[]) throws ParseException {


        //   long str_date=System.currentTimeMillis();
        // DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        //Date date = (Date)formatter.parse(String.valueOf(str_date));
        System.out.println("Today is " + System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        Date currentTime = Calendar.getInstance().getTime();
        if (calendar.get(Calendar.AM_PM) == 0){
            String current_time =
                    (calendar.get(Calendar.HOUR) == 0) ? "12" : calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE) + " AM" ;
        System.out.println("Today is " + current_time);
    }
        else if (calendar.get(Calendar.AM_PM) == Calendar.PM) {
            String current_time =
                    (calendar.get(Calendar.HOUR) == 0) ? "12" : calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE) + " PM" ;
            System.out.println("Today is " + current_time);

        }

    }

}
