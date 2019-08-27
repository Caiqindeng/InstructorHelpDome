package com.example.instructorhelpdome;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.instructorhelpdome.JavaBean.MyUser;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FetchUserInfoListener;
import cn.bmob.v3.listener.FindListener;


public class PresonFragment extends Fragment implements View.OnClickListener {

    private TextView t_name;
    private TextView t_number;
    private Button btn_quit;
    private LinearLayout preson_data;
    Handler  mTimeHandler;

    private  String imageUrl;
    private ImageView img;

    public PresonFragment() {
    }
    //在消息队列中实现对控件的更改
    private Handler handle = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Bitmap bmp=(Bitmap)msg.obj;
                    img.setImageBitmap(bmp);
                    break;
            }
        };
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_preson,container,false);
        Bmob.initialize(getActivity(),"258d981ccdece06fee031bcc3ee685f3","Bomb");
        initView(view);
        fetchUserInfo();
        return view;
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


    private void initView(View view) {
        t_name=(TextView)view.findViewById(R.id.t_name);
        t_number=(TextView)view.findViewById(R.id.t_number);

        btn_quit=(Button)view.findViewById(R.id.btn_quit);
        btn_quit.setOnClickListener(this);
        
        preson_data=(LinearLayout)view.findViewById(R.id.preson_data);
        preson_data.setOnClickListener(this);

        img=(ImageView)view.findViewById(R.id.images_head);

        fetchUserInfo();
        queryImages();

        //在类里声明一个Handler
        mTimeHandler = new Handler() {
            public void handleMessage(android.os.Message msg) {

                if (msg.what == 0) {
                    MyUser userInfo = BmobUser.getCurrentUser(MyUser.class);
                    String s_name=userInfo.getByname();
                    String s_number=userInfo.getUsername();
                    t_name.setText(s_name);
                    t_number.setText(s_number);
                    sendEmptyMessageDelayed(0, 1000);
                }
            }


        };

        //在你的onCreate的类似的方法里面启动这个Handler就可以了：
        mTimeHandler.sendEmptyMessageDelayed(0, 1000);
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





    private void fetchUserInfo() {
        queryImages();
        BmobUser.fetchUserJsonInfo(new FetchUserInfoListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e==null){
                    Log.i("smile","更新用户信息成功"+s);
                }else {
                    Log.i("smile","更新用户信息失败");
                }
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mTimeHandler.removeCallbacksAndMessages(null);
        mTimeHandler.removeMessages(0);
        handle.removeMessages(0);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_quit:
                //退出登陆
                mTimeHandler.removeCallbacksAndMessages(null);
                mTimeHandler.removeMessages(0);
                handle.removeMessages(0);
                BmobUser.logOut();
                BmobUser currentUser = BmobUser.getCurrentUser(MyUser.class); // 现在的currentUser是null了
                startActivity(new Intent(getActivity(),LoginActivity.class));
                break;
            case R.id.preson_data:
                startActivity(new Intent(getActivity(),Edit_presonActivity.class));
                break;
        }
        
    }
}
