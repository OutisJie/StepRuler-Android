package com.example.ready.stepruler.module.register;

import com.example.ready.stepruler.api.UserApi;
import com.example.ready.stepruler.module.login.User;
import com.example.ready.stepruler.utils.RetrofitFactory;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by single dog on 2017/12/19.
 */

public class RegisterModel {

    private UserApi userApi= RetrofitFactory.getRetrofit().create(UserApi.class);

    public void register(User user, final OnRegisterFinishedListener onRegisterFinishedListener){
        Call<ResponseBody> userCall=userApi.addUser(user);
        userCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String result=response.body().string();
                    if(result.equals("failed")){
                        onRegisterFinishedListener.onUsernameError();
                    }
                    if(result.equals("success")){
                        onRegisterFinishedListener.onSuccess();
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }
}
