package com.example.ready.stepruler.module.base;

import com.trello.rxlifecycle2.LifecycleTransformer;

/**
 * Created by ready on 2017/12/9.
 */

public interface IBaseView<T> {
    //显示加载动画
    void onShowLoading();

    //隐藏加载动画
    void onHideLoading();

    //显示网络错误
    void onShowNetError();

    //设置presenter
    void setPresenter(T presenter);

    //绑定生命周期
    <T>LifecycleTransformer<T> bindToLife();
}
