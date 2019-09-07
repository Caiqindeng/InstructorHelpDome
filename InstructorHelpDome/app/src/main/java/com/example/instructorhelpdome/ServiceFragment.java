package com.example.instructorhelpdome;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.instructorhelpdome.adapter.TitleFragmentPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;


public class ServiceFragment extends Fragment {
    TabLayout tabLayout;
    ViewPager viewpager;


    String url1 = "https://qq.campusplus.com/bhu/synnews/list/bhu_news";
    String url2 = "http://www.bhu.edu.cn/fpage/pgeneral/index.asp";

    private  Toolbar ToolBarServer;
    public ServiceFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service,container,false);
        init(view);

        Fragment webfragment1=new webFragment();
        ((webFragment) webfragment1).setUrl(url1);
        Fragment webfragment2=new webFragment();
        ((webFragment) webfragment2).setUrl(url2);


        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new LostFoodFragment());
        fragments.add(webfragment2);
        fragments.add(webfragment1);
        TitleFragmentPagerAdapter adapter = new TitleFragmentPagerAdapter(getFragmentManager(), fragments, new String[]{"失物招领", "二手商品", "渤大新闻"});
        viewpager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewpager);
        return view;
    }
    private void init(View view) {
        tabLayout = (TabLayout) view.findViewById(R.id.tab);
        viewpager = (ViewPager) view.findViewById(R.id.viewpager_server);

    }
}
