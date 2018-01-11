package com.example.ready.stepruler.api;

import com.example.ready.stepruler.bean.push.PushArticleDataBean;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by ready on 2017/12/28.
 */

public interface ArticleApi {


    //获取所有文章
    @GET(value = "article/list/{random}")
    Observable<List<PushArticleDataBean>> getArticleList(@Path("random") int random);

    //获取单篇文章
    @GET(value = "article/single/{article_id}")
    Observable<PushArticleDataBean> getArticle(@Path("article_id") int article_id);

    //搜索文章
    @POST(value = "article/search")
    Call<ArrayList<PushArticleDataBean>> search(@Query("searchData") String searchData);

}
