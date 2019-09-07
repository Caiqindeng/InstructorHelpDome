package com.example.instructorhelpdome.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.instructorhelpdome.JavaBean.Foodphone;
import com.example.instructorhelpdome.R;

import java.util.List;

public class FoodTeleAdapter extends BaseAdapter {
    private List<Foodphone> mFoodphoneList;
    private LayoutInflater mLayoutInflater;



    public FoodTeleAdapter(List<Foodphone> foodphones, Context context){
        this.mFoodphoneList=foodphones;
        this.mLayoutInflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mFoodphoneList==null?0:mFoodphoneList.size();
    }

    @Override
    public Object getItem(int i) {
        return mFoodphoneList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        //加载布局为一个视图
        View view1=mLayoutInflater.inflate(R.layout.food_item,null);
        Foodphone foodphone=mFoodphoneList.get(i);
        TextView tshopname =(TextView)view1.findViewById(R.id.shopname);
        TextView tshoptele =(TextView) view1.findViewById(R.id.shoptele);
        tshopname.setText(foodphone.getShopname());
        tshoptele.setText(foodphone.getShopphone());
        return view1;
    }
}
