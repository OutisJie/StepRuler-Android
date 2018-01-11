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
import com.example.ready.stepruler.module.push.article.PushArticleFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ready on 2017/11/20.
 */

public class PushFragment extends Fragment {
    public static final String TAG = "PushFragment";
    private static PushFragment instance = null;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private List<String> titleList;
    private List<Fragment> fragmList;
    private LinearLayout linearLayout;

    public static PushFragment getInstance(){
        if(instance == null)
            instance = new PushFragment();
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
        mTabLayout.setTabMode(TabLayout.GRAVITY_FILL);
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.push_type_article),true);

        fragmList = new ArrayList<>();

        fragmList.add(PushArticleFragment.newInstance());

        titleList = new ArrayList<>();
        titleList.add("文章");
        BasePagerAdapter mPagerAdapter = new BasePagerAdapter(getChildFragmentManager(),fragmList,titleList);
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        linearLayout = (LinearLayout) view.findViewById(R.id.header_layout);
        //linearLayout.setBackgroundColor(SettingUtil.getInstance().getColor());
    }

    public void onDoubleClick() {
    }
}
