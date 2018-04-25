package com.example.aishwarya.reminder;

/**
 * Created by aishwarya on 25/04/18.
 */

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
/**
 * Created by kulsv on 15-04-2018.
 */

public class TimeDistanceCalculation extends AsyncTask<String, Integer, Long>  {
    private Context mContext;

    long duration_seconds;

    private TaskCompleted mCallback;

    public TimeDistanceCalculation(Context context){
        this.mContext = context;
        this.mCallback = (TaskCompleted) context;

    }

    public interface TaskCompleted {
        // Define data you like to return from AysncTask
        public void onTaskComplete(Long result);
    }
    protected Long doInBackground(String... urls) {
        //System.out.print("inside doInBackground function!");
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(urls[0]);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            System.out.println("Exception while downloading url" + e.toString());
        } finally {
            try {
                iStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            urlConnection.disconnect();
        }
        System.out.println("Data ="+ "url"+data.toString());
        String duration = new String();
        try {
            duration = new JSONObject(data).getJSONArray("routes").getJSONObject(0).getJSONArray("legs").
                    getJSONObject(0).getJSONObject("duration").getString("value");
            duration_seconds = Long.parseLong(duration);
            System.out.println("duration_seconds :: " + duration_seconds);
            return duration_seconds;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return 0L;
    }

    @Override
    protected void onPostExecute(Long seconds){
        System.out.println("duration_seconds :: " + seconds);
        mCallback.onTaskComplete(seconds);

    }


}

