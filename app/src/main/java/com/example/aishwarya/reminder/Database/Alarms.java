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
    private long mtimestamp;
    private int mid;
    private String mmode;

    public Alarms(int id, String title, String date, String time, double latitude, double longitude, String address, long timestamp_dummy, String mode){
        mid = id;
        mtitle = title;
        mdate = date;
        mtime = time ;
        mlatitude = latitude;
        mlongitude = longitude;
        maddress = address;
        mtimestamp = timestamp_dummy;
        mmode = mode;
    }

    public Alarms( String title, String date, String time, double latitude, double longitude, String address, long timestamp_dummy, String mode){
        mtitle = title;
        mdate = date;
        mtime = time ;
        mlatitude = latitude;
        mlongitude = longitude;
        maddress = address;
        mtimestamp = timestamp_dummy;
        mmode = mode;
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

    public long getMtimestamp() {
        return mtimestamp;
    }

    public void setMtimestamp(long mtimestamp) {
        this.mtimestamp = mtimestamp;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public String getMmode() {
        return mmode;
    }

    public void setMmode(String mmode) {
        this.mmode = mmode;
    }
}
