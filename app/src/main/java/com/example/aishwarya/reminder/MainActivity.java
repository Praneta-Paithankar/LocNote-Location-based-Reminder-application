package com.example.aishwarya.reminder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aishwarya.reminder.Database.Alarms;
import com.example.aishwarya.reminder.Database.AlarmsDBHandler;
import com.example.aishwarya.reminder.Maps.MapActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView lv;
    List<Alarms> AllAlarms;
    ArrayList<String> item_alarm;
    CustomAdapter customAdapter;
    private static LayoutInflater inflater=null;

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
                        "\n"+ "Address:"+AllAlarms.get(i).getMaddress() +
                        "\n" + "Timestamp:" + AllAlarms.get(i).getMtimestamp()

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
                                "\n" + "Address:" + AllAlarms.get(i).getMaddress() +
                                "\n" + "Timestamp:" + AllAlarms.get(i).getMtimestamp()


                        );
                    }
                    customAdapter = new CustomAdapter(this, item_alarm);
                    //customAdapter.notifyDataSetChanged();
                    lv.setAdapter(customAdapter);
                }
            }
        }
    }

    public class CustomAdapter extends BaseAdapter {
        ArrayList<String> result;
        Context context;

        public CustomAdapter(MainActivity mainActivity, ArrayList<String> prgmNameList) {
            // TODO Auto-generated constructor stub
            result=prgmNameList;
            context=mainActivity;
            inflater = ( LayoutInflater )context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return result.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public class Holder
        {
            TextView tv;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            CustomAdapter.Holder holder=new CustomAdapter.Holder();
            View rowView;
            rowView = inflater.inflate(R.layout.item_main, null);
            holder.tv=(TextView) rowView.findViewById(R.id.textView1);
            holder.tv.setText(result.get(position));
            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Toast.makeText(context, "You Clicked "+ result.get(position), Toast.LENGTH_LONG).show();
                    Bundle bundle = new Bundle();
                    Intent intent = new Intent(MainActivity.this, AddAlarm.class);
                    bundle.putString("title", AllAlarms.get(position).getMtitle());
                    bundle.putString("date", AllAlarms.get(position).getMdate());
                    bundle.putString("time", AllAlarms.get(position).getMtime());
                    bundle.putString("address", AllAlarms.get(position).getMaddress());
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 1);
                }

            });
            return rowView;
        }

    }
}
