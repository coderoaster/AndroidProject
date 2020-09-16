package com.example.RoomChef;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ReviewDetail extends AppCompatActivity {
    TextView title_detail, review_detail;
    Button btn_goList;
    ImageView imageView;
    ArrayList<Review> data;
    String centIP = RecipeData.CENIP;
    String urlAddr;
    String email = RecipeData.USERID;
    Intent intent;
    int seq;

    String imgurlAddr ="http://" + centIP + ":8080/test2/imgs/";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_detail);
        intent = getIntent();
        seq = Integer.parseInt(intent.getStringExtra("seq"));


        review_detail = findViewById(R.id.review_detail);
        title_detail = findViewById(R.id.title_detail);
        imageView = findViewById(R.id.img_detail);
        btn_goList = findViewById(R.id.btn_goList);

        btn_goList.setOnClickListener(OnClickListener);

        connectGetData();
    }

    protected void connectGetData() { // 상세페이지 불러오기
        try {

            urlAddr = "http://" + centIP + ":8080/test/click_review.jsp?";
            urlAddr = urlAddr + "&seq=" + seq;

            Log.v("urlAddr",urlAddr);

            reviewNetworkTask networkTask = new reviewNetworkTask(ReviewDetail.this, urlAddr);

            // execute() java 파일안의 메소드 한번에 동작시키기, 메소드를 사용하면 HttpURLConnection 이 제대로 작동하지않는다.
            Object obj = networkTask.execute().get();
            data = (ArrayList<Review>) obj;
            String title = data.get(0).getTitle();
            String review = data.get(0).getContent();
            imgurlAddr = imgurlAddr+ data.get(0).getImage();
            Glide.with(ReviewDetail.this).load(imgurlAddr).into(imageView);
            title_detail.setText(title);
            review_detail.setText(review);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    View.OnClickListener OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_goList:
                    finish();
                    break;
            }

        }
    };
}
