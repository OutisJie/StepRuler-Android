package com.example.ready.stepruler.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import java.util.List;

/**
 * Created by ready on 2017/12/7.
 */

public class BasePagerAdapter extends FragmentStatePagerAdapter{
    private List<Fragment> mFragmList;
    private List<String> mTitleList;
    public BasePagerAdapter(FragmentManager fm, List<Fragment> fragmList, List<String> titleList){
        super(fm);
        this.mFragmList = fragmList;
        this.mTitleList = titleList;
    }

    @Override
    public int getCount() {
        return mFragmList.size();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmList.get(position);
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitleList.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }
    public void recreateItems(List<Fragment> fragmList, List<String> titleList) {
        this.mFragmList = fragmList;
        this.mTitleList = titleList;
        notifyDataSetChanged();
    }
}

