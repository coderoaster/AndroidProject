package com.example.RoomChef;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class EmailLoginActivity extends AppCompatActivity {

    TextView tv_dialog2;
    Button btn_login;
    EditText edit_email;
    EditText edit_pw;
    TextView tv_findId, tv_lMain;
    String email, pw, urlAddr;
    Intent intent;
    String returnpwd = "";
    private String centIP = RecipeData.CENIP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_login);

        btn_login = findViewById(R.id.btn_login);
        edit_email = findViewById(R.id.login_email);
        edit_pw = findViewById(R.id.login_pw);
        tv_lMain = findViewById(R.id.tv_lMain);
        tv_findId = findViewById(R.id.tv_findId);
        tv_dialog2 = findViewById(R.id.tv_dialog2);

        tv_findId.setOnClickListener(onClickListener);
        btn_login.setOnClickListener(onClickListener);
        tv_lMain.setOnClickListener(onClickListener);

    }
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_login:
                    email = edit_email.getText().toString(); // 이메일 받고
                    pw = edit_pw.getText().toString(); // 패스워드 받고
                    if (email.length() == 0 || pw.length() == 0) { // 빈칸이 있는지 확인
                        tv_dialog2.setText("이메일 혹은 비밀번호를 확인해주세요.");

                    }
                    else{ // 빈칸 없으면 이메일들고 패스워드 가질러
                        urlAddr = "";
                        urlAddr = "http://" + centIP + ":8080/test/login.jsp?"; // centIP 는 항상 위에
                        urlAddr = urlAddr + "email=" + email;
                        Log.v("오류",urlAddr);
                        connectionloginData();
                    }
                    break;
                case R.id.tv_findId: // 비밀번호 찾기 페이지로 이동
                    intent = new Intent(EmailLoginActivity.this, FindIdActivity.class);
                    startActivity(intent);
                    break;
                case R.id.tv_lMain: // 로고누르면 다시 첫페이지로 이동
                    intent = new Intent(EmailLoginActivity.this, LoginActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    };
    private void connectionloginData() {

        try {
            LoginNetworkTask loginNetworkTask = new LoginNetworkTask(EmailLoginActivity.this, urlAddr);
            returnpwd = loginNetworkTask.execute().get().toString();

            Log.v("패스워드",returnpwd);
            if (returnpwd.equals("null")){ // 디비에서 가져온 패스워드값이 null 이면 이메일이 없어요
                tv_dialog2.setText("이메일을 확인해주세요.");


            }else if(returnpwd.equals(pw)){ // 디비에서 가져온 비밀번호랑 입력한 비밀번호가 동일하면 로그인성공
                // 로그인 성공
                new AlertDialog.Builder(EmailLoginActivity.this)
                        .setTitle("로그인 성공!")
                        .setCancelable(false)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) { // 확인누르면 로그인한 이메일들고 메인페이지로(아직안만들어서 첫페이지)
                                intent = new Intent(EmailLoginActivity.this, MainActivity.class);
                                intent.putExtra("email", email);
                                RecipeData.USERID = email;
                                SharedPreference sharedPreference= new SharedPreference();
                                sharedPreference.setUserName(EmailLoginActivity.this, email);
                                startActivity(intent);
                            }
                        })
                        .show();
            }
            else { // 이메일과 비밀번호가 디비에 저장되어 있는거랑 다름
                Log.v("받아온거",pw);
                tv_dialog2.setText("비밀번호를 확인해주세요.");

            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }


}