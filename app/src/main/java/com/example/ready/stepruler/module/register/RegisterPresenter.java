package com.example.ready.stepruler.module.register;

import com.example.ready.stepruler.module.login.User;

/**
 * Created by single dog on 2017/12/19.
 */

public class RegisterPresenter implements OnRegisterFinishedListener {
    private RegisterView registerView;
    private RegisterModel registerModel;

    public RegisterPresenter(RegisterView registerView){
        this.registerView=registerView;
        this.registerModel=new RegisterModel();
    }

    public void doRegister(User user){
        registerModel.register(user,this);
    }

    @Override
    public void onUsernameError() {
        registerView.showToast("该用户名已经存在！");
    }

    @Override
    public void onSuccess(){
        registerView.showToast("恭喜你，注册成功！");
    }
}
