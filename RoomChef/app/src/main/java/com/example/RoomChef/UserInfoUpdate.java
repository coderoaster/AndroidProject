package com.example.RoomChef;

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

import com.bumptech.glide.Glide;

import java.io.InputStream;
import java.util.ArrayList;


public class UserInfoUpdate extends AppCompatActivity {

    ArrayList<UserInfo> beans;
    String centIP = RecipeData.CENIP;
    String email = RecipeData.USERID;
    EditText Newpw, NewpwChk, Phone;
    TextView tv_chk;
    ImageView img_update;
    Button btn_update, btn_cancel;
    String urlAddr, sPhone, sPw, sPwChk, image, nowPw;
    Intent intent;

    String imgurlAddr ="http://" + centIP + ":8080/test2/imgs/";




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info_update);





        img_update = findViewById(R.id.img_updqteInfo);
        btn_update = findViewById(R.id.btn_update);
        btn_cancel = findViewById(R.id.btn_update_cancel);
        Phone = findViewById(R.id.edit_phone);
        Newpw = findViewById(R.id.edit_update_pw);
        NewpwChk = findViewById(R.id.edit_update_pw2);
        tv_chk = findViewById(R.id.tv_update_chk);

        intent = getIntent();
        Phone.setText(intent.getStringExtra("phone"));
        nowPw = intent.getStringExtra("nowPw");
        image = intent.getStringExtra("image");

        if(image.equals("null")){

        }else {
            imgurlAddr = imgurlAddr + image;
            Glide.with(UserInfoUpdate.this).load(imgurlAddr).into(img_update);
        }


        btn_update.setOnClickListener(onClickListener);
        btn_cancel.setOnClickListener(onClickListener);
        img_update.setOnClickListener(onClickListener);

        Phone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)});


    }


    /// 개인정보 수정 버튼
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_update:
                    urlAddr = "";
                    sPhone = Phone.getText().toString().trim();
                    sPw = Newpw.getText().toString().trim();
                    sPwChk = NewpwChk.getText().toString().trim();
                    if (sPw.equals(sPwChk)) {
                        urlAddr = "http://" + centIP + ":8080/test/project_userInfoUpdate.jsp?";
                        if(sPw.equals("") || sPw.length() == 0 || sPw.isEmpty()){
                            urlAddr = urlAddr + "phone=" + sPhone + "&password=" + nowPw + "&email=" + email + "&image=" + image;
                        }else {
                            urlAddr = urlAddr + "phone=" + sPhone + "&password=" + sPw + "&email=" + email + "&image=" + image;
                        }
                            Log.v("urlAddr", urlAddr);
                            connectUpdateData();
                            Toast.makeText(UserInfoUpdate.this, "완료되었습니다.", Toast.LENGTH_SHORT).show();
                            finish();

                    } else {
                        tv_chk.setText("비밀번호가 일치하지 않습니다.");
                    }
                    break;
                case R.id.btn_update_cancel:
                    finish();
                    break;
                case R.id.img_updqteInfo:
                    intent = new Intent();
                    intent.setType("image/*");// 이미지 타입으로 지정
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent,1);
                    break;

            }
        }
    };
        private void connectUpdateData(){
            try{
                UpdateNetworkTask updateNetworkTask = new UpdateNetworkTask(UserInfoUpdate.this,urlAddr);
                updateNetworkTask.execute().get();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            if(resultCode == UserInfoUpdate.this.RESULT_OK){
                try {
                    Uri file = data.getData();
                    Log.v("이거뭐임",file.toString());
                    // FTP 접속
                    ConnectFTP connectFTP = new ConnectFTP(UserInfoUpdate.this, "172.30.1.33", "Sookjeon", "qwer1234", 25, file);
                    image = connectFTP.imgname();
                    Log.v("imgname",image);
                    connectFTP.execute();

                    // 선택한 이미지에서 비트맵 생성
                    InputStream in = UserInfoUpdate.this.getContentResolver().openInputStream(data.getData());
                    Bitmap img = BitmapFactory.decodeStream(in);

                    in.close();
                    // 이미지 표시
                    img_update.setImageBitmap(img);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

    }


}//--

