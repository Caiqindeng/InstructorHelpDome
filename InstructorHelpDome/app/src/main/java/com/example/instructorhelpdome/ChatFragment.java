package com.example.instructorhelpdome;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.example.instructorhelpdome.adapter.MyAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.bmob.v3.BmobArticle;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


public class ChatFragment extends Fragment  implements View.OnClickListener{
    private MaterialRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;

    private List<BmobArticle> datas = new ArrayList<>();
    private List<BmobArticle> mList = new ArrayList<>();
    private MyAdapter mAdapter;

    private boolean isLoadMore = true;

    public ChatFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        initView(view);
        initDatas();

        mAdapter.setListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void onClick(View v, int position) {
              //  Toast.makeText(getActivity(),"您点击了"+position+"行",Toast.LENGTH_SHORT).show();

            }
        });

        return view;
    }

    private void initView(View view) {
        mRecyclerView=(RecyclerView)view.findViewById(R.id.tz_recyclerview);
        mRefreshLayout=(MaterialRefreshLayout)view.findViewById(R.id.refresh);
        initRefresh();

    }


    private void initRefresh() {

        mAdapter = new MyAdapter(datas);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mRefreshLayout.setLoadMore(false);
        mRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {

            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshDatas();
                        mRefreshLayout.finishRefresh();
                    }
                }, 3000);

            }



        });
    }

    /**
     *初始化数据
     */
    private void initDatas() {
        BmobQuery<BmobArticle> bmobArticleBmobQuery = new BmobQuery<>();
        bmobArticleBmobQuery.findObjects(new FindListener<BmobArticle>() {
            @Override
            public void done(List<BmobArticle> object, BmobException e) {
                if (e == null) {
                    Log.i("log","查询成功"+object.size());
                    datas=object;
                    mAdapter.setLists(datas);
                    mRecyclerView.setAdapter(mAdapter);
                } else {
                    Log.i("log","查询失败"+e.getMessage());
                    Toast.makeText(getActivity(), "请检查网络是否连通", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    /**
     *刷新数据
     */
    private void refreshDatas() {
        BmobQuery<BmobArticle> bmobArticleBmobQuery = new BmobQuery<>();
        bmobArticleBmobQuery.findObjects(new FindListener<BmobArticle>() {
            @Override
            public void done(List<BmobArticle> object, BmobException e) {
                if (e == null) {
                    Log.i("log","查询成功"+object.size());
                    Log.i("log","obj"+object);
                    Log.i("log","data"+datas);
                    if (datas.size()==object.size()){
                        Toast.makeText(getActivity(), "已经没有更多数据了", Toast.LENGTH_SHORT).show();
                    }else {
                        mList=object;
                        mAdapter.setLists(mList);
                        mAdapter.notifyDataSetChanged();
                    }

                } else {
                    Log.i("log","查询失败"+e.getMessage());
                    Toast.makeText(getActivity(), "请检查网络是否连通", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
    @Override
    public void onClick(View view) {

    }


}
