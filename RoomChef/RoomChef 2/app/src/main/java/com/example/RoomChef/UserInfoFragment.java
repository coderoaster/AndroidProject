package com.example.RoomChef;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import java.io.InputStream;
import java.util.ArrayList;


public class UserInfoFragment extends Fragment {

    Button btn_userUpdate, btn_logout;
    TextView tv_email, tv_phone;
    ImageView img_userInfo;
    String nowPw, urlAddr, returnPw, phone, image;
    String centIP = RecipeData.CENIP;
    String email = RecipeData.USERID;
    ArrayList<UserInfo> beans;
    Intent intent;
    String imgurlAddr ="http://" + centIP + ":8080/test2/imgs/";


    public static UserInfoFragment newInstance() {
        return new UserInfoFragment();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.userinfo,container,false);

        btn_userUpdate = rootView.findViewById(R.id.btn_userUpdate);
        btn_logout = rootView.findViewById(R.id.btn_logout);
        tv_email = rootView.findViewById(R.id.tv_email);
        tv_phone = rootView.findViewById(R.id.tv_phone);
        img_userInfo = rootView.findViewById(R.id.img_userInfo);

        urlAddr = "http://" + centIP + ":8080/test/project_roomchefcall.jsp?";
        urlAddr = urlAddr + "email=" + email;
        Log.v("어디로가니", urlAddr);
        Log.v("이메일잘 저장되나", email);



        btn_userUpdate.setOnClickListener(onClickListener);
        btn_logout.setOnClickListener(onClickListener);
        return rootView;
    }

    @Override
    public void onResume() {
        connectGetData();
        super.onResume();
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                case R.id.btn_userUpdate:
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    final LayoutInflater inflater = getActivity().getLayoutInflater();
                    final View view1 = inflater.inflate(R.layout.pwcheck, null);
                    builder.setView(view1)
                            .setTitle("비밀번호를 입력해주세요")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    try {
                                        EditText ed_nowPw = view1.findViewById(R.id.ed_nowPw);
                                        nowPw = ed_nowPw.getText().toString().trim();
                                        urlAddr = "http://" + centIP + ":8080/test/project_roomchefcall.jsp?";
                                        urlAddr = urlAddr + "email=" + email;
                                        Log.v("어디로가니", urlAddr);
                                        Log.v("이메일잘 저장되나", email);
                                        connectGetData();
                                        returnPw = beans.get(0).getPassword();
                                        phone = beans.get(0).getPhone();
                                        image = beans.get(0).getImage();
                                        if (returnPw.equals(nowPw)) {
                                            intent = new Intent(getActivity(), UserInfoUpdate.class);
                                            intent.putExtra("phone", phone);
                                            intent.putExtra("nowPw", nowPw);
                                            intent.putExtra("image", image);
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(getActivity(), "비밀번호가 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
                                        }

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            })
                            .setNegativeButton("취소", null)
                            .show();
                    break;
                case R.id.btn_logout:
                    //카카오 로그아웃 처리 구현
                    UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                        @Override
                        public void onCompleteLogout() {
                            final Intent intent = new Intent(getContext(), LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    });
                    SharedPreference sharedPreference = new SharedPreference();
                    sharedPreference.clearUserName(getContext());
                    Intent intent = new Intent(getContext(),LoginActivity.class);
                    startActivity(intent);
                    break;

            }
        }
    };
    private void connectGetData() {
        try {
            UpdateCallNetworkTask networkTask = new UpdateCallNetworkTask(getContext(), urlAddr);
            Object obj = networkTask.execute().get();  //
            beans = (ArrayList<UserInfo>) obj;
            tv_email.setText(email);
            tv_phone.setText(beans.get(0).getPhone());
            image = beans.get(0).getImage();
            if(image.equals("null")){

            }else {
                imgurlAddr = imgurlAddr + beans.get(0).getImage();
                Glide.with(getContext()).load(imgurlAddr).into(img_userInfo);
            }
            Log.v("phone",beans.get(0).getPhone());
            Log.v("password",beans.get(0).getPassword());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}//---------------