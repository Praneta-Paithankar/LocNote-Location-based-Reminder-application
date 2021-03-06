package com.example.aishwarya.reminder;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.aishwarya.reminder.Database.Alarms;
import com.example.aishwarya.reminder.Database.AlarmsDBHandler;
import com.example.aishwarya.reminder.Maps.MapActivity;

import java.util.ArrayList;
import java.util.List;
import com.amulyakhare.textdrawable.util.ColorGenerator;

public class MainActivity extends AppCompatActivity {

    ListView lv;
    List<Alarms> AllAlarms;
    ArrayList<String> item_alarm;
    CustomAdapter customAdapter;
    private static LayoutInflater inflater=null;
    int flag = 0;
    int listflag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv=(ListView) findViewById(R.id.listView);
        UpdateListView();
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
                listflag = 1;
                UpdateListView();
                listflag = 0;
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
            Button icon;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            final CustomAdapter.Holder holder=new CustomAdapter.Holder();
            View rowView ;
                rowView = inflater.inflate(R.layout.item_main, null);
                holder.tv = (TextView) rowView.findViewById(R.id.textView);
                holder.tv.setText(result.get(position));
                holder.icon = (Button) rowView.findViewById(R.id.round_icon);
                rowView.setTag(holder);
            ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
            // generate random color
            int color = generator.getColor(getItem(position));

            holder.icon.setText(AllAlarms.get(position).getMtitle().substring(0, 1));
            holder.icon.setBackgroundColor(color);


            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LayoutInflater li = LayoutInflater.from(context);
                    final View vi = li.inflate(R.layout.alertbox_del_update, null);
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setView(vi);
                    builder.setCancelable(true);
                    builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which){
                            Bundle bundle = new Bundle();
                            Intent intent = new Intent(MainActivity.this, AddAlarm.class);
                            bundle.putInt("id",AllAlarms.get(position).getMid());
                            bundle.putDouble("lat",AllAlarms.get(position).getMlatitude());
                            bundle.putDouble("long",AllAlarms.get(position).getMlongitude());
                            bundle.putLong("timestamp",AllAlarms.get(position).getMtimestamp());
                            bundle.putString("title", AllAlarms.get(position).getMtitle());
                            bundle.putString("date", AllAlarms.get(position).getMdate());
                            bundle.putString("time", AllAlarms.get(position).getMtime());
                            bundle.putString("address", AllAlarms.get(position).getMaddress());
                            bundle.putString("mode",AllAlarms.get(position).getMmode());
                            flag = 1;
                            bundle.putInt("flag",flag);
                            intent.putExtras(bundle);
                            startActivityForResult(intent, 1);
                        }});
                    builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialogInterface, int j) {
                            AlarmsDBHandler handler = new AlarmsDBHandler(MainActivity.this);
                            handler.deleteAlarm(AllAlarms.get(position));
                            listflag = 1;
                            UpdateListView();
                            listflag = 0;
                            Toast.makeText(MainActivity.this, "Successfully deleted", Toast.LENGTH_SHORT).show();
                            }

                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                    // TODO Auto-generated method stub

                }

            });
            return rowView;
        }

    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("isMyServiceRunning?", true+"");
                return true;
            }
        }
        Log.i ("isMyServiceRunning?", false+"");
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();

        Intent intent = new Intent(this, LocationService.class);
        if (!isMyServiceRunning(LocationService.class)) {
            startService(intent);
        }
    }

    public void UpdateListView(){
        AlarmsDBHandler handler = new AlarmsDBHandler(this);
        AllAlarms = handler.findAll();
        item_alarm = new ArrayList<String>();
        if(listflag==1){
            item_alarm.clear();
        }
        if(AllAlarms.size() != 0) {
            System.out.println(AllAlarms);
            for (int i = 0; i < AllAlarms.size(); i++) {
                item_alarm.add(AllAlarms.get(i).getMtitle() +
                        "\n" + "Date:\t" + AllAlarms.get(i).getMdate() +
                        "\n" + "Time:\t" + AllAlarms.get(i).getMtime() +
                        "\n" + "Address:\t" + AllAlarms.get(i).getMaddress()
                );

            }
        }
        customAdapter = new CustomAdapter(this, item_alarm);
        lv.setAdapter(customAdapter);

    }
}

