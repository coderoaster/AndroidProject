package com.example.RoomChef;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ingredientAdapter extends BaseAdapter {
    private Context mContext = null;
    private int layout = 0;
    private ArrayList<ingredient> data = null;
    private LayoutInflater inflater = null;
//    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd"); // 날짜 포맷
//    Date date;
//    Date date2;
//    String timeStamp = mFormat.format(new Date());
//    String expireDate;

    public ingredientAdapter(Context mContext, int layout, ArrayList<ingredient> data) {
        this.mContext = mContext;
        this.layout = layout;
        this.data = data;
        this.inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position).getSeq();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(this.layout, parent, false);
        }
        TextView tv_name = convertView.findViewById(R.id.tv_ingredient);
        TextView tv_date = convertView.findViewById(R.id.tv_expire);

        tv_name.setText(data.get(position).getName());
        tv_date.setText(data.get(position).getDate());

//        Log.v("date",timeStamp);
//        expireDate = data.get(position).getDate();
//        Log.v("디비",expireDate);
//
//        try {
//            date = mFormat.parse(timeStamp);
//            date2 = mFormat.parse(expireDate);
//
//
//            Log.v("뭐지",expireDate);
//            Log.v("date2",date2.toString());
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        int compare = date2.compareTo(date);
//        if(compare < 0){
//            tv_date.setTextColor(Color.parseColor("#BD0000"));
//        }

        return convertView;
    }
}
