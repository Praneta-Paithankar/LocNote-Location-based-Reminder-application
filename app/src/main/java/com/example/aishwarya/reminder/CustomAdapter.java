package com.example.aishwarya.reminder;

/**
 * Created by aishwarya on 10/04/18.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aishwarya.reminder.Database.Alarms;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends BaseAdapter{
    ArrayList<String> result;
    Context context;

    private static LayoutInflater inflater=null;
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
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.item_main, null);
        holder.tv=(TextView) rowView.findViewById(R.id.textView1);
        holder.tv.setText(result.get(position));
        rowView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(context, "You Clicked "+ result.get(position), Toast.LENGTH_LONG).show();
            }
        });
        return rowView;
    }

}
