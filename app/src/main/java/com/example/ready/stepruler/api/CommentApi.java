package com.example.ready.stepruler.api;

import com.example.ready.stepruler.bean.comment.CommentBean;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by single dog on 2018/1/3.
 */

public interface CommentApi {
    String Host="http://10.0.2.2:8080/";

    //获取当前说说的评论内容
    @POST("comment/getComments")
    Call<ArrayList<CommentBean>> getComments(@Query("community_id") int community_id);
}
