package com.example.ready.stepruler.module.community;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ready.stepruler.R;
import com.example.ready.stepruler.adapter.BasePagerAdapter;
import com.example.ready.stepruler.module.community.follow.CommFollFragment;
import com.example.ready.stepruler.module.community.hot.CommHotFragment;
import com.example.ready.stepruler.module.community.me.CommMeFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ready on 2017/12/3.
 */

public class CommunityFragment extends Fragment {
    private static final String Tag = "CommunityFragment";
    private static CommunityFragment instance = null;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private List<String> titleList;
    private List<Fragment> fragmList;

    public static CommunityFragment getInstance() {
        if(instance == null)
            instance = new CommunityFragment();
        return instance;
    }

    public void onDoubleClick() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_community, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initView(View view) {
        mTabLayout = (TabLayout) view.findViewById(R.id.tab_layout_community);
        mViewPager = (ViewPager) view.findViewById(R.id.view_pager_communities);
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.addTab(mTabLayout.newTab().setText("热门文章"),true);
        mTabLayout.addTab(mTabLayout.newTab().setText("我的关注"), false);
        mTabLayout.addTab(mTabLayout.newTab().setText("我的文章"),false );

        fragmList = new ArrayList<>();
        fragmList.add(CommHotFragment.newInstance());
        fragmList.add(CommFollFragment.newInstance());
        fragmList.add(CommMeFragment.newInstance());
        titleList = new ArrayList<>();
        titleList.add("热门文章");
        titleList.add("我的关注");
        titleList.add("我的文章");
        BasePagerAdapter mPagerAdapter = new BasePagerAdapter(getChildFragmentManager(),fragmList,titleList);
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }
}
