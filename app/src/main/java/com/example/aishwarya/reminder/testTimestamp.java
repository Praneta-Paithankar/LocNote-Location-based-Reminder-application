package com.example.aishwarya.reminder;

import java.util.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by aishwarya on 25/04/18.
 */

public class testTimestamp {

    public static void main(String args[]) throws ParseException {


        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, 26);  //1-31
        calendar.set(Calendar.MONTH, 3);  //first month is 0!!! January is zero!!!
        calendar.set(Calendar.YEAR, 2018);

        calendar.set(Calendar.HOUR_OF_DAY, 04);
        calendar.set(Calendar.MINUTE, 30);


        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String test = sdf.format(cal.getTime());

        SimpleDateFormat format = new SimpleDateFormat("hh:mm");


//        Date date1 = format.parse("8:00 pm");
//        Date date2 = format.parse("5:30 pm");
//        long mills = date1.getTime() - date2.getTime();
//


        //long t = cal.getTime();
//        long str_date=System.currentTimeMillis();
//         DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
//        String date =formatter.format((new Date(str_date)));
//        System.out.println("Today is " + mills);
//        Date t = (Date) calendar.getTime();
        System.out.println("String date "+(calendar.getTime().getTime()));

        Long t1 = calendar.getTime().getTime() - cal.getTime().getTime();
        System.out.println("String date "+(t1));
//
//        DateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
//
//        Date date11 = simpleDateFormat.parse("10/10/2013 11:30:10");
//        Date date22 = simpleDateFormat.parse("13/10/2013 20:35:55");
//
//        long different = date11.getTime() - date22.getTime();


    }

}
