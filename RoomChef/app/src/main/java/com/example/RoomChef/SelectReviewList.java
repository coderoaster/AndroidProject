package com.example.RoomChef;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class SelectReviewList extends Activity {

    ListView listView;
    String centIP = RecipeData.CENIP;
    String urlAddr;
    String email = RecipeData.USERID;
    ArrayList<Review> data;
    reviewAdapter adapter;
    String seq;
    String rcpseq;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reviewlist);
        listView = findViewById(R.id.lv_review);

        Intent intent = getIntent();
        rcpseq = intent.getStringExtra("seq");
        String name = intent.getStringExtra("name");
        TextView listname = findViewById(R.id.review_recipe_name);
        listname.setText(name);

        connectGetData();

        listView.setOnItemClickListener(onClickListener);
    }
    final ListView.OnItemClickListener onClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            seq = Integer.toString(data.get(i).getSeq());
            Log.v("레시피번호",seq);
            Intent intent = new Intent(SelectReviewList.this, ReviewDetail.class);
            intent.putExtra("seq",seq);

            startActivity(intent);
        }
    };

    protected void connectGetData() { // 리스트 불러오기
        try {
            urlAddr = "http://" + centIP + ":8080/test/select_food_review.jsp?";
            urlAddr = urlAddr + "seq=" + rcpseq;

            Log.v("urlAddr",urlAddr);
            reviewNetworkTask networkTask = new reviewNetworkTask(SelectReviewList.this, urlAddr);

            // execute() java 파일안의 메소드 한번에 동작시키기, 메소드를 사용하면 HttpURLConnection 이 제대로 작동하지않는다.
            Object obj = networkTask.execute().get();
            data = (ArrayList<Review>) obj;

            adapter = new reviewAdapter(SelectReviewList.this, R.layout.reviewlistview, data);
            listView.setAdapter(adapter);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
