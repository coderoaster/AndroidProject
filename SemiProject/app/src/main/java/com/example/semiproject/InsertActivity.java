package com.example.semiproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;

public class InsertActivity extends AppCompatActivity {

    ArrayAdapter<CharSequence> adapter = null;
    Spinner spinner = null;
    String urlAddr;
    String image1, image2, name, phone, address, relation, centIP;
    EditText edit_name, edit_phone, edit_address;
    Spinner sp_relation;
    Button btn_insert, btn_cancel;
    ImageView insert_img;
    TextView select_insert_img;
    Intent intent;
    String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        intent = new Intent();
        userid = intent.getStringExtra("userseq");

        adapter = ArrayAdapter.createFromResource(this,R.array.relation, android.R.layout.simple_spinner_item);
        spinner = findViewById(R.id.sp_relation);
        spinner.setAdapter(adapter);

        edit_name = findViewById(R.id.edit_name);
        edit_phone = findViewById(R.id.edit_phone);
        edit_address = findViewById(R.id.edit_address);
        sp_relation = findViewById(R.id.sp_relation);
        btn_insert = findViewById(R.id.btn_insert);
        btn_cancel = findViewById(R.id.btn_cancel);
        insert_img =findViewById(R.id.insert_img1);
        select_insert_img= findViewById(R.id.select_insert_img);

        edit_name.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4) });
        edit_phone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12) });
        btn_insert.setEnabled(false);

        btn_insert.setOnClickListener(onClickListener);
        btn_cancel.setOnClickListener(onClickListener);
        insert_img.setOnClickListener(onClickListener);
    }
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_insert:
                    urlAddr = "";
                    centIP = "192.168.0.148";
                    urlAddr = "http://" + centIP + ":8080/test/project_insert.jsp?";

                    name = edit_name.getText().toString();
                    phone = edit_phone.getText().toString();
                    address = edit_address.getText().toString();
                    relation = sp_relation.getSelectedItem().toString();
                    Log.v("imgname",image1);

                    urlAddr = urlAddr + "name=" + name + "&phone=" + phone + "&address=" + address + "&relation=" + relation+"&img="+image1+"id="+userid;
//                    Toast.makeText(InsertActivity.this, urlAddr, Toast.LENGTH_LONG).show();
                    Toast.makeText(InsertActivity.this, name+ "님의 정보가 등록되었습니다.", Toast.LENGTH_LONG).show();
                    connectionInsertData();
                    break;
                case R.id.btn_cancel:
                    finish();
                    Toast.makeText(InsertActivity.this, "취소되었습니다.", Toast.LENGTH_LONG).show();
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
                    private void connectionInsertData(){
                try {
                    InsNetworkTask insNetworkTask = new InsNetworkTask(InsertActivity.this, urlAddr);
                    insNetworkTask.execute().get();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1){
            if(resultCode==RESULT_OK){
                try {
                    Uri file = data.getData();
                    // FTP 접속
                    ConnectFTP connectFTP = new ConnectFTP(InsertActivity.this, "192.168.0.148", "host", "qwer1234", 25, file);
                    image1 = connectFTP.imgname();
                    Log.v("imgname",image1);
                    connectFTP.execute();

                    // 선택한 이미지에서 비트맵 생성
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    Bitmap img = BitmapFactory.decodeStream(in);

                    in.close();
                    // 이미지 표시
                    insert_img.setImageBitmap(img);
                    btn_insert.setEnabled(true);
                    select_insert_img.setText("");

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

    }
}