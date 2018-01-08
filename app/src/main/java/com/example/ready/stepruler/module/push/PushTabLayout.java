package com.example.ready.stepruler.module.push;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.ready.stepruler.R;
import com.example.ready.stepruler.adapter.BasePagerAdapter;
import com.example.ready.stepruler.module.push.Photo.PushPhoto;
import com.example.ready.stepruler.module.push.article.PushArticle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ready on 2017/11/20.
 */

public class PushTabLayout extends Fragment {
    public static final String TAG = "PushTabLayout";
    private static PushTabLayout instance = null;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private List<String> titleList;
    private List<Fragment> fragmList;
    private LinearLayout linearLayout;

    public static PushTabLayout getInstance(){
        if(instance == null)
            instance = new PushTabLayout();
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_push_tab, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mTabLayout = (TabLayout) view.findViewById(R.id.tab_layout_push);
        mViewPager = (ViewPager)view.findViewById(R.id.view_pager_types);
        mTabLayout.setTabMode(TabLayout.GRAVITY_CENTER);
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.push_type_article),true);
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.push_type_photo), false);

        fragmList = new ArrayList<>();

        fragmList.add(PushArticle.newInstance());

        fragmList.add(PushPhoto.newInstance());
        titleList = new ArrayList<>();
        titleList.add("文章");
        titleList.add("图片");
        BasePagerAdapter mPagerAdapter = new BasePagerAdapter(getChildFragmentManager(),fragmList,titleList);
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        linearLayout = (LinearLayout) view.findViewById(R.id.header_layout);
        //linearLayout.setBackgroundColor(SettingUtil.getInstance().getColor());
    }

    public void onDoubleClick() {
    }
}
