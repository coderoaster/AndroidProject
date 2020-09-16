package com.example.RoomChef;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class SelectFood extends Activity {

    String urladdr = "http://192.168.0.148:8080/test/recipe_select.jsp?seq=";
    ArrayList<RecipeData> data;
    SelectAdapter adapter;
    RecyclerView listView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_food);
        listView= findViewById(R.id.select_food_list);




        Intent intent = getIntent();
        String seq = intent.getStringExtra("seq");
        urladdr = urladdr+seq;
        Log.v("url",urladdr);
        connectGetData();
    }
    protected void connectGetData() {
        try {
            NetworkTask networkTask = new NetworkTask(SelectFood.this, urladdr);
            // execute() java 파일안의 메소드 한번에 동작시키기, 메소드를 사용하면 HttpURLConnection 이 제대로 작동하지않는다.
            Object obj = networkTask.execute().get();
            data = (ArrayList<RecipeData>) obj;
            System.out.println(data);

            listView.setLayoutManager(new LinearLayoutManager(SelectFood.this)) ;

            // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
            adapter = new SelectAdapter(data) ;
            listView.setAdapter(adapter) ;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
