package com.example.ready.stepruler.module.login;


import com.example.ready.stepruler.api.UserApi;
import com.example.ready.stepruler.utils.RetrofitFactory;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by single dog on 2017/12/18.
 */

public class LoginModel {

    private UserApi userApi = RetrofitFactory.getRetrofit().create(UserApi.class);

    public void login(final String name, final String pwd, final OnLoginFinishedListener onLoginFinishedListener){
        Call<Integer> usercall = userApi.login(name, pwd);
        usercall.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call,Response<Integer> response) {
                if(response.body() == -1){
                    onLoginFinishedListener.onUsernameError();
                }else if(response.body()==-2){
                    onLoginFinishedListener.onPasswordError();
                }else {
                    onLoginFinishedListener.onSuccess(response.body(), name);
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


}
