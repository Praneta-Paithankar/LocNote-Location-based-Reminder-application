package com.example.aishwarya.reminder;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.aishwarya.reminder.Database.Alarms;
import com.example.aishwarya.reminder.Database.AlarmsDBHandler;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView lv;
    List<Alarms> AllAlarms;
    ArrayList<String> item_alarm;
    CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv=(ListView) findViewById(R.id.listView);
        AlarmsDBHandler handler = new AlarmsDBHandler(this);
        AllAlarms = handler.findAll();
        item_alarm = new ArrayList<String>();
        if(AllAlarms.size() != 0)
        {
            System.out.println(AllAlarms);
           // String title = AllAlarms.get(1).getMtitle();
            for(int i =0;i<AllAlarms.size();i++){
                item_alarm.add("Title:"+AllAlarms.get(i).getMtitle() +
                        "\n"+ "Date:"+AllAlarms.get(i).getMdate() +
                        "\n"+ "Time:"+AllAlarms.get(i).getMtime()+
                        "\n"+ "Address:"+AllAlarms.get(i).getMaddress()
                );
            }

            customAdapter = new CustomAdapter(this, item_alarm);
            lv.setAdapter(customAdapter);
        }


    }

    public void onClickFloatingButton(View v){
        Intent intent = new Intent(MainActivity.this, AddAlarm.class);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 || requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                AlarmsDBHandler handler = new AlarmsDBHandler(this);
                AllAlarms = handler.findAll();
                item_alarm.clear();
                if (AllAlarms.size() != 0) {
                    System.out.println(AllAlarms);
                    // String title = AllAlarms.get(1).getMtitle();
                    for (int i = 0; i < AllAlarms.size(); i++) {
                        item_alarm.add("Title:" + AllAlarms.get(i).getMtitle() +
                                "\n" + "Date:" + AllAlarms.get(i).getMdate() +
                                "\n" + "Time:" + AllAlarms.get(i).getMtime() +
                                "\n" + "Address:" + AllAlarms.get(i).getMaddress()
                        );
                    }
                    customAdapter.notifyDataSetChanged();
                    lv.setAdapter(customAdapter);
                }
            }
        }
    }
}
