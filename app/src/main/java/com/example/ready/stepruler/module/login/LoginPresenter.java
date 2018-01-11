package com.example.ready.stepruler.module.login;

import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.PushService;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.example.ready.stepruler.MainActivity;
import com.example.ready.stepruler.api.UserApi;
import com.example.ready.stepruler.utils.AppManager;
import com.example.ready.stepruler.utils.RetrofitFactory;

import cn.leancloud.chatkit.LCChatKit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by single dog on 2017/12/18.
 */

public class LoginPresenter implements OnLoginFinishedListener {
    private LoginView loginView;
    private LoginModel loginModel;

    public LoginPresenter(LoginView loginView) {
        this.loginView = loginView;
        this.loginModel = new LoginModel();
    }

    public void doLogin(String name, String pwd) {
        loginModel.login(name, pwd, this);
    }

    @Override
    public void onUsernameError() {
        loginView.showToast("没有该用户名!");
    }

    @Override
    public void onPasswordError() {
        loginView.showToast("密码错误！");
    }

    @Override
    public void onSuccess(int id, String name) {
        commitLogin(id, name);
    }

    private void commitLogin(final int clientId, final String name) {
        LCChatKit.getInstance().open(String.valueOf(clientId), new AVIMClientCallback() {
            @Override
            public void done(AVIMClient avimClient, AVIMException e) {
                if (null == e) {
                    commitDevice(clientId, name);
                    loginView.changeView(clientId);
                } else {
                    Toast.makeText(AppManager.getAppManager().currentActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //获取设备id,将设备号发送给leancloud以及我们的数据库
    // 每次登陆之后执行，将用户和设备每时每刻都绑定在一起
    private void commitDevice(final int user_id, final String name){
        //设置推送服务默认回调的activity
        PushService.setDefaultPushCallback(AppManager.getAppManager().currentActivity(), MainActivity.class);
        //保存installationId到服务器
        AVInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                AVInstallation.getCurrentInstallation().saveInBackground();
                Call<Boolean> call = RetrofitFactory.getRetrofit().create(UserApi.class).commitDevice(user_id, AVInstallation.getCurrentInstallation().getInstallationId());
                call.enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if(response.body()){
                            MainActivity.getUser().setDevice(AVInstallation.getCurrentInstallation().getInstallationId());
                            //登陆之后订阅频道，接受该频道发过来的消息，打开对应的Activity
                            PushService.subscribe(AppManager.getAppManager().currentActivity(), name, MainActivity.class);
                        }
                    }
                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                    }
                });
            }
        });
    }
}
