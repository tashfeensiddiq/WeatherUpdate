package com.devmukul.weatherupdate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MyAdapter extends BaseAdapter{
    Context c;
    List<WeatherData> listData;

    public MyAdapter(Context c, List<WeatherData> listData) {
        this.c = c;
        this.listData = listData;
    }

    @Override
    public int getCount() {
        return listData.size();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflate = LayoutInflater.from(c);
        convertView = inflate.inflate(R.layout.list_item, parent,false);
        ImageView imgV = (ImageView) convertView.findViewById(R.id.image1);
        TextView date = (TextView) convertView.findViewById(R.id.dt_text);
        TextView max = (TextView) convertView.findViewById(R.id.max_text);
        TextView min = (TextView) convertView.findViewById(R.id.min_text);

        long ms = Long.parseLong(listData.get(position).getDt());
        Date d = new Date(ms*1000);
        SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
        String newDate= dateformat.format(d);
        date.setText(newDate);
        //date.setText(listData.get(position).getDt());
        max.setText("Max :"+listData.get(position).getMax());
        min.setText("Min :"+listData.get(position).getMin());
        //imgV.setImageResource(img[position]);

        return convertView;
    }



    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}
