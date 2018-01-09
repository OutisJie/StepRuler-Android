package com.example.ready.stepruler.module.push.article;

import android.support.annotation.NonNull;

import com.example.ready.stepruler.BuildConfig;
import com.example.ready.stepruler.api.ArticleApi;
import com.example.ready.stepruler.bean.push.PushArticleDataBean;
import com.example.ready.stepruler.utils.RetrofitFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ready on 2017/12/9.
 */

public class PushArticlePresenter implements IPushArticleView.Presenter {
    private static final String TAG = "PushArticlePresenter";
    private IPushArticleView.View view;
    private List<PushArticleDataBean> dataBeanList = new ArrayList<>();

    PushArticlePresenter(IPushArticleView.View view) {
        this.view = view;
    }

    @Override
    public void doLoadData() {
        //释放内存
        if (dataBeanList.size() > 150) {
            dataBeanList.clear();
        }
        getRandom()
                .subscribeOn(Schedulers.io())
                .map(new Function<List<PushArticleDataBean>, List<PushArticleDataBean>>() {
                    @Override
                    public List<PushArticleDataBean> apply(@NonNull List<PushArticleDataBean> list) throws Exception {
                        for (int i = 0; i < list.size() - 1; i++) {
                            for (int j = list.size() - 1; j > i; j--) {
                                if (list.get(j).getArticleId() == list.get(i).getArticleId()) {
                                    list.remove(j);
                                }
                            }
                        }
                        return list;
                    }
                })
                .compose(view.<List<PushArticleDataBean>>bindToLife())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<PushArticleDataBean>>() {
                    @Override
                    public void accept(List<PushArticleDataBean> list) throws Exception {
                        if (list != null && list.size() > 0) {
                            //有数据返回，则更新数据
                            doSetAdapter(list);
                        } else {
                            //没有数据返回，表示没有更多数据了
                            doShowNoMore();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        //网络异常
                        doShowNetError();
                        if (BuildConfig.DEBUG) {
                            throwable.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void doLoadMoreData() {
        doLoadData();
    }

    @Override
    public void doSetAdapter(List<PushArticleDataBean> list) {
        dataBeanList.addAll(list);
        view.onSetAdapter(dataBeanList);
        view.onHideLoading();
    }

    @Override
    public void doRefresh() {
        //刷新数据之前，把旧数据清空
        if (dataBeanList.size() != 0) {
            dataBeanList.clear();
        }
        view.onShowLoading();
        doLoadData();
    }

    @Override
    public void doShowNetError() {
        view.onHideLoading();
        view.onShowNetError();
    }

    @Override
    public void doShowNoMore() {
        view.onHideLoading();
        view.onShowNoMore();
    }

    private Observable<List<PushArticleDataBean>> getRandom() {
        int random = new Random().nextInt(10) + 5;
        Observable<List<PushArticleDataBean>> ob = RetrofitFactory.getRetrofit().create(ArticleApi.class)
                .getArticleList(random);
        return ob;
    }
}
