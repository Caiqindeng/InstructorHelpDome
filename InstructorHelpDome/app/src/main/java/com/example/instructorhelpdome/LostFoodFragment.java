package com.example.instructorhelpdome;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.instructorhelpdome.JavaBean.LostInfomationReq;
import com.example.instructorhelpdome.adapter.LostAndFoundAdapter;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

import static android.app.Activity.RESULT_OK;


public class LostFoodFragment extends Fragment implements View.OnClickListener, LostAndFoundAdapter.ItemClickListener  {
    private RecyclerView recyclerView;
    private LostAndFoundAdapter lostAndFoundAdapter;
    private List<LostInfomationReq> lostInfomationReqList;

    private long exitTime = 0;
    private final static int REQUEST_CODE = 999;
    public LostFoodFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lost_food,container,false);
        initView(view);
        initData();
        return view;
    }


    private void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.rl_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayout.VERTICAL, false));
        lostAndFoundAdapter = new LostAndFoundAdapter(getActivity());
        lostAndFoundAdapter.setLongClickListener(this);

    }
    private void initData() {
        BmobQuery<LostInfomationReq> lostInfomationReqBmobQuery = new BmobQuery<>();
        lostInfomationReqBmobQuery.order("-updatedAt");//排序
        lostInfomationReqBmobQuery.findObjects(new FindListener<LostInfomationReq>() {
            @Override
            public void done(List<LostInfomationReq> list, BmobException e) {
                if (e == null) {
                    lostInfomationReqList = list;
                    lostAndFoundAdapter.setData(list);
                    recyclerView.setAdapter(lostAndFoundAdapter);
                } else {
                    showToast("查询数据失败");
                }
            }
        });
    }
    private void showToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onEditOrDeleteClick(int position, int code) {
        if (code == LostAndFoundAdapter.EDIT_CODE) {
            Intent intent = new Intent(getActivity(), Lost_fondActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("editData", lostInfomationReqList.get(position));
            intent.putExtras(bundle);
            startActivityForResult(intent, REQUEST_CODE);
        } else if (code == LostAndFoundAdapter.DELETE_CODE) {
            deleteItemData(position);
        }

    }

    private void deleteItemData(final int position) {
        if (lostInfomationReqList.size() != 0) {
            LostInfomationReq lostInfomationReq = new LostInfomationReq();
            lostInfomationReq.setObjectId(lostInfomationReqList.get(position).getObjectId());
            lostInfomationReq.delete(new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        lostInfomationReqList.remove(position);
                        lostAndFoundAdapter.setData(lostInfomationReqList);
                        lostAndFoundAdapter.notifyDataSetChanged();
                    } else {
                        showToast("删除数据失败");
                    }
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    refreshData();//数据刷新
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }



    /**
     * 查询数据库中最新的数据
     * */
    private void refreshData() {
        BmobQuery<LostInfomationReq> lostInfomationReqBmobQuery = new BmobQuery<>();
        lostInfomationReqBmobQuery.order("-updatedAt");//按更新时间排序
        lostInfomationReqBmobQuery.findObjects(new FindListener<LostInfomationReq>() {
            @Override
            public void done(List<LostInfomationReq> list, BmobException e) {
                if (e == null) {
                    lostInfomationReqList = list;
                    lostAndFoundAdapter.setData(list);
                    lostAndFoundAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}
