package com.example.instructorhelpdome.adapter;

import android.app.Fragment;
import android.app.FragmentManager;


import java.util.ArrayList;
import java.util.List;

import androidx.legacy.app.FragmentPagerAdapter;


/**
 * Created by l on 2019/4/29.
 */
public class TitleFragmentPagerAdapter  extends FragmentPagerAdapter {

    private List<Fragment> mFragmentList=null;
    private String[] titles;

    public TitleFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public TitleFragmentPagerAdapter(FragmentManager mFragmentManager,
                                     ArrayList<Fragment> fragmentList) {
        super(mFragmentManager);
        mFragmentList = fragmentList;
    }
    public TitleFragmentPagerAdapter(FragmentManager mFragmentManager,
                                     List<Fragment> fragmentList, String[] titles) {
        super(mFragmentManager);
        mFragmentList = fragmentList;
        this.titles = titles;
    }
    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;
        if (position < mFragmentList.size()) {
            fragment = mFragmentList.get(position);
        } else {
            fragment = mFragmentList.get(0);
        }
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (titles != null && titles.length > 0)
            return titles[position];
        return null;
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }
}
