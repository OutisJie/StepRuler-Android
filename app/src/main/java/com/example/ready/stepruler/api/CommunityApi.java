package com.example.ready.stepruler.api;

import com.example.ready.stepruler.bean.community.CommunityBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

/**
 * Created by single dog on 2017/12/28.
 */

public interface CommunityApi {
    String Host = "http://10.0.2.2:8080/";

    //点赞
    @PUT("community/iszan")
    Call<Integer> dianzan(@Query("community_id") int id);

    //取消点赞
    @PUT("community/notzan")
    Call<Integer> quxiaozan(@Query("community_id") int id);

    //我所发过的所有说说，按时间排序，一次获取随机条数
    @GET("community/mine")
    Observable<List<CommunityBean>> getMine(@Query("user_id") int id, @Query("page") int page);

    //我的所有好友们的所有说说，按时间排序，一次获取随机条数
    @GET("community/follow")
    Observable<List<CommunityBean>> getFoll(@Query("user_id") int id, @Query("page") int page);

    //获取热门说说
    @GET("community/hot")
    Observable<List<CommunityBean>> getHot(@Query("page") int page);

    //发表说说
    @POST("community/post")
    Call<String> post(@Body CommunityBean community);

}
