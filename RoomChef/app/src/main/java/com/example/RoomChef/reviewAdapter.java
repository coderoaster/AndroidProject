package com.example.RoomChef;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class reviewAdapter extends BaseAdapter {
    private Context mContext = null;
    private int layout = 0;
    private ArrayList<Review> data = null;
    private LayoutInflater inflater = null;

    public reviewAdapter(Context mContext, int layout, ArrayList<Review> data) {
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
        ImageView iv_review = convertView.findViewById(R.id.iv_review);
        TextView title = convertView.findViewById(R.id.tv_title);
        String  imgurlAddr ="http://192.168.0.148:8080/test2/imgs/";
        imgurlAddr =  imgurlAddr + data.get(position).getImage();

        title.setText(data.get(position).getTitle());

        Glide.with(mContext).load(imgurlAddr).into(iv_review);
        return convertView;
    }
}
