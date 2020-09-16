package com.example.RoomChef;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.io.InputStream;

public class ReviewActivity extends AppCompatActivity {

    ImageView imageView;
    EditText ed_review, ed_title;
    TextView tv_image;
    Button btn_insert, btn_cancel;
    String email = RecipeData.USERID;
    String centIP = RecipeData.CENIP;
    String urlAddr;
    String image1, review, title;
    Intent intent;
    String rcpseq;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        imageView = findViewById(R.id.insert_img1);
        ed_review = findViewById(R.id.ed_review);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_insert = findViewById(R.id.btn_insert);
        tv_image = findViewById(R.id.select_insert_img);
        ed_title = findViewById(R.id.ed_title);
        ed_title.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15) });

        Intent intent = getIntent();
        rcpseq = intent.getStringExtra("seq");



        btn_insert.setOnClickListener(onClickListener);
        btn_cancel.setOnClickListener(onClickListener);
        imageView.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_insert:
                    urlAddr = "";
                    urlAddr = "http://" + centIP + ":8080/test/review_insert.jsp?";
                    review = ed_review.getText().toString();
                    title = ed_title.getText().toString();
                    if(title.length() == 0 || review.length() == 0 ){
                        new AlertDialog.Builder(ReviewActivity.this)
                                    .setTitle("알림")
                                    .setMessage("빈칸을 입력해주세요.")
                                    .setIcon(R.mipmap.ic_launcher)
                                    .setCancelable(false)
                                    .setPositiveButton("확인", null)
                                    .show();

                    }else{
                    urlAddr = urlAddr + "email=" + email + "&rcpseq=" + rcpseq + "&review=" + review + "&image=" + image1 + "&title=" + title;
                    Log.v("어디로가니",urlAddr);
                    connectionInsertData();
                    Toast.makeText(ReviewActivity.this, email+ "님의 후기가 등록되었습니다.", Toast.LENGTH_LONG).show();
                    intent = new Intent(ReviewActivity.this,MainActivity.class);
                    startActivity(intent);}
                    break;
                case R.id.btn_cancel:
                    finish();
                    Toast.makeText(ReviewActivity.this, "취소되었습니다.", Toast.LENGTH_LONG).show();

                    break;
                case R.id.insert_img1:
                    Intent intent;
                    intent = new Intent();
                    intent.setType("image/*");// 이미지 타입으로 지정
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent,1);
                    break;
            }
        }


    };
    private void connectionInsertData(){
        try {
            InsNetworkTask insNetworkTask = new InsNetworkTask(ReviewActivity.this, urlAddr);
            insNetworkTask.execute().get();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1){
            if(resultCode==RESULT_OK){
                try {
                    Uri file = data.getData();
                    // FTP 접속
                    ConnectFTP connectFTP = new ConnectFTP(ReviewActivity.this, "192.168.0.148", "host", "qwer1234", 25, file);
                    image1 = connectFTP.imgname();
                    Log.v("imgname",image1);
                    connectFTP.execute();

                    // 선택한 이미지에서 비트맵 생성
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    Bitmap img = BitmapFactory.decodeStream(in);

                    in.close();
                    // 이미지 표시
                    imageView.setImageBitmap(img);
                    btn_insert.setEnabled(true);
                    tv_image.setText("");

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

    }
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment).commit();      // Fragment로 사용할 MainActivity내의 layout공간을 선택합니다.
    }
}
