package com.example.instructorhelpdome;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.instructorhelpdome.JavaBean.MyUser;
import com.fingerth.supdialogutils.SYSDiaLogUtils;

import androidx.appcompat.app.AppCompatActivity;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText mAccount;                        //用户名编辑
    private EditText mPwd;                            //密码编辑
    private EditText mPwdCheck;                       //密码编辑
    private Button mSureButton;                       //确定按钮
    private Button mCancelButton;                     //取消按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
       
        Bmob.initialize(this,"258d981ccdece06fee031bcc3ee685f3","Bomb"); 
        initview();
    }

    private void initview() {
        mAccount = (EditText) findViewById(R.id.resetpwd_edit_name);
        mPwd = (EditText) findViewById(R.id.resetpwd_edit_pwd_old);
        mPwdCheck = (EditText) findViewById(R.id.resetpwd_edit_pwd_new);

        mSureButton = (Button) findViewById(R.id.register_btn_sure);
        mCancelButton = (Button) findViewById(R.id.register_btn_cancel);

        mSureButton.setOnClickListener(this);      //注册界面两个按钮的监听事件
        mCancelButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register_btn_sure:                       //确认按钮的监听事件
                userRegister();
                break;
            case R.id.register_btn_cancel:                     //取消按钮的监听事件,由注册界面返回登录界面
                Intent intent_Register_to_Login = new Intent(RegisterActivity.this,LoginActivity.class) ;
                startActivity(intent_Register_to_Login);
                finish();
                break;
        }
    }

    private void userRegister() {
        if (isUserNameAndPwdValid()) {
            String userName = mAccount.getText().toString();
            String userPwd = mPwd.getText().toString();
            String userPwdCheck = mPwdCheck.getText().toString();
            if (userPwd.equals(userPwdCheck) == false) {     //两次密码输入不一样
                Toast.makeText(this, getString(R.string.pwd_not_the_same), Toast.LENGTH_SHORT).show();
                return;
            } else {

                MyUser myUser = new MyUser();
                myUser.setUsername(userName);
                myUser.setPassword(userPwd);
                myUser.signUp(new SaveListener<MyUser>() {
                    @Override
                    public void done(MyUser myUser, BmobException e) {
                        if (e == null) {
                            Toast.makeText(RegisterActivity.this, getString(R.string.register_success), Toast.LENGTH_SHORT).show();
                            Intent intent_Register_to_Login = new Intent(RegisterActivity.this, LoginActivity.class);    //切换User Activity至Login Activity
                            startActivity(intent_Register_to_Login);
                            finish();
                            Log.i("smile", "用户注册成功");
                        } else {
                            SYSDiaLogUtils.showErrorDialog(RegisterActivity.this, "失败警告", "很抱歉，这次注册失败，用户名已经注册或請检查网络重新試試！", "取消", false);
                            Log.i("smile", "用户注册失败");
                        }
                    }
                });


            }
        }

    }








    private boolean isUserNameAndPwdValid() {
        if (mAccount.getText().toString().trim().equals("")) {
            Toast.makeText(this, getString(R.string.account_empty),
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (mPwd.getText().toString().trim().equals("")) {
            Toast.makeText(this, getString(R.string.pwd_empty),
                    Toast.LENGTH_SHORT).show();
            return false;
        }else if(mPwdCheck.getText().toString().trim().equals("")) {
            Toast.makeText(this, getString(R.string.pwd_check_empty),
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


}
