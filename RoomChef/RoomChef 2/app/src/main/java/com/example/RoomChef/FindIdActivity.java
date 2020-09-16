package com.example.RoomChef;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class FindIdActivity extends AppCompatActivity {

    Button btn_findId;
    EditText edit_email;
    EditText edit_phone;

    TextView fMain, tv_dialog3;
    Intent intent;
    String urlAddr, email, phone, returnPhone, newPW;
    private String centIP = RecipeData.CENIP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_id);

        btn_findId = findViewById(R.id.btn_findId);
        edit_email = findViewById(R.id.findId_email);
        edit_phone = findViewById(R.id.findId_phone);
        fMain = findViewById(R.id.tv_fMain);
        tv_dialog3 = findViewById(R.id.tv_dialog3);




        btn_findId.setOnClickListener(onClickListener);
        fMain.setOnClickListener(onClickListener);
    }
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_findId:
                    email = edit_email.getText().toString(); // 이메일 받고
                    phone = edit_phone.getText().toString(); // 폰번호 받고
                    if (email.length() == 0 || phone.length() == 0) { // 빈칸이 있는지 확인
                        tv_dialog3.setText("이메일 혹은 비밀번호를 확인해주세요.");
                    }
                    else{ // 빈칸 없으면 이메일들고 폰번호 가질러
                        urlAddr = "";
                        urlAddr = "http://" + centIP + ":8080/test/findId.jsp?"; // centIP 는 항상 위에
                        urlAddr = urlAddr + "email=" + email;
                        Log.v("오류",urlAddr);
                        connectionfindIdData();
                    }
                    break;
                case R.id.tv_fMain:
                    intent = new Intent(FindIdActivity.this, LoginActivity.class);
                    startActivity(intent);
                    break;
            }

        }
    };
    private void connectionfindIdData() {

        try {
            final FindIdNetworkTask findIdNetworkTask = new FindIdNetworkTask(FindIdActivity.this, urlAddr);
            returnPhone = findIdNetworkTask.execute().get().toString();

            Log.v("전화번호",returnPhone);
            if (returnPhone.equals("null")){ // 디비에서 가져온 phone 값이 null 이면 이메일이 없어요
                tv_dialog3.setText("이메일을 확인해주세요.");

            }else if(returnPhone.equals(phone)){ // 디비에서 가져온 phone이랑 입력한 phone이 동일하면 비밀번호 재설정
                final LinearLayout linear = (LinearLayout) View.inflate(FindIdActivity.this, R.layout.update_pw, null);
                new AlertDialog.Builder(FindIdActivity.this)
                        .setTitle("새로운 비밀번호를 입력해주세요")
                        .setCancelable(false)
                        .setView(linear)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                EditText edit_pw = linear.findViewById(R.id.update_pw); // 다이얼로그안에 EditText연결
                                newPW = edit_pw.getText().toString(); // 새로운 비밀번호 가져와서 저장
                                urlAddr = "";
                                urlAddr = "http://" + centIP + ":8080/test/update_pw.jsp?"; // 새로운 비밀번호 업데이트하러
                                urlAddr = urlAddr + "email=" + email + "&pw=" + newPW;
                                Log.v("오류",urlAddr);
                                connectUpdateData(); // 업데이트
                                Toast.makeText(FindIdActivity.this,"비밀번호 변경 완료",Toast.LENGTH_SHORT).show(); // 성공
                                intent = new Intent(FindIdActivity.this,EmailLoginActivity.class); // 로그인하러감
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("취소", null)
                        .show();


            }
            else { // 이메일과 전화번호가 디비에 저장되어 있는거랑 다름
                Log.v("받아온거",returnPhone);
                tv_dialog3.setText("전화번호를 확인해주세요.");

            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void connectUpdateData(){
        try {
            InsNetworkTask insertNetworkTask = new InsNetworkTask(FindIdActivity.this, urlAddr);
            insertNetworkTask.execute().get();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}