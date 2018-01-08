package com.example.ready.stepruler.module.push.content;

import com.example.ready.stepruler.bean.push.PushArticleDataBean;
import com.trello.rxlifecycle2.LifecycleTransformer;

/**
 * Created by ready on 2017/12/26.
 */

public interface IArticleContentView {
    interface view {
        //加载网页
        void onSetWebView(String url, boolean flag);

        //设置presenter
        void setPresenter(ArticleContentPresenter presenter);

        //绑定生命周期
        <T>LifecycleTransformer<T> bindToLife();
    }
    interface presenter{
        //请求数据
        void doLoadHtml(PushArticleDataBean dataBean);
    }
}
