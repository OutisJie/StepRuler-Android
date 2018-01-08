package com.example.ready.stepruler;

import android.app.Application;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.PushService;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.example.ready.stepruler.module.social.FriendsActivity;
import com.example.ready.stepruler.module.social.UserProvider;

import cn.leancloud.chatkit.LCChatKit;


/**
 * Created by ready on 2017/11/21.
 */

public class InitApp extends Application{
    // 此 id 与 key 仅供测试使用
    private final String APP_ID = "G1eKnXroVQvkLFggWSmqx86E-gzGzoHsz";
    private final String APP_KEY = "WzzFLelHVx8j23imTEHq0osY";

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化Chatkit组件
        LCChatKit.getInstance().setProfileProvider(UserProvider.getInstance());
        AVOSCloud.setDebugLogEnabled(true);
        LCChatKit.getInstance().init(getApplicationContext(), APP_ID, APP_KEY);
        AVIMClient.setAutoOpen(false);
        PushService.setDefaultPushCallback(this, FriendsActivity.class);
        //初始化应用信息
        AVOSCloud.initialize(this, APP_ID, APP_KEY);
        // 启用崩溃错误统计
        AVAnalytics.enableCrashReport(this.getApplicationContext(), true);
        AVOSCloud.setLastModifyEnabled(true);
        AVOSCloud.setDebugLogEnabled(true);
    }

}
