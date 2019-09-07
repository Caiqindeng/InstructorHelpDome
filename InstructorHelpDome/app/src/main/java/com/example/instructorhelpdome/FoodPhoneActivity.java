package com.example.instructorhelpdome;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.instructorhelpdome.JavaBean.Foodphone;
import com.example.instructorhelpdome.adapter.FoodTeleAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


public class FoodPhoneActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView listView;
    private List<Foodphone> datas = new ArrayList<>();
    private FoodTeleAdapter mFoodTeleAdapter;
    private ImageButton back_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_phone);

        listView=(ListView)findViewById(R.id.food_tele);
        back_btn=(ImageButton)findViewById(R.id.back_btn);
        back_btn.setOnClickListener(this);


        BmobQuery<Foodphone> bmobQuery = new BmobQuery<>();
        bmobQuery.findObjects(new FindListener<Foodphone>() {
            @Override
            public void done(List<Foodphone> object, BmobException e) {
                if (e == null) {
                    Log.i("log","查询成功"+object.size());
                    datas=object;
                    mFoodTeleAdapter=new FoodTeleAdapter(datas,FoodPhoneActivity.this);
                    listView.setAdapter(mFoodTeleAdapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            Foodphone foodphone=datas.get(i);
                            String Sshoptele=foodphone.getShopphone();
                            Log.i("msg",Sshoptele);
                            toTelephone(Sshoptele);

                        }
                    });


                } else {
                    Log.i("log","查询失败"+e.getMessage());
                    Toast.makeText(FoodPhoneActivity.this, "请检查网络是否连通", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
        }
    }
    private void toTelephone(String phoneNum) {

        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNum));
        startActivity(intent);
    }



}
