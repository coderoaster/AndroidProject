package com.example.semiproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AddressBookAdapter extends BaseAdapter {

   private Context mContext;
   private int layout;
   private ArrayList<AddressBook> date;
   private LayoutInflater inflater;

    public AddressBookAdapter(Context mContext, int layout, ArrayList<AddressBook> date) {
        this.mContext = mContext;
        this.layout = layout;
        this.date = date;
        this.inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return date.size();
    }

    @Override
    public Object getItem(int i) {
        return date.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView = inflater.inflate(this.layout,parent,false);
        }

        TextView tv_no = convertView.findViewById(R.id.tv_no);
        TextView tv_name = convertView.findViewById(R.id.tv_name);
        TextView tv_phone = convertView.findViewById(R.id.tv_phone);
        TextView tv_address = convertView.findViewById(R.id.tv_address);

        tv_no.setText("no :"+date.get(i).getNo());
        tv_name.setText("이름 :"+date.get(i).getName());
        tv_phone.setText("연락처 :"+date.get(i).getPhone());
        tv_address.setText("관계 :"+date.get(i).getRelation());

        if((i % 2)==1){
            convertView.setBackgroundColor(0x50000000);
        }else{
            convertView.setBackgroundColor(0x50dddddd);
        }

        return convertView;
    }

}
