package com.example.RoomChef;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class SignUpActivity extends AppCompatActivity {

    final static String TAG = "Check";
    TextView tv_sMain, tv_dialog;
    Button btn_signUp,btn_email;
    EditText edit_email, edit_pw, edit_pwChk, edit_phone;
    CheckBox chb_chk;
    Intent intent;
    String email, pw, pw_chk, phone, urlAddr,num;
    private String centIP = RecipeData.CENIP;
    int count = 0;
    Random rnd;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        tv_sMain = findViewById(R.id.tv_sMain);
        btn_signUp = findViewById(R.id.btn_signUp);
        btn_email= findViewById(R.id.signUp_email_btn);
        edit_email = findViewById(R.id.signUp_email);
        edit_pw = findViewById(R.id.signUp_pw);
        edit_pwChk = findViewById(R.id.signUp_pwChk);
        edit_phone = findViewById(R.id.signUp_phone);
        chb_chk = findViewById(R.id.chb_chk);
        tv_dialog = findViewById(R.id.tv_dialog);


        edit_pw.setEnabled(false);
        edit_pwChk.setEnabled(false);
        edit_phone.setEnabled(false);

        //인터넷 사용권한 허가
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());

        edit_email.setFilters(new InputFilter[]{new InputFilter.LengthFilter(30)});
        edit_pw.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)});
        edit_phone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});

        tv_sMain.setOnClickListener(onClickListener);
        btn_signUp.setOnClickListener(onClickListener);
        chb_chk.setOnClickListener(onClickListener);
        btn_email.setOnClickListener(onClickListener);



    }
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_signUp:
                    email = edit_email.getText().toString().trim();
                    pw = edit_pw.getText().toString().trim();
                    pw_chk = edit_pwChk.getText().toString().trim();
                    phone = edit_phone.getText().toString().trim();
                    try {
                        if (editChk() == 1) { // 빈칸 있는지 확인하는 메소드
                            tv_dialog.setText("회원정보를 정확히 입력해주세요."); // 밑에 알림띄우는애

//                            new AlertDialog.Builder(SignUpActivity.this)
//                                    .setTitle("알림")
//                                    .setMessage("이메일 혹은 비밀번호를 확인해주세요.")
//                                    .setIcon(R.mipmap.ic_launcher)
//                                    .setCancelable(false)
//                                    .setPositiveButton("확인", null)
//                                    .show();
                            break;

                        }else if(passChk() == 1){ // 비밀번호와 비밀번호 확인칸 동일한지 확인하는 메소드
                            tv_dialog.setText("비밀번호가 일치하지 않습니다.");
                            break;
                        }else if(chbChk() == 1) { // 개인정보 취급방침 체크 확인하는 메소드
                            tv_dialog.setText("개인정보 취급방침에 동의해주세요.");
                            break;
                        }else if(doubleChk() == 1) { // 이메일이 중복되는지 확인하는 메소드
                            tv_dialog.setText("중복된 이메일이 존재합니다.");
                            break;
                        }else {
                            urlAddr = "";
                            urlAddr = "http://" + centIP + ":8080/test/signUp.jsp?"; // centIP 는 위에 지정되어있음 해당아이피로 바꿔야함.

                            urlAddr = urlAddr + "email=" + email + "&pw=" + pw + "&phone=" + phone; // email, pw 들고 jsp로 슝
                            Log.v(TAG, urlAddr);

                            connectSignupData();

                            // 문제없이 회원가입 완료되었을때
                            new AlertDialog.Builder(SignUpActivity.this)
                                    .setTitle("알림")
                                    .setMessage("회원가입이 완료되었습니다.")
                                    .setIcon(R.mipmap.ic_launcher)
                                    .setCancelable(false)
                                    .setPositiveButton("확인", new DialogInterface.OnClickListener() { // 확인누르면 첫페이지로 로그인하러
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                        }
                                    })
                                    .show();

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(SignUpActivity.this, "SignUp Error", Toast.LENGTH_SHORT).show();
                    }
                    break;

                case R.id.tv_sMain: // 로고를 누르면 첫페이지로
                    intent = new Intent(SignUpActivity.this, LoginActivity.class);
                    startActivity(intent);
                    break;
                case R.id.signUp_email_btn:
                    rnd = new Random();
                    num= String.valueOf(rnd.nextInt(1000));
                    SendMail mailServer = new SendMail();
                    mailServer.sendSecurityCode(SignUpActivity.this,"인증번호입니다.\n"+num ,edit_email.getText().toString());
                    final LinearLayout linear = (LinearLayout) View.inflate(SignUpActivity.this, R.layout.jhj_custom_dialog, null);
                    new AlertDialog.Builder(SignUpActivity.this)
                            .setTitle("이메일 본인인증")
                            .setView(linear)
                            .setIcon(R.mipmap.ic_launcher)
                            .setCancelable(false)
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() { // 확인누르면 첫페이지로 로그인하러
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    EditText editText = linear.findViewById(R.id.Custom_btn_email);
                                    if (num.equals(editText.getText().toString())){
                                        Toast.makeText(SignUpActivity.this,"인증완료",Toast.LENGTH_SHORT).show();
                                        edit_email.setEnabled(false);
                                        edit_pw.setEnabled(true);
                                        edit_pwChk.setEnabled(true);
                                        edit_phone.setEnabled(true);
                                        tv_dialog.setText("이메일 인증완료.");
                                    }else {
                                        Toast.makeText(SignUpActivity.this,"인증실패",Toast.LENGTH_SHORT).show();
                                        tv_dialog.setText("이메일 인증실패.");
                                     }
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Toast.makeText(SignUpActivity.this,"인증취소",Toast.LENGTH_SHORT).show();
                                    dialogInterface.dismiss();
                                }
                            })
                            .show();
                    break;

            }
        }

    };

        private void connectSignupData() { // 회원가입용
            try {
                InsNetworkTask insertNetworkTask = new InsNetworkTask(SignUpActivity.this, urlAddr);
                insertNetworkTask.execute().get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        private void connectDoubleChkData() { // 이메일 중복체크용
            try {
                DoubleChkNetworkTask doubleChkNetworkTask = new DoubleChkNetworkTask(SignUpActivity.this, urlAddr);
                count = Integer.parseInt(doubleChkNetworkTask.execute().get().toString()); // Object 값을 String 으로 변환 후 Integer 로 또 변환
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 빈칸체크!!!!!
        private int editChk() {

            int result;
            if (email.length() == 0 || pw.length() < 8 || pw_chk.length() < 8 || phone.length() < 10 ) {
               result = 1;
            } else {
                result = 0;
            }return result;
        }
        // 패스워드랑 확인칸 동일한지 확인!!!!!
        private int passChk(){
            int result;

            if(pw.equals(pw_chk)){
                result = 0;
            }else{
                result = 1;
            }return result;
        }
        // 개인정보취급 동의 체크 확인!!!!!
        private int chbChk(){
            int result;
            if (chb_chk.isChecked()) {
                result = 0;
            } else {
                result = 1;
            }return result;
        }
        // 이메일 중복확인!!!!
        private int doubleChk(){
            urlAddr = "";
            urlAddr = "http://" + centIP + ":8080/test/doubleChk.jsp?"; // centIP 위에 지정되어 있음.
            email = edit_email.getText().toString().trim();

            urlAddr = urlAddr + "email=" + email;
            Log.v(TAG, urlAddr);

            connectDoubleChkData();
            return count; // 이메일이 존재하면 1 없으면 0
        }

} //--------------------

