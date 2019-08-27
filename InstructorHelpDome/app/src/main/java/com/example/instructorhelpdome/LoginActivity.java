package com.example.instructorhelpdome;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.instructorhelpdome.JavaBean.MyUser;

import androidx.appcompat.app.AppCompatActivity;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText mAccount;                        //用户名编辑
    private EditText mPwd;                            //密码编辑
    private Button mRegisterButton;                   //注册按钮
    private Button mLoginButton;                      //登录按钮
    private CheckBox mRememberCheck;
    private SharedPreferences login_sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Bmob.initialize(this,"258d981ccdece06fee031bcc3ee685f3","Bomb");
        initView();
    }

    private void initView() {
        //通过id找到相应的控件
        mAccount = (EditText) findViewById(R.id.login_edit_account);
        mPwd = (EditText) findViewById(R.id.login_edit_pwd);
        mRegisterButton = (Button) findViewById(R.id.login_btn_register);
        mLoginButton = (Button) findViewById(R.id.login_btn_login);

        mRegisterButton.setOnClickListener(this);                      //采用OnClickListener方法设置不同按钮按下之后的监听事件
        mLoginButton.setOnClickListener(this);
        mRememberCheck = (CheckBox) findViewById(R.id.Login_Remember);


        login_sp = getSharedPreferences("userInfo", 0);
        String name=login_sp.getString("USER_NAME", "");
        String pwd =login_sp.getString("PASSWORD", "");
        boolean choseRemember =login_sp.getBoolean("mRememberCheck", false);

        //如果上次选了记住密码，那进入登录页面也自动勾选记住密码，并填上用户名和密码
        if(choseRemember){
            mAccount.setText(name);
            mPwd.setText(pwd);
            mRememberCheck.setChecked(true);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_btn_register:                            //登录界面的注册按钮
                Intent intent_Login_to_Register = new Intent(LoginActivity.this, RegisterActivity.class);    //切换Login Activity至RegisterActivity
                startActivity(intent_Login_to_Register);
                break;
            case R.id.login_btn_login:                              //登录界面的登录按钮
                login();
                break;

        }
    }

    private void login() {
        final String uName=mAccount.getText().toString();
        final String uPwd=mPwd.getText().toString();

        MyUser myUser=new MyUser();
        myUser.setUsername(uName);
        myUser.setPassword(uPwd);
        final SharedPreferences.Editor editor =login_sp.edit();
        myUser.login(new SaveListener<MyUser>() {

            @Override
            public void done(MyUser myUser, BmobException e) {
                if(e==null){
                    //保存用户名和密码
                    editor.putString("USER_NAME", uName);
                    editor.putString("PASSWORD", uPwd);

                    //是否记住密码
                    if(mRememberCheck.isChecked()){
                        editor.putBoolean("mRememberCheck", true);
                    }else{
                        editor.putBoolean("mRememberCheck", false);
                    }
                    editor.commit();

                    Toast.makeText(LoginActivity.this, getString(R.string.login_success),Toast.LENGTH_SHORT).show();//登录成功提示
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class) ;    //切换Login Activity至Main Activity
                    startActivity(intent);
                    Log.i("smile","用户登陆成功");
                    finish();
                }else {
                    Toast.makeText(LoginActivity.this, getString(R.string.login_fail),Toast.LENGTH_SHORT).show();  //登录失败提示
                }
            }
        });
    }
}
