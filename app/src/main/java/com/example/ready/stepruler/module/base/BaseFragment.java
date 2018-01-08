package com.example.ready.stepruler.module.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.trello.rxlifecycle2.components.support.RxFragment;

/**
 * Created by ready on 2017/12/9.
 * 为所有的展示页面定义一个基类
 * 页面布局绑定
 * 初始化视图、数据
 * MVP:BaseFragment,
 *     IBaseView,
 *     IBasePresenter
 */

public abstract class BaseFragment<T extends IBasePresenter> extends RxFragment implements IBaseView<T>{
    protected  T presenter;
    //绑定布局文件
    protected abstract int attachLayoutId();
    //初始化视图
    protected abstract void initView(View view);
    //初始化数据
    protected abstract void initData() throws NullPointerException;
    //绑定生命周期
    @Override
    public <T1> LifecycleTransformer<T1> bindToLife() {
        return bindUntilEvent(FragmentEvent.DESTROY);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setPresenter(presenter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(attachLayoutId(),container,false);
        initData();
        initView(view);
        return view;
    }
}
