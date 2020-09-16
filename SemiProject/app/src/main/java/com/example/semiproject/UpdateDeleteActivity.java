package com.example.semiproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;

public class UpdateDeleteActivity extends AppCompatActivity {


    ArrayAdapter<CharSequence> adapter = null;
    UpdateCallNetworkTask networkTask;
    ArrayList<Member> members;
    EditText edit_name, edit_phone, edit_address;
    TextView tv_relation;
    Spinner sp_relation;
    Button btn_update, btn_delete;
    String urlAddr;
    String name, phone, address, relation;
    Intent intent;
    String centIP;
    String imagename;
    String userid;
    ImageView update_img;
    String imgurlAddr = "http://192.168.0.148:8080/test2/imgs/";


    int seq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_delete);
        adapter = ArrayAdapter.createFromResource(this,R.array.relation, android.R.layout.simple_spinner_item);
        sp_relation = findViewById(R.id.relation_update);
        sp_relation.setAdapter(adapter);
        intent = getIntent();
//        seq = Integer.parseInrintent.getStringExtra("seq");
        seq = Integer.parseInt(intent.getStringExtra("seq"));
        userid = intent.getStringExtra("userseq");
        centIP = "192.168.0.148";
        urlAddr = "http://" + centIP + ":8080/test/projectUpdateCall.jsp?";
        urlAddr = urlAddr + "seq=" + seq;


        edit_name = findViewById(R.id.name_update);
        edit_phone = findViewById(R.id.phone_update);
        edit_address = findViewById(R.id.address_update);
        sp_relation = findViewById(R.id.relation_update);
        btn_update = findViewById(R.id.btn_update);
        btn_delete = findViewById(R.id.btn_delete);
        tv_relation = findViewById(R.id.tv_relation_update);
        update_img  =findViewById(R.id.img1_update);

        connectGetData();

        NetworkTask networkTask = new NetworkTask(UpdateDeleteActivity.this, imgurlAddr, update_img);
        // 100 바이트씩 읽어와라 (NetworkTask 의 len 변수를 말한다.)
        networkTask.execute(100);
        //[출처] AndroidStudio 서버에있는 Image 가져오기|작성자 피실






        edit_name.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        edit_phone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)});

        btn_update.setOnClickListener(upClickListener);
        btn_delete.setOnClickListener(deClickListener);
        update_img.setOnClickListener(imgClickListener);

    }


    View.OnClickListener upClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final LinearLayout linear = (LinearLayout) View.inflate(UpdateDeleteActivity.this, R.layout.update, null);
            new AlertDialog.Builder(UpdateDeleteActivity.this)
                    .setTitle("수정하시겠습니까?")
                    .setIcon(R.mipmap.ic_launcher)
                    .setCancelable(false)
                    .setView(linear)
                    .setPositiveButton("수정", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            name = edit_name.getText().toString();
                            phone = edit_phone.getText().toString();
                            address = edit_address.getText().toString();
                            relation = sp_relation.getSelectedItem().toString();


                            try{
                                centIP = "192.168.0.148";
                                urlAddr = "http://" + centIP + ":8080/test/project_update.jsp?";
                                urlAddr = urlAddr + "name=" + name + "&phone=" + phone + "&address=" + address + "&relation=" + relation + "&seq=" + seq+ "&id="+userid ;

                                connectUpdateData();
                                Toast.makeText(UpdateDeleteActivity.this, "Update OK!", Toast.LENGTH_SHORT).show();

                                intent =new Intent(UpdateDeleteActivity.this,SelectAllActivity.class);
                                intent.putExtra("userseq",userid);
                                startActivity(intent);

                            } catch (Exception e ){
                                e.printStackTrace();
                                Toast.makeText(UpdateDeleteActivity.this, "Update Error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(UpdateDeleteActivity.this, "취소하였습니다.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .show();
        }

    };
    private void connectUpdateData(){
        try {
            UpDeNetworkTask upDeNetworkTask = new UpDeNetworkTask(UpdateDeleteActivity.this, urlAddr);
            upDeNetworkTask.execute().get();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    View.OnClickListener deClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final LinearLayout linear = (LinearLayout) View.inflate(UpdateDeleteActivity.this, R.layout.delete, null);
            new AlertDialog.Builder(UpdateDeleteActivity.this)
                    .setTitle("삭제하시겠습니까?")
                    .setIcon(R.mipmap.ic_launcher)
                    .setCancelable(false)
                    .setView(linear)
                    .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            try{
//                                centIP = intent.getStringExtra("centIP");
                                centIP = "192.168.0.148";
                                urlAddr = "http://" + centIP + ":8080/test/project_delete.jsp?";
                                urlAddr = urlAddr + "seq=" + seq;
                                Log.v("오류",urlAddr);
                                connectUpdateData();
                                Toast.makeText(UpdateDeleteActivity.this, "Delete OK!", Toast.LENGTH_SHORT).show();
                                intent =new Intent(UpdateDeleteActivity.this,SelectAllActivity.class);
                                intent.putExtra("userseq",userid);
                                startActivity(intent);
                            } catch (Exception e) {

                                e.printStackTrace();
                                Toast.makeText(UpdateDeleteActivity.this, "Delete Error", Toast.LENGTH_SHORT).show();
                            }
//                            intent = new Intent(UpdateDeleteActivity.this, ListActivity.class);
//                            intent.putExtra("centIP",centIP);

                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(UpdateDeleteActivity.this, "취소하였습니다.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .show();
        }
    };

    View.OnClickListener imgClickListener =new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent;
            intent = new Intent();
            intent.setType("image/*");// 이미지 타입으로 지정
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent,1);
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
                    ConnectFTP connectFTP = new ConnectFTP(UpdateDeleteActivity.this, "192.168.0.148", "host", "qwer1234", 25, file);
                    imagename = connectFTP.imgname();
                    connectFTP.execute();

                    // 선택한 이미지에서 비트맵 생성
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    Bitmap img = BitmapFactory.decodeStream(in);

                    in.close();
                    // 이미지 표시
                    update_img.setImageBitmap(img);


                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    private void connectGetData() {
        try {
            networkTask = new UpdateCallNetworkTask(UpdateDeleteActivity.this,urlAddr,members);
            Object obj = networkTask.execute().get();  //
            members = (ArrayList<Member>) obj;
            edit_name.setText( members.get(0).getName());
            edit_phone.setText(members.get(0).getPhone());
            edit_address.setText(members.get(0).getAddress());
            tv_relation.setText(members.get(0).getRelation());
            imgurlAddr =  imgurlAddr + members.get(0).getImg();
            Log.v("imgurl",imgurlAddr);
        }catch (Exception e){
            e.printStackTrace();
        }


    }
}