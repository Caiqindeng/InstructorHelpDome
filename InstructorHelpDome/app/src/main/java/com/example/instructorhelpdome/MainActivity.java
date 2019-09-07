package com.example.instructorhelpdome;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationManagerCompat;
import cn.bmob.v3.Bmob;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,RadioGroup.OnCheckedChangeListener{
    RadioGroup rg_tab_bar;
    private RadioButton rb_home;

    private HomeFragment fg1;
    private ServiceFragment fg2;
    private PresonFragment fg3;
    private ChatFragment fg4;
    private FragmentManager fManager;
    private Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bmob.initialize(this,"258d981ccdece06fee031bcc3ee685f3","Bomb");
        init();

        notification();
    }


    private void init() {
        rg_tab_bar = (RadioGroup) findViewById(R.id.rg_tab_bar);
        fManager = getFragmentManager();
        rg_tab_bar.setOnCheckedChangeListener(this);
        rb_home=(RadioButton)findViewById(R.id.rb_Home);
        rb_home.setChecked(true);

        mToolbar=findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);//利用Toolbar代替ActionBar

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"点击导航栏",Toast.LENGTH_SHORT).show();
            }
        });

        mToolbar.inflateMenu(R.menu.toolbar_menu);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_settings:
                        Toast.makeText(MainActivity.this,"action_settings",Toast.LENGTH_SHORT).show();

                        break;
                    case R.id.action_share:
                        Toast.makeText(MainActivity.this,"action_share",Toast.LENGTH_SHORT).show();

                        break;
                    case R.id.action_search:
                        Toast.makeText(MainActivity.this,"action_search",Toast.LENGTH_SHORT).show();

                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        return true;
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        FragmentTransaction fragmentTransaction=fManager.beginTransaction();
        hideAllFragment(fragmentTransaction);
        switch (i){
            case R.id.rb_Home:

                if (fg1==null){
                    fg1=new HomeFragment();
                    fragmentTransaction.add(R.id.ly_content,fg1);
                }else {
                    fragmentTransaction.show(fg1);
                }
                break;
            case R.id.rb_Service:

                if (fg2==null){
                    fg2=new ServiceFragment();
                    fragmentTransaction.add(R.id.ly_content,fg2);
                }else {
                    fragmentTransaction.show(fg2);
                }
                break;
            case R.id.rb_Preson:

                if (fg3==null){
                    fg3=new PresonFragment();
                    fragmentTransaction.add(R.id.ly_content,fg3);
                }else {
                    fragmentTransaction.show(fg3);
                }
                break;
            case R.id.rb_Chat:

                if (fg4==null){

                    fg4=new ChatFragment();
                    fragmentTransaction.add(R.id.ly_content,fg4);
                }else {
                    fragmentTransaction.show(fg4);
                }
                break;
        }
        fragmentTransaction.commit();
    }

    private void hideAllFragment(FragmentTransaction fragmentTransaction) {
        if (fg1 != null) fragmentTransaction.hide(fg1);
        if (fg2 != null) fragmentTransaction.hide(fg2);
        if (fg3 != null) fragmentTransaction.hide(fg3);
        if (fg4 != null) fragmentTransaction.hide(fg4);
    }



    private void notification() {

        NotificationManagerCompat notification = NotificationManagerCompat.from(this);
        boolean isEnabled = notification.areNotificationsEnabled();

        if (!isEnabled) {

            //未打开通知
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("请在“通知”中打开通知权限")
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            Intent intent = new Intent();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                                intent.putExtra("android.provider.extra.APP_PACKAGE", MainActivity.this.getPackageName());

                            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {  //5.0
                                intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                                intent.putExtra("app_package", MainActivity.this.getPackageName());
                                intent.putExtra("app_uid", MainActivity.this.getApplicationInfo().uid);
                                startActivity(intent);

                            } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {  //4.4
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.addCategory(Intent.CATEGORY_DEFAULT);
                                intent.setData(Uri.parse("package:" + MainActivity.this.getPackageName()));

                            } else if (Build.VERSION.SDK_INT >= 15) {
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                                intent.setData(Uri.fromParts("package", MainActivity.this.getPackageName(), null));
                            }
                            startActivity(intent);
                        }
                    })
                    .create();
            alertDialog.show();
            alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);

        }
    }


    /**
     * 设置在onStart()方法里面，可以在界面每次获得焦点的时候都进行检测
     */
    @Override
    protected void onStart() {
        if (!isNetworkConnected(this)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("开启网络服务");
            builder.setMessage("网络没有连接，请到设置进行网络设置！");
            builder.setPositiveButton("确定",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if (android.os.Build.VERSION.SDK_INT > 10) {
                                // 3.0以上打开设置界面，也可以直接用ACTION_WIRELESS_SETTINGS打开到wifi界面
                                startActivity(new Intent(
                                        android.provider.Settings.ACTION_SETTINGS));
                            } else {
                                startActivity(new Intent(
                                        android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                            }
                            dialog.cancel();
                        }
                    });

            builder.setNegativeButton("取消",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            builder.show();
        }
        super.onStart();
    }

    public static boolean isNetworkConnected(Context context){
        ConnectivityManager manager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager.getActiveNetworkInfo()!=null){
            return manager.getActiveNetworkInfo().isAvailable();
        }
        return false;
    }

}
