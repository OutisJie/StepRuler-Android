package com.example.ready.stepruler.module.login;


import com.example.ready.stepruler.api.UserApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by single dog on 2017/12/18.
 */

public class LoginModel {
    private Retrofit retrofit=new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("http://10.0.2.2:8080/user/")
            .build();

    private UserApi userApi = retrofit.create(UserApi.class);

    public void login(final User user, final OnLoginFinishedListener onLoginFinishedListener){
        Call<Integer> usercall = userApi.login(user);
        usercall.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call,Response<Integer> response) {
                if(response.body() == -1){
                    onLoginFinishedListener.onUsernameError();
                }else if(response.body()==-2){
                    onLoginFinishedListener.onPasswordError();
                }else {
                    onLoginFinishedListener.onSuccess(response.body(), user.getId());
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


}
