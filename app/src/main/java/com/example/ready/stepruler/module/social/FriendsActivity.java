package com.example.ready.stepruler.module.social;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.ready.stepruler.R;
import com.example.ready.stepruler.adapter.BasePagerAdapter;
import com.example.ready.stepruler.module.search.SearchActivity;
import com.example.ready.stepruler.utils.AppManager;

import java.util.Arrays;

import cn.leancloud.chatkit.activity.LCIMConversationListFragment;

public class FriendsActivity extends AppCompatActivity {
    private static final String TAG = "FriendsActivity";
    //控件
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    //获取当前登录的用户信息
    public static void lunch(Context context, int user_id, String user_name){
        context.startActivity(new Intent(context, FriendsActivity.class)
            .putExtra("user_id", user_id)
            .putExtra("user_name",user_name)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        AppManager.getAppManager().addActivity(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        viewPager = (ViewPager) findViewById(R.id.pager);
        initView();
    }

    private void initView(){
        initTabLayout();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchActivity.startActivity(FriendsActivity.this);
            }
        });
    }
    private void initTabLayout() {
        String[] tabList = new String[]{"消息", "联系人"};
        final Fragment[] fragmentList = new Fragment[] {new LCIMConversationListFragment(), FriendsFragment.newInstance()};

        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        for (int i = 0; i < tabList.length; i++) {
            tabLayout.addTab(tabLayout.newTab().setText(tabList[i]));
        }

        BasePagerAdapter adapter = new BasePagerAdapter(getSupportFragmentManager(), Arrays.asList(fragmentList), Arrays.asList(tabList));
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AppManager.getAppManager().finishActivity(this);
    }
}
