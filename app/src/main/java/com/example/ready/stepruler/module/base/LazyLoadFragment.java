package com.example.ready.stepruler.module.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by ready on 2017/12/9.
 * 继承自基类BaseFragment的基类
 * 在基类的基础上定义一些虚操作,简化加载
 */

public abstract class LazyLoadFragment<T extends IBasePresenter> extends BaseFragment<T>{
    protected boolean isViewInitiated;
    protected boolean isVisibleToUser;
    protected boolean isDataInitiated;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isViewInitiated = true;
        prepareFetchData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        prepareFetchData();
    }
    //显示当前页面，如果数据已有则不获取数据，否则重新获取数据
    private boolean prepareFetchData(boolean forceUpdate) {
        if(isVisibleToUser && isViewInitiated && (!isDataInitiated || forceUpdate)){
            fetchData();
            isDataInitiated = true;
            return true;
        }
        return false;
    }
    public boolean prepareFetchData(){
        return prepareFetchData(false);
    }
    public abstract void fetchData();
}
