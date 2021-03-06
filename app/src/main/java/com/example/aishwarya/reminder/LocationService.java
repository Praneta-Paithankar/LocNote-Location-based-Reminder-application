package com.example.aishwarya.reminder;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.example.aishwarya.reminder.Database.Alarms;
import com.example.aishwarya.reminder.Database.AlarmsDBHandler;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.concurrent.ExecutionException;

import javax.xml.transform.dom.DOMLocator;


public class LocationService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,TimeDistanceCalculation.TaskCompleted
{
    public static final String ACTION_LOCATION_BROADCAST = LocationService.class.getName() + "LocationBroadcast";
    public static final String EXTRA_LATITUDE = "extra_latitude";
    public static final String EXTRA_LONGITUDE = "extra_longitude";
    private static final String TAG = LocationService.class.getSimpleName();
    private int novibrate =0;
    //String latitude;
    //String longitude;
    GoogleApiClient mLocationClient;
    LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private FusedLocationProviderClient mFusedLocationClient;
    private NotificationManager notificationManager;
    NotificationCompat.Builder mBuilder;

    @SuppressLint("RestrictedApi")
    public LocationService() {

        mLocationRequest = new LocationRequest();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mLocationClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationRequest.setInterval(600000);
        mLocationRequest.setFastestInterval(600000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationClient.connect();
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        mBuilder = new NotificationCompat.Builder(this, "LocationNotify")
                .setSmallIcon(com.google.android.gms.R.drawable.common_google_signin_btn_icon_dark)
                .setContentTitle("Location")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setDefaults(Notification.DEFAULT_LIGHTS);

        //LED
        mBuilder.setLights(Color.RED, 3000, 3000);

        notificationManager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            NotificationChannel channel = new NotificationChannel("LocationNotify", "Location Notification", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Sends update of location");
            // Register the channel with the system
            notificationManager.createNotificationChannel(channel);
        }



        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);


                if (isNetworkAvailable()) {
                    Location location = locationResult.getLastLocation();
                    long time = 0;
                    long time_difference = 0;
                    Alarms next_alarm = null;
                    //fetch the immediate next alarm from the database
                    AlarmsDBHandler handler = new AlarmsDBHandler(LocationService.this);
                    long current_time = System.currentTimeMillis();
                    next_alarm = handler.findNextLocAlarm(current_time);
                    if (next_alarm != null) {


                        time_difference = next_alarm.getMtimestamp() - current_time;
                        Log.d("LocationService", String.valueOf(current_time));
                        Log.d("LocationService", String.valueOf(next_alarm.getMtitle()));
                        Log.d("LocationService", String.valueOf(time_difference));

                        try {
                            time = CalculateTimeRequired(location, next_alarm.getMlatitude(), next_alarm.getMlongitude(),next_alarm.getMmode());
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //keeping buffer of 10 minutes if the time difference is
                        // less than the time taken to reach the destination then notify user\
                        if (((time_difference - 600000) <= (time * 1000))) {
                            mBuilder.setContentText(next_alarm.getMtitle() + " " + (time / 60) + " minutes away!" );
                            if(novibrate==0 || ((time_difference) <= (time * 1000))) {
                                mBuilder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
                            }
                            notificationManager.notify(1, mBuilder.build());
                        }
                    }
                }
                else{
                    mBuilder.setContentText("Internet Conection Lost!!");
                    notificationManager.notify(1, mBuilder.build());
                }
            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.d(TAG, "== Error On onConnected() Permission not granted");
            return;
        }
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);


    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "Connection suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "Failed to connect to Google API");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("EXIT", "ondestroy!");
        Intent broadcastIntent = new Intent("LocationServiceRestart");
        sendBroadcast(broadcastIntent);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void sendMessageToUI(String lat, String lng) {

        Log.d(TAG, "Sending info...");
        Intent intent = new Intent(ACTION_LOCATION_BROADCAST);
        intent.putExtra(EXTRA_LATITUDE, lat);
        intent.putExtra(EXTRA_LONGITUDE, lng);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    public long CalculateTimeRequired(Location location, double latitude, double longitude, String mode) throws ExecutionException, InterruptedException {
        double source_lat = location.getLatitude();
        double source_long = location.getLongitude();
        String str_origin = "origin=" + source_lat + "," + source_long;
        // Destination of route
        String str_dest = "destination="+(latitude)+","+(longitude);// + latitude + "," + longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        // Travelling mode enable
        String mode1 = "mode=" + mode;

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&"+ mode1;

        // Output format
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        System.out.println("url :: " + url);
//        String response = new String();
        long result11 = new TimeDistanceCalculation(this).execute(url).get();
        Log.d("LocationService", "time by driving"+String.valueOf(result11));
        return result11;
    }

    @Override
    public void onTaskComplete(Long result) {

      //  Toast.makeText(this,"The result is " + Long.toString(result),Toast.LENGTH_LONG).show();
        Log.e(TAG, "Seconds"+ result );
    }

}