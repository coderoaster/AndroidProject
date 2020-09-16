package com.example.semiproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {


    final static String TAG  = "status";
    String urlAddr;
    String sSeqno, sEmail, sPwd, centIP;
    EditText semail, spwd;
    Button ssignup,scancel;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        semail = findViewById(R.id.signup_email);
        spwd = findViewById(R.id.signup_pwd);
        ssignup = findViewById(R.id.btn_ssignup);
        scancel = findViewById(R.id.btn_scancel);


        semail.setFilters(new InputFilter[]{new InputFilter.LengthFilter(40)});
        spwd.setFilters(new InputFilter[]{new InputFilter.LengthFilter(30)});

        ssignup.setOnClickListener(onClickListener);
        scancel.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_ssignup:
                        sEmail = semail.getText().toString();
                        sPwd = spwd.getText().toString();
                        try {
                            if (sEmail.length() == 0 || sPwd.length() == 0) {
                                new AlertDialog.Builder(SignupActivity.this)
                                        .setTitle("알림")
                                        .setMessage("아이디 혹은 비밀번호를 확인해주세요.")
                                        .setIcon(R.mipmap.ic_launcher)
                                        .setCancelable(false)
                                        .setPositiveButton("확인", null)
                                        .show();
                            } else {
                                urlAddr = "";
                                intent = getIntent();
                                centIP = "192.168.0.148";
                                urlAddr = "http://" + centIP + ":8080/test/Membership.jsp?";

                                sEmail = semail.getText().toString().trim();
                                sPwd = spwd.getText().toString().trim();

                                urlAddr = urlAddr + "email=" + sEmail + "&pwd=" + sPwd;
                                Log.v(TAG, urlAddr);

                                connectSignupData();

                                new AlertDialog.Builder(SignupActivity.this)
                                        .setTitle("알림")
                                        .setMessage("회원가입이 완료되었습니다.")
                                        .setIcon(R.mipmap.ic_launcher)
                                        .setCancelable(false)
                                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                intent = new Intent(SignupActivity.this, MainActivity.class); // 화면을 넘기면서 데이터를 가져갈때 구동되는것
                                                intent.putExtra("email", sEmail);
                                                startActivity(intent);
                                            }
                                        })
                                        .show();
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                            Toast.makeText(SignupActivity.this, "SignUp Error", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.btn_scancel:
                        finish();
                        break;
                }
             }
        };

        private void connectSignupData(){
            try{
                InsertNetworkTask insertNetworkTask = new InsertNetworkTask(SignupActivity.this,urlAddr);
                insertNetworkTask.execute().get();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };
