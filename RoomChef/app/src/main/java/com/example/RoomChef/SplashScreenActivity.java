package com.example.RoomChef;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreenActivity extends Activity {

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            @Override
            public void run() {
                if(SharedPreference.getUserName(SplashScreenActivity.this).length() == 0) {
                    // call Login Activity
                    intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    // Call Next Activity
                    intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                    intent.putExtra("email", SharedPreference.getUserName(SplashScreenActivity.this).toString());
                    startActivity(intent);
                }
            }
        },3000); //3초 뒤에 Runner객체 실행하도록 함
    }
}

