package com.example.instructorhelpdome;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.instructorhelpdome.JavaBean.LostInfomationReq;
import com.example.instructorhelpdome.JavaBean.MyUser;

import androidx.appcompat.app.AppCompatActivity;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class Lost_fondActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView lfname;
    private TextView lfid;
    private TextView lfcollage;
    private ImageView lf_submit;
    private ImageView back_btn;
    private EditText title;
    private EditText phoneNum;
    private EditText desc;
    private LostInfomationReq infomationReq;
    private boolean isChangeInfos;
    MyUser userInfo = BmobUser.getCurrentUser(MyUser.class);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_fond);

        Bmob.initialize(this,"258d981ccdece06fee031bcc3ee685f3","Bomb");
        initview();
        initdate();
    }

    private void initview() {
        lfname=(TextView)findViewById(R.id.lf_name);
        lfid=(TextView)findViewById(R.id.lf_id);
        lfcollage=(TextView)findViewById(R.id.lf_collage);

        title = (EditText) findViewById(R.id.et_title);
        phoneNum = (EditText) findViewById(R.id.et_phone_num);
        desc = (EditText) findViewById(R.id.et_desc);

    }
    private void initdate() {
        String lf_name=userInfo.getByname();
        String lf_id=userInfo.getUsername();
        String lf_collage=userInfo.getCollege();
        lfname.setText(lf_name);
        lfid.setText(lf_id);
        lfcollage.setText(lf_collage);

        lf_submit=(ImageView) findViewById(R.id.iv_add);
        lf_submit.setOnClickListener(this);
        back_btn=(ImageView) findViewById(R.id.iv_back);
        back_btn.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_add:
                addData();
                break;
        }
    }

    private void addData() {
        String titleName = title.getText().toString().trim();
        String Num = phoneNum.getText().toString().trim();
        String descridle = desc.getText().toString().trim();

        if (TextUtils.isEmpty(titleName)) {
            showToast("标题不能为空");
            return;
        }

        if (TextUtils.isEmpty(Num)) {
            showToast("手机号码不能为空");
            return;
        }

        if (TextUtils.isEmpty(descridle)) {
            showToast("描述不能为空");
            return;
        }

        //判断是发表新的信息还是更改信息
        if (isChangeInfos) {
            updataInfo(titleName, Num, descridle);
        } else {
            publishLostInfo(titleName, Num, descridle);
        }
    }
    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void updataInfo(String titleName, String num, String descridle) {
        LostInfomationReq lostInfomationReq = new LostInfomationReq();
        lostInfomationReq.setTitle(titleName);//titleName为用户输入的标题
        lostInfomationReq.setPhoneNum(num);//num为用户输入的号码
        lostInfomationReq.setDesc(descridle);//descridle为信息描述
        lostInfomationReq.update(infomationReq.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    showToast("更新信息成功");
                    //更新数据后提示主界面进行数据刷新
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    private void publishLostInfo(String titleName, String num, String descridle) {
        LostInfomationReq lostInfomationReq = new LostInfomationReq();
        String lf_name=userInfo.getUsername();
        lostInfomationReq.setUsername(lf_name);
        lostInfomationReq.setTitle(titleName);//titleName为用户输入的标题
        lostInfomationReq.setPhoneNum(num);//num为用户输入的号码
        lostInfomationReq.setDesc(descridle);//descridle为信息描述
        lostInfomationReq.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    showToast("招领信息发布成功");

                    //成功后提示主界面刷新数据
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    //成功后将页面销毁
                    finish();
                } else {
                    showToast("信息发布失败");
                }
            }
        });
    }

}
