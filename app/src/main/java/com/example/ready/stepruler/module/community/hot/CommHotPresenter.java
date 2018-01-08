package com.example.ready.stepruler.module.community.hot;

import android.support.annotation.NonNull;

import com.example.ready.stepruler.BuildConfig;
import com.example.ready.stepruler.bean.community.CommunityBean;
import com.example.ready.stepruler.api.CommunityApi;
import com.example.ready.stepruler.module.community.ICommunityView;
import com.example.ready.stepruler.utils.RetrofitFactory;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ready on 2017/12/9.
 */

public class CommHotPresenter implements ICommunityView.Presenter{
    private ICommunityView.View view;
    private static int page = 1;
    private List<CommunityBean> communityBeanList = new ArrayList<>();

    CommHotPresenter(ICommunityView.View view) {
        this.view = view;
    }

    @Override
    public void doLoadData() {
        //释放内存
        if(communityBeanList.size() > 150){
            communityBeanList.clear();
        }
        getPage(page)
                .subscribeOn(Schedulers.io())
                .map(new Function<List<CommunityBean>, List<CommunityBean>>() {
                    @Override
                    public List<CommunityBean> apply(@NonNull List<CommunityBean> list) throws Exception {
                        for (int i = 0; i < list.size() - 1; i++) {
                            for (int j = list.size() - 1; j > i; j--) {
                                if (list.get(j).getCommunityId() == list.get(i).getCommunityId()) {
                                    list.remove(j);
                                }
                            }
                        }
                        return list;
                    }
                })
                .compose(view.<List<CommunityBean>>bindToLife())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<CommunityBean>>() {
                    @Override
                    public void accept(List<CommunityBean> list) throws Exception {
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
                        if(BuildConfig.DEBUG){
                            throwable.printStackTrace();
                        }
                    }
                });
        page = page + 1;
    }



    @Override
    public void doLoadMoreData() {
        doLoadData();
    }

    @Override
    public void doSetAdapter(List<CommunityBean> list) {
        communityBeanList.addAll(list);
        view.onSetAdapter(communityBeanList);
        view.onHideLoading();
    }

    @Override
    public void doRefresh() {
        if(communityBeanList.size() != 0){
            communityBeanList.clear();
            page = 1;
        }
        view.onShowLoading();
        doLoadData();
    }

    @Override
    public void doShowNoMore() {
        view.onHideLoading();
        view.onShowNoMore();
    }

    @Override
    public void doShowNetError() {
        view.onHideLoading();
        view.onShowNetError();
    }

    private Observable<List<CommunityBean>> getPage(int page){
        Observable<List<CommunityBean>> ob = RetrofitFactory.getRetrofit().create(CommunityApi.class)
                .getHot(page);
        return ob;
    }
}
