package com.example.instructorhelpdome;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.instructorhelpdome.JavaBean.MyUser;

import androidx.appcompat.app.AppCompatActivity;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

public class SplashActivity extends AppCompatActivity {
    Handler mHandler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Bmob.initialize(this,"258d981ccdece06fee031bcc3ee685f3","Bomb");

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                BmobUser bmobUser = BmobUser.getCurrentUser(MyUser.class);
                if(bmobUser != null){
                    // 允许用户使用应用
                    intent.setClass(SplashActivity.this, MainActivity.class);
                }else{
                    //缓存用户对象为空时， 可打开用户注册界面…
                    intent.setClass(SplashActivity.this, LoginActivity.class);
                }
                startActivity(intent);
                finish();

            }
        }, 3000);
    }

}
