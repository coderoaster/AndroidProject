package com.example.semiproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText editIP;

    Button login;
    Button signup;
    EditText email, pwd;
    String loginpart, centIP, Lemail,Lpwd;
    String urlAddr;
    Intent intent;
    String returnpwd = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addListener();
    }
    private void addListener(){


        login =findViewById(R.id.btn_login);
        signup = findViewById(R.id.btn_signup);

        email = findViewById(R.id.et_email);
        pwd = findViewById(R.id.et_pwd);

        signup.setOnClickListener(onClickListener);
        login.setOnClickListener(onClickListener);

    }



    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            centIP = "192.168.0.148";
//            Intent intent = null;


            switch (v.getId()){
                case R.id.btn_login:
                    Lemail = email.getText().toString();
                    Lpwd = pwd.getText().toString();
                    if (Lemail.length() == 0 || Lpwd.length() == 0) {
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("알립니다.")
                                .setMessage("아이디 혹은 비밀번호를 확인해주세요.")
                                .setIcon(R.mipmap.ic_launcher)
                                .setCancelable(false)
                                .setPositiveButton("확인", null)
                                .show();
                    }
                    else{
                        urlAddr = "";
                        urlAddr = "http://" + centIP + ":8080/test/login.jsp?";
                        urlAddr = urlAddr + "email=" + Lemail;
                        Log.v("오류",urlAddr);
                        connectionloginData();
                    }
                    break;
                case R.id.btn_signup:
                    intent = new Intent(MainActivity.this, SignupActivity.class);
                    intent.putExtra("centIP", centIP);
                    startActivity(intent);
                    break;
            }




//                                name = edit_name.getText().toString();
//                                phone = edit_phone.getText().toString();
//                                address = edit_address.getText().toString();
//                                relation = sp_relation.getSelectedItem().toString();
//
//
//                                try{
//                                    centIP = "192.168.0.83";
//                                    urlAddr = "http://" + centIP + ":8080/Test/project_update.jsp?";
//                                    urlAddr = urlAddr + "name=" + name + "&phone=" + phone + "&address=" + address + "&relation=" + relation + "&seq=" + seq;
//
//                                    connectUpdateData();
//                                    Toast.makeText(UpdateDeleteActivity.this, "Update OK!", Toast.LENGTH_SHORT).show();
//
////                                intent = new Intent(UpdateDeleteActivity.this, ListActivity.class);
////                                intent.putExtra("centIP",centIP);
//                                    startActivity(intent);
//
//                                } catch (Exception e ){
//                                    e.printStackTrace();
//                                    Toast.makeText(MainActivity.this, "Login Error", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        })
//                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                Toast.makeText(MainActivity.this, "취소하였습니다.", Toast.LENGTH_SHORT).show();
//                            }
//                        })




        }

        private void connectionloginData() {

            try {
                LoginNetworkTask loginNetworkTask = new LoginNetworkTask(MainActivity.this, urlAddr);
                returnpwd = loginNetworkTask.execute().get().toString();

                Log.v("패스워드",returnpwd);
                if (returnpwd.equals("null")){
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("아이디를 확인해 주세요.")
                            .setCancelable(false)
                            .setPositiveButton("확인", null)
                            .show();
                }else if(returnpwd.equals(Lpwd)){
                // 로그인 성공
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("로그인 성공!")
                        .setCancelable(false)
                        .setPositiveButton("확인", null)
                        .show();
                intent = new Intent(MainActivity.this, SelectAllActivity.class);
                intent.putExtra("centIP", centIP);
                intent.putExtra("email", Lemail);
                startActivity(intent);
            }
                else {
                        Log.v("받아온거",Lpwd);
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("비밀번호를 확인해 주세요.")
                                .setCancelable(false)
                                .setPositiveButton("확인", null)
                                .show();
                    }

            }catch (Exception e){
                e.printStackTrace();
            }
        }



    };
}