package com.example.instructorhelpdome.adapter;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.instructorhelpdome.R;
import com.example.instructorhelpdome.WebviewActivity;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import cn.bmob.v3.BmobArticle;



public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>  {

    private LayoutInflater mInflater;
    private List<BmobArticle> mLists;
    private OnItemClickListener mListener;

    public void setLists(List<BmobArticle> lists) {
        mLists = lists;
    }

    public interface OnItemClickListener {
        void onClick(View v, int position);

    }
    public MyAdapter(OnItemClickListener listener) {
        mListener = listener;
    }

    public OnItemClickListener getListener() {
        return mListener;
    }

    public void setListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public List<BmobArticle> getLists() {
        return mLists;
    }

    public void addLists(List<BmobArticle> lists) {
        addLists(0, lists);
    }

    public void addLists(int position, List<BmobArticle> lists) {

        if (lists != null && lists.size() > 0) {
            mLists.addAll(lists);
            notifyItemRangeChanged(position, mLists.size());
        }
    }
    public void removeData(List<BmobArticle> lists,int position) {
        lists.remove(position);
        notifyItemRemoved(position);
    }


    public MyAdapter(List<BmobArticle> items) {
        mLists = items;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mInflater = LayoutInflater.from(parent.getContext());
        View view = mInflater.inflate(R.layout.item_tz, null);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });
        return new MyViewHolder(view);

    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView item_title;
        private TextView item_desc;
        private ImageView item_im;
        private TextView item_time;
        private CardView mCardView;
        public MyViewHolder(View itemView) {
            super(itemView);
            item_title = (TextView) itemView.findViewById(R.id.item_title);
            item_desc = (TextView) itemView.findViewById(R.id.item_desc);
            item_im = (ImageView) itemView.findViewById(R.id.item_im);
            item_time =(TextView) itemView.findViewById(R.id.item_time);
            mCardView=(CardView)itemView.findViewById(R.id.cardview);
        }
    }



    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
      //  holder.item_title.setText(mLists.get(position));
        BmobArticle mBmobArticle=mLists.get(position);
        String s_title=mBmobArticle.getTitle();
        String s_desc=mBmobArticle.getDesc();
        String s_time=mBmobArticle.getUpdatedAt();
        String imageurl=mBmobArticle.getCover();

        //在消息队列中实现对控件的更改
        Handler handle = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        Bitmap bmp=(Bitmap)msg.obj;
                        holder.item_im.setImageBitmap(bmp);
                        break;
                }
            };
        };
       String imageUrl=imageurl.replaceAll("http://bmob-cdn-25347.b0.upaiyun.com","http://jn.philuo.com");
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
        holder.item_title.setText(s_title);
        holder.item_desc.setText(s_desc);
        holder.item_time.setText(s_time);

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = holder.getAdapterPosition();
                    BmobArticle mBmobArticle=mLists.get(position);
                    String O_title=mBmobArticle.getTitle();
                    String O_url=mBmobArticle.getUrl();
                    String O_Url=O_url.replaceAll("http://bmob-cdn-25347.b0.upaiyun.com","http://jn.philuo.com");
                    Log.i("msg", O_title);
                    Log.i("msg", O_Url);
                    Intent intent = new Intent(view.getContext(), WebviewActivity.class);
                    Bundle B = new Bundle();
                    B.putString("name", O_title);
                    B.putString("url", O_Url);
                    intent.putExtra("data", B);
                    view.getContext().startActivity(intent);

                    mListener.onClick(holder.itemView, position);
                }
        });

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


    @Override
    public int getItemCount() {
        return mLists.size();
    }

}