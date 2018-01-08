package com.example.ready.stepruler.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ready.stepruler.R;
import com.example.ready.stepruler.bean.friends.UserBean;
import com.example.ready.stepruler.module.social.FriendsActivity;
import com.example.ready.stepruler.module.login.LoginActivity;
import com.example.ready.stepruler.module.Diary.diarys.DiaryTabLayout;
import com.example.ready.stepruler.module.Step.StepTabLayout;
import com.example.ready.stepruler.module.community.CommunityTabLayout;
import com.example.ready.stepruler.module.push.PushTabLayout;
import com.example.ready.stepruler.module.search.SearchActivity;
import com.example.ready.stepruler.utils.AppManager;
import com.example.ready.stepruler.widget.BottomNavigationViewHelper;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private static final String POSITION = "position";
    private static final String SELECT_ITEM = "bottomNavigationSelectItem";
    private static final int FRAGMENT_PUSH = 0;
    private static final int FRAGMENT_COMMUNITY = 1;
    private static final int FRAGMENT_DIARY = 2;
    private static final int FRAGMENT_STEP = 3;
    private PushTabLayout mPushTabLayout;
    private CommunityTabLayout mCommunityTabLayout;
    private DiaryTabLayout mDiaryTabLayout;
    private StepTabLayout mStepTabLayout;
    private Toolbar toolbar;
    private BottomNavigationView mBottomNavigationView;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private long firstClickTime = 0;
    private long exitTime = 0;
    private int position;

    //用户登录信息
    private static boolean isLogin = false;
    private static UserBean user = new UserBean();

    public static void  startActivity(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
        AppManager.getAppManager().finishActivity(context.getClass());
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppManager.getAppManager().addActivity(this);
        initView();
        getUserInformation();
        if (savedInstanceState != null) {
            mPushTabLayout = (PushTabLayout) getSupportFragmentManager().findFragmentByTag(PushTabLayout.class.getName());
            mCommunityTabLayout = (CommunityTabLayout) getSupportFragmentManager().findFragmentByTag(CommunityTabLayout.class.getName());
            mDiaryTabLayout = (DiaryTabLayout) getSupportFragmentManager().findFragmentByTag(DiaryTabLayout.class.getName());
            mStepTabLayout = (StepTabLayout) getSupportFragmentManager().findFragmentByTag(StepTabLayout.class.getName());
            // 恢复 recreate 前的位置
            showFragment(savedInstanceState.getInt(POSITION));
            mBottomNavigationView.setSelectedItemId(savedInstanceState.getInt(SELECT_ITEM));
        } else {
            showFragment(FRAGMENT_PUSH);
        }
    }

    private void initView(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_activity_main);
        setSupportActionBar(toolbar);
        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        BottomNavigationViewHelper.disableShiftMode(mBottomNavigationView);
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_push:
                        showFragment(FRAGMENT_PUSH);
                        doubleClick(FRAGMENT_PUSH);
                        break;
                    case R.id.action_community:
                        showFragment(FRAGMENT_COMMUNITY);
                        doubleClick(FRAGMENT_COMMUNITY);
                        break;
                    case R.id.action_diary:
                        showFragment(FRAGMENT_DIARY);
                        break;
                    case R.id.action_step:
                        showFragment(FRAGMENT_STEP);
                        break;
                }
                return true;
            }
        });
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        View centerHeader = mNavigationView.inflateHeaderView(R.layout.na_center_header);
        ImageView mImageView = (ImageView) centerHeader.findViewById(R.id.image_center_header);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isLogin==false){
                    finish();
                    Intent login = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(login);
                }else {
                    //修改头像界面
                    String s = String.valueOf(user.getUserId());
                    Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    private void doubleClick(int index) {
        long secondClickTime = System.currentTimeMillis();
        if(secondClickTime - firstClickTime < 500){
            switch (index){
                case FRAGMENT_PUSH:
                    mPushTabLayout.onDoubleClick();
                    break;
                case FRAGMENT_COMMUNITY:
                    mCommunityTabLayout.onDoubleClick();
                    break;
            }
        }else {
            firstClickTime = secondClickTime;
        }
    }

    private void showFragment(int index) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        hideFragment(ft);
        position = index;
        switch (index){
            case FRAGMENT_PUSH:
                toolbar.setTitle(R.string.title_push);
                if(mPushTabLayout == null){
                    mPushTabLayout = PushTabLayout.getInstance();
                    ft.add(R.id.container, mPushTabLayout, PushTabLayout.class.getName());
                }else {
                    ft.show(mPushTabLayout);
                }
                break;
            case FRAGMENT_COMMUNITY:
                toolbar.setTitle(R.string.title_community);
                if(mCommunityTabLayout == null){
                    mCommunityTabLayout = CommunityTabLayout.getInstance();
                    ft.add(R.id.container, mCommunityTabLayout, CommunityTabLayout.class.getName());
                }else {
                    ft.show(mCommunityTabLayout);
                }
                break;
            case FRAGMENT_DIARY:
                toolbar.setTitle(R.string.title_diary);
                if (mDiaryTabLayout == null){
                    mDiaryTabLayout = DiaryTabLayout.getInstace();
                    ft.add(R.id.container, mDiaryTabLayout, DiaryTabLayout.class.getName());
                }else {
                    ft.show(mDiaryTabLayout);
                }
                break;
            case FRAGMENT_STEP:
                toolbar.setTitle(R.string.title_step);
                if(mStepTabLayout == null){
                    mStepTabLayout = StepTabLayout.getInstance();
                    ft.add(R.id.container, mStepTabLayout, StepTabLayout.class.getName());
                }else {
                    ft.show(mStepTabLayout);
                }
                break;
        }
        ft.commit();
    }

    private void hideFragment(FragmentTransaction ft) {
        //如果几个fragment不为空，就先隐藏起来
        if(mPushTabLayout != null){
            ft.hide(mPushTabLayout);
        }
        if(mCommunityTabLayout != null){
            ft.hide(mCommunityTabLayout);
        }
        if(mDiaryTabLayout != null){
            ft.hide(mDiaryTabLayout);
        }
        if(mStepTabLayout != null){
            ft.hide(mStepTabLayout);
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // recreate 时记录当前位置 (在 Manifest 已禁止 Activity 旋转,所以旋转屏幕并不会执行以下代码)
        outState.putInt(POSITION, position);
        outState.putInt(SELECT_ITEM, mBottomNavigationView.getSelectedItemId());
    }

    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if ((currentTime - exitTime) < 2000) {
            super.onBackPressed();
            AppManager.getAppManager().AppExit(this );
        } else {
            Toast.makeText(this, R.string.double_click_exit, Toast.LENGTH_SHORT).show();
            exitTime = currentTime;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_search){
            SearchActivity.startActivity(this);
        }
        return super.onOptionsItemSelected(item);
    }

    //侧栏菜单的点击事件
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_account:
                Toast.makeText(this, "您想要修改个人信息吗？", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_friend:
                FriendsActivity.lunch(MainActivity.this, 2, "张家浩");
                break;
            case R.id.nav_history:
                Toast.makeText(this, "想看历史文章？想多了吧", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_manage:
                Toast.makeText(this, "卧槽你还想要修改设置？", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_change:
                Toast.makeText(this, "换个毛主题，就这样挺好的", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_exit:
                Toast.makeText(this, "我们还是朋友(-_-)", Toast.LENGTH_SHORT).show();
                break;
        }
        return false;
    }

    private void getUserInformation(){
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        try {
            user.setUserId(bundle.getInt("user_id"));
            user.setUserName(bundle.getString("user_name"));
            if(!user.getUserName().equals("") || user.getUserName() != null)
                isLogin=true;
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    public static boolean getLoginState(){return isLogin;}
//    public static int getUserid(){
//        return user.getUserId();
//    }
//    public static String getUsername(){
//        return user.getUserName();
//    }
    public static UserBean getUser(){
        return user;
    }
}
