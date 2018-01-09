package com.example.ready.stepruler.api;

import com.example.ready.stepruler.bean.user.UserBean;
import com.example.ready.stepruler.module.login.User;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by single dog on 2017/12/18.
 */

public interface UserApi {
    @POST("user/register")
    Call<ResponseBody> addUser(@Body User user);

    @POST("user/login")
    Call<Integer> login(@Query("user_name") String name, @Query("user_pwd")String pwd);

    @POST("user/search")
    Call<ArrayList<String>> search(@Query("user_name") String name);

    @POST("user/postDevice")
    Call<Boolean> commitDevice(@Query("user_id") int id,@Query("device_id") String device_id);

    @POST("user/addFriend")
    Call<Boolean> addFriend(@Query("user_id1") int id1, @Query("user_id2") int id2);

    @GET("user/getDevice")
    Call<String> getDevice(@Query("user_name") String name, @Query("user_id") int id);

    //获取好友列表
    @GET(value = "/user/friends")
    Observable<List<UserBean>> getFriends(@Query("user_id") int user_id);

    //上传头像
    @POST(value = "/user/postImg")
    @Multipart
    Observable<String> updatePhoto(@Query("user_id") int user_id, @Part MultipartBody.Part photo);

    //获取用户信息
    @GET(value = "/user/getUserInfo")
    Call<List<String>> getUserInfo(@Query("user_id") int id);

}
