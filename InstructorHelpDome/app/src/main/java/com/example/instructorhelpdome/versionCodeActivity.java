package com.example.instructorhelpdome;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.instructorhelpdome.Dao.APKVersionCodeUtils;

import androidx.appcompat.app.AppCompatActivity;

public class versionCodeActivity extends AppCompatActivity implements View.OnClickListener {

    TextView version ;
    Handler  mTimeHandler;
    private ImageButton back_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_version_code);

        version =(TextView)findViewById(R.id.version);
        String versionCode = APKVersionCodeUtils.getVersionCodeUtil(this) + "";

        initView();

        //在类里声明一个Handler
        mTimeHandler = new Handler() {
            public void handleMessage(android.os.Message msg) {

                if (msg.what == 0) {

                    version.setText(versionCode);
                    sendEmptyMessageDelayed(0, 1000);
                }
            }


        };

        //在你的onCreate的类似的方法里面启动这个Handler就可以了：
        mTimeHandler.sendEmptyMessageDelayed(0, 1000);

    }

    private void initView() {
        back_btn=(ImageButton)findViewById(R.id.back_btn);
        back_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
        }

    }
}
