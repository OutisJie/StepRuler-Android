package com.example.ready.stepruler.api;

import com.example.ready.stepruler.bean.friends.UserBean;
import com.example.ready.stepruler.module.login.User;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by single dog on 2017/12/18.
 */

public interface UserApi {
    @POST("register")
    Call<ResponseBody> addUser(@Body User user);

    @POST("login")
    Call<Integer> login(@Body User user);

    @POST("user/search")
    Call<ArrayList<String>> search(@Query("user_name") String name);

    @POST("user/postDevice")
    Call<Boolean> commitDevice(@Query("user_id") int id,@Query("device_id") String device_id);

    @POST("user/addFriend")
    Call<Boolean> addFriend(@Query("user_id1") int id1, @Query("user_id2") int id2);

    @GET("user/getDevice")
    Call<String> getDevice(@Query("user_name") String user_name, @Query("user_id") int id);

    //获取好友列表
    @GET(value = "/user/friends")
    Observable<List<UserBean>> getFriends(@Query("user_id") int user_id);
}
