package com.example.instructorhelpdome;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.instructorhelpdome.JavaBean.MyUser;
import com.fingerth.supdialogutils.SYSDiaLogUtils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FetchUserInfoListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class Edit_presonActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView im_head;
    private TextView username;
    private EditText byname;
    private EditText email;
    private EditText collage;
    private EditText major;
    private EditText tele;
    private EditText says;
    private Spinner sex;
    private EditText bnumber;
    private Button p_sbmit;
    private ImageButton back_btn;
    private Button button_changeimg;
    private  String imageUrl;



    //在消息队列中实现对控件的更改
    private Handler handle = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Bitmap bmp=(Bitmap)msg.obj;
                    im_head.setImageBitmap(bmp);
                    break;
            }
        };
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_preson);
        Bmob.initialize(this, "258d981ccdece06fee031bcc3ee685f3", "Bomb");
        initview();
    }

    private void queryImages() {
        BmobQuery<MyUser> query=new BmobQuery<MyUser>();
        query.findObjects(new FindListener<MyUser>() {
            @Override
            public void done(List<MyUser> list, BmobException e) {
                if(e == null){
                    MyUser user = list.get(0);
                    String imageurl=user.getImages().getUrl();
                    imageUrl=imageurl.replaceAll("http://bmob-cdn-25347.b0.upaiyun.com","http://jn.philuo.com");
                    Log.i("smile","图片url"+imageUrl);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Bitmap bmp = getURLimage(imageUrl);
                            Message msg = new Message();
                            msg.what = 0;
                            msg.obj = bmp;
                            handle.sendMessage(msg);
                        }
                    }).start();
                }else {

                }
            }
        });

    }

    private void initview() {
        MyUser userInfo = BmobUser.getCurrentUser(MyUser.class);
        String t_name=userInfo.getByname();
        byname=(EditText)findViewById(R.id.byname);
        byname.setText(t_name);

        username=(TextView) findViewById(R.id.username);
        String t_username=userInfo.getUsername();
        username.setText(t_username);

        String t_email=userInfo.getEmail();
        email=(EditText)findViewById(R.id.email);
        email.setText(t_email);

        String t_collage=userInfo.getCollege();
        collage=(EditText)findViewById(R.id.collage);
        collage.setText(t_collage);

        major=(EditText)findViewById(R.id.major);
        String t_major=userInfo.getMajor();
        major.setText(t_major);

        tele=(EditText)findViewById(R.id.tele);
        String t_tele=userInfo.getMobilePhoneNumber();
        tele.setText(t_tele);

        bnumber=(EditText)findViewById(R.id.dnumber);
        String t_bnum=userInfo.getDnumber();
        bnumber.setText(t_bnum);

        says =(EditText)findViewById(R.id.says);
        String t_says=userInfo.getSays();
        says.setText(t_says);


        sex=(Spinner)findViewById(R.id.sex);
        int sexCount=userInfo.getSex();
        sex.setSelection(sexCount,true);

        p_sbmit=(Button)findViewById(R.id.p_sub);
        p_sbmit.setOnClickListener(this);

        back_btn=(ImageButton)findViewById(R.id.back_btn);
        back_btn.setOnClickListener(this);

        button_changeimg=(Button)findViewById(R.id.button_changeimg);
        button_changeimg.setOnClickListener(this);

        im_head=(ImageView)findViewById(R.id.head_icon);
        queryImages();
        fetchUserInfo();

    }



    @Override
    public void onClick(View view) {
        //预备写进数据库，看项目进程吧
        switch (view.getId()) {
            case R.id.back_btn:
                queryImages();
                finish();
                break;
            case R.id.p_sub:
                edit_person();
                break;
            case R.id.button_changeimg:
                Intent intent1= new Intent(Edit_presonActivity.this,SelectphotoActivity.class);
                startActivity(intent1);
                break;

        }
    }


    private void edit_person() {
        MyUser newUser = new MyUser();
        String s_byname = byname.getText().toString();
        newUser.setByname(s_byname);
        queryImages();
        int sexx;
        String s_sex=sex.getSelectedItem().toString();
        if (s_sex.equals("男"))
        {
            sexx=0;
        }else {
            sexx=1;
        }
        newUser.setSex(sexx);

        String s_email = email.getText().toString();
        newUser.setEmail(s_email);

        String s_college = collage.getText().toString();
        newUser.setCollege(s_college);

        String s_major = major.getText().toString();
        newUser.setMajor(s_major);

        String s_tele = tele.getText().toString();
        newUser.setMobilePhoneNumber(s_tele);

        String s_dnum = bnumber.getText().toString();
        newUser.setDnumber(s_dnum);

        String s_says = says.getText().toString();
        newUser.setSays(s_says);


        MyUser bmobUser = MyUser.getCurrentUser(MyUser.class);
        newUser.update(bmobUser.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Log.i("smile", "用户更新成功");
                    SYSDiaLogUtils.showSuccessDialog(Edit_presonActivity.this, "个人信息修改成功", "恭喜你，操作成功了！", "OK", false);
                } else {
                    SYSDiaLogUtils.showErrorDialog(Edit_presonActivity.this, "失败警告", "很抱歉，这次更新失败，請检查网络重新試試！", "取消", false);
                    Log.i("smile", "用户更新失败");
                }
            }
        });
        fetchUserInfo();


    }

    private Bitmap getURLimage(String imageUrl) {
        Bitmap bmp = null;
        try {
            URL myurl = new URL(imageUrl);
            // 获得连接
            HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();
            conn.setConnectTimeout(6000);//设置超时
            conn.setDoInput(true);
            conn.setUseCaches(false);//不缓存
            conn.connect();
            InputStream is = conn.getInputStream();//获得图片的数据流
                    bmp = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bmp;
    }
    private void fetchUserInfo() {
        BmobUser.fetchUserJsonInfo(new FetchUserInfoListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e==null){
                    Log.i("smile","更新用户信息成功"+s);
                    queryImages();
                }else {
                    Log.i("smile","更新用户信息失败");
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        queryImages();
        handle.removeMessages(0);
    }


}
