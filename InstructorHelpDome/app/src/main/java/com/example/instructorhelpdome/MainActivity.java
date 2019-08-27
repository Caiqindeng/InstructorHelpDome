package com.example.instructorhelpdome;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
}
