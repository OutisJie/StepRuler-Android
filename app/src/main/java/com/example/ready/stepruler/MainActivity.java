package com.example.ready.stepruler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.ready.stepruler.api.UserApi;
import com.example.ready.stepruler.bean.user.UserBean;
import com.example.ready.stepruler.module.Diary.diarys.DiaryFragment;
import com.example.ready.stepruler.module.Step.StepFragment;
import com.example.ready.stepruler.module.community.CommunityFragment;
import com.example.ready.stepruler.module.login.LoginActivity;
import com.example.ready.stepruler.module.push.PushFragment;
import com.example.ready.stepruler.module.search.SearchActivity;
import com.example.ready.stepruler.module.social.FriendsActivity;
import com.example.ready.stepruler.utils.AppManager;
import com.example.ready.stepruler.utils.ImageLoadUtil;
import com.example.ready.stepruler.utils.RetrofitFactory;
import com.example.ready.stepruler.widget.BottomNavigationWidget;
import com.yancy.gallerypick.config.GalleryConfig;
import com.yancy.gallerypick.config.GalleryPick;
import com.yancy.gallerypick.inter.IHandlerCallBack;
import com.yancy.gallerypick.inter.ImageLoader;
import com.yancy.gallerypick.widget.GalleryImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.leancloud.chatkit.LCChatKitUser;
import cn.leancloud.chatkit.cache.LCIMProfileCache;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String POSITION = "position";
    private static final String SELECT_ITEM = "bottomNavigationSelectItem";
    private static final int FRAGMENT_PUSH = 0;
    private static final int FRAGMENT_COMMUNITY = 1;
    private static final int FRAGMENT_DIARY = 2;
    private static final int FRAGMENT_STEP = 3;
    private PushFragment mPushFragment;
    private CommunityFragment mCommunityFragment;
    private DiaryFragment mDiaryFragment;
    private StepFragment mStepFragment;
    private Toolbar toolbar;
    private BottomNavigationView mBottomNavigationView;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private static ImageView mImageView;
    private static TextView mState;
    private long firstClickTime = 0;
    private long exitTime = 0;
    private int position;

    //用户登录信息
    private static boolean isLogin = false;
    private static UserBean user = new UserBean();

    public static void startActivity(Context context) {
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
            mPushFragment = (PushFragment) getSupportFragmentManager().findFragmentByTag(PushFragment.class.getName());
            mCommunityFragment = (CommunityFragment) getSupportFragmentManager().findFragmentByTag(CommunityFragment.class.getName());
            mDiaryFragment = (DiaryFragment) getSupportFragmentManager().findFragmentByTag(DiaryFragment.class.getName());
            mStepFragment = (StepFragment) getSupportFragmentManager().findFragmentByTag(StepFragment.class.getName());
            // 恢复 recreate 前的位置
            showFragment(savedInstanceState.getInt(POSITION));
            mBottomNavigationView.setSelectedItemId(savedInstanceState.getInt(SELECT_ITEM));
        } else {
            showFragment(FRAGMENT_PUSH);
        }
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_activity_main);
        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        View centerHeader = mNavigationView.inflateHeaderView(R.layout.na_center_header);
        mImageView = (ImageView) centerHeader.findViewById(R.id.image_center_header);
        mState = (TextView)centerHeader.findViewById(R.id.tv_state);

        setSupportActionBar(toolbar);
        BottomNavigationWidget.disableShiftMode(mBottomNavigationView);
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
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

        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mNavigationView.setNavigationItemSelectedListener(this);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLogin == false) {
                    finish();
                    Intent login = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(login);
                } else {
                    //修改头像界面
                    requesrRermission();
                }

            }
        });
    }

    private void doubleClick(int index) {
        long secondClickTime = System.currentTimeMillis();
        if (secondClickTime - firstClickTime < 500) {
            switch (index) {
                case FRAGMENT_PUSH:
                    mPushFragment.onDoubleClick();
                    break;
                case FRAGMENT_COMMUNITY:
                    mCommunityFragment.onDoubleClick();
                    break;
            }
        } else {
            firstClickTime = secondClickTime;
        }
    }

    private void showFragment(int index) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        hideFragment(ft);
        position = index;
        switch (index) {
            case FRAGMENT_PUSH:
                toolbar.setTitle(R.string.title_push);
                if (mPushFragment == null) {
                    mPushFragment = PushFragment.getInstance();
                    ft.add(R.id.container, mPushFragment, PushFragment.class.getName());
                } else {
                    ft.show(mPushFragment);
                }
                break;
            case FRAGMENT_COMMUNITY:
                toolbar.setTitle(R.string.title_community);
                if (mCommunityFragment == null) {
                    mCommunityFragment = CommunityFragment.getInstance();
                    ft.add(R.id.container, mCommunityFragment, CommunityFragment.class.getName());
                } else {
                    ft.show(mCommunityFragment);
                }
                break;
            case FRAGMENT_DIARY:
                toolbar.setTitle(R.string.title_diary);
                if (mDiaryFragment == null) {
                    mDiaryFragment = DiaryFragment.getInstace();
                    ft.add(R.id.container, mDiaryFragment, DiaryFragment.class.getName());
                } else {
                    ft.show(mDiaryFragment);
                }
                break;
            case FRAGMENT_STEP:
                toolbar.setTitle(R.string.title_step);
                if (mStepFragment == null) {
                    mStepFragment = StepFragment.getInstance();
                    ft.add(R.id.container, mStepFragment, StepFragment.class.getName());
                } else {
                    ft.show(mStepFragment);
                }
                break;
        }
        ft.commit();
    }

    private void hideFragment(FragmentTransaction ft) {
        //如果几个fragment不为空，就先隐藏起来
        if (mPushFragment != null) {
            ft.hide(mPushFragment);
        }
        if (mCommunityFragment != null) {
            ft.hide(mCommunityFragment);
        }
        if (mDiaryFragment != null) {
            ft.hide(mDiaryFragment);
        }
        if (mStepFragment != null) {
            ft.hide(mStepFragment);
        }
    }

    private void requesrRermission() {
        if (ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
            openGallery();
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.WRITE_EXTERNAL_STORAGE")) {
            Toast.makeText(this, "请在 设置-应用管理 中开启此应用的储存授权。", Toast.LENGTH_SHORT).show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 2);
        }
    }

    private void openGallery() {
        GalleryPick.getInstance()
                .setGalleryConfig(new GalleryConfig.Builder()
                        .imageLoader(new ImageLoader() {
                            @Override
                            public void displayImage(Activity activity, Context context, String path, GalleryImageView galleryImageView, int width, int height) {
                                RequestOptions requestOptions = new RequestOptions();
                                requestOptions.centerCrop();
                                Glide.with(context).load(path).apply(requestOptions).into(galleryImageView);
                            }

                            @Override
                            public void clearMemoryCache() {

                            }
                        })
                        .iHandlerCallBack(new IHandlerCallBack() {
                            @Override
                            public void onStart() {

                            }

                            @Override
                            public void onSuccess(List<String> photoList) {
                                if (photoList.size() > 0) {
                                    updatePhoto(photoList.get(0));
                                }
                            }

                            @Override
                            public void onCancel() {

                            }

                            @Override
                            public void onFinish() {

                            }

                            @Override
                            public void onError() {

                            }
                        })
                        .provider("com.yancy.gallerypickdemo.fileprovider")
                        .pathList(new ArrayList<String>())
                        .multiSelect(false)
                        .maxSize(9)
                        .crop(true)
                        .isShowCamera(true)
                        .filePath("/Gallery/Pictures")
                        .build())
                .open(this);
    }

    private void updatePhoto(final String filename) {
        Luban.with(this)
                .load(filename)
                .ignoreBy(100)
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(File file) {
                        String type = filename.substring(filename.lastIndexOf(".") + 1);
                        MultipartBody.Part photo = MultipartBody.Part.createFormData("photo",
                                file.getName(), RequestBody.create(MediaType.parse("image/" + type),
                                        file));
                        Observable<String> ob = RetrofitFactory.getRetrofit().create(UserApi.class).updatePhoto(user.getUserId(), photo);
                        ob.subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<String>() {
                                    @Override
                                    public void accept(String s) throws Exception {
                                        ImageLoadUtil.loadCenterCrop(MainActivity.this, s.toString(), mImageView, R.color.viewBackground, R.drawable.ice_user_header);
                                        LCChatKitUser user = new LCChatKitUser(String.valueOf(getUser().getUserId()), getUser().getUserName(), s.toString());
                                        LCIMProfileCache.getInstance().cacheUser(user);
                                    }
                                }, new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) throws Exception {
                                        Toast.makeText(MainActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(MainActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                    }
                }).launch();
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
            AppManager.getAppManager().AppExit(this);
        } else {
            Toast.makeText(this, R.string.double_click_exit, Toast.LENGTH_SHORT).show();
            exitTime = currentTime;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            SearchActivity.startActivity(this);
        }
        return super.onOptionsItemSelected(item);
    }

    //侧栏菜单的点击事件
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_account:
                //Toast.makeText(this, "您想要修改个人信息吗？", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_friend:
                FriendsActivity.lunch(MainActivity.this, 2, "张家浩");
                break;
            case R.id.nav_history:
                //Toast.makeText(this, "想看历史文章？想多了吧", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_manage:
               // Toast.makeText(this, "卧槽你还想要修改设置？", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_change:
               // Toast.makeText(this, "换个毛主题，就这样挺好的", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_exit:
               // Toast.makeText(this, "我们还是朋友(-_-)", Toast.LENGTH_SHORT).show();
                break;
        }
        return false;
    }

    private void getUserInformation() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        try {
            user.setUserId(bundle.getInt("user_id"));
            user.setUserName(bundle.getString("user_name"));
            if (!user.getUserName().equals("") || user.getUserName() != null)
                isLogin = true;
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public static void setState(){
        if (isLogin == true){
            Toast.makeText(AppManager.getAppManager().currentActivity(), "nihao", Toast.LENGTH_SHORT).show();
            ImageLoadUtil.loadCenterCrop(AppManager.getAppManager().currentActivity(),  getUser().getUserImg(), mImageView, R.drawable.ice_user_header);
            mState.setText("点击修改头像");
        }
    }

    public static boolean getLoginState() {
        return isLogin;
    }

    public static UserBean getUser() {
        return user;
    }
}