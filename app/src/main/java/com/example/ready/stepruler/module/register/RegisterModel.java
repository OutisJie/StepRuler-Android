package com.example.ready.stepruler.module.register;

import com.example.ready.stepruler.module.login.User;
import com.example.ready.stepruler.api.UserApi;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by single dog on 2017/12/19.
 */

public class RegisterModel {
    private Retrofit retrofit=new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("http://10.0.2.2:8080/user/")
            .build();

    private UserApi userApi=retrofit.create(UserApi.class);

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
