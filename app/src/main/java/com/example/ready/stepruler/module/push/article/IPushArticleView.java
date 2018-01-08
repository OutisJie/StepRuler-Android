package com.example.ready.stepruler.module.push.article;

import com.example.ready.stepruler.bean.push.PushArticleDataBean;
import com.example.ready.stepruler.module.base.IBaseListView;
import com.example.ready.stepruler.module.base.IBasePresenter;

import java.util.List;

/**
 * Created by ready on 2017/12/9.
 */

public interface IPushArticleView {
    interface View extends IBaseListView<Presenter> {
        //请求数据
        void onloadData();

        //刷新数据
        void onRefresh();
    }
    interface Presenter extends IBasePresenter{
        //请求数据
        void doLoadData();

        //再次请求数据
        void doLoadMoreData();

        //加载完毕
        void doShowNoMore();

        //设置适配器
        void doSetAdapter(List<PushArticleDataBean> list);
    }
}
