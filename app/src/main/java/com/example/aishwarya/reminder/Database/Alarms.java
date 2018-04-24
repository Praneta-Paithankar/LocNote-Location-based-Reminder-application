package com.example.aishwarya.reminder.Database;

/**
 * Created by aishwarya on 24/04/18.
 */

public class Alarms {
    private String mtitle;
    private String mdate;
    private String mtime;
    private double mlatitude;
    private double mlongitude;
    private String maddress;

    public Alarms(String title, String date, String time, double latitude, double longitude, String address){
        mtitle = title;
        mdate = date;
        mtime = time ;
        mlatitude = latitude;
        mlongitude = longitude;
        maddress = address;
    }

    public String getMdate() {
        return mdate;
    }

    public void setMdate(String mdate) {
        this.mdate = mdate;
    }

    public String getMtitle() {
        return mtitle;
    }

    public void setMtitle(String mtitle) {
        this.mtitle = mtitle;
    }

    public String getMtime() {
        return mtime;
    }

    public void setMtime(String mtime) {
        this.mtime = mtime;
    }

    public double getMlatitude() {
        return mlatitude;
    }

    public void setMlatitude(double mlatitude) {
        this.mlatitude = mlatitude;
    }

    public double getMlongitude() {
        return mlongitude;
    }

    public void setMlongitude(double mlongitude) {
        this.mlongitude = mlongitude;
    }

    public String getMaddress() {
        return maddress;
    }

    public void setMaddress(String maddress) {
        this.maddress = maddress;
    }
}
