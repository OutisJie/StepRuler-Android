package com.example.ready.stepruler.module.social;

import com.example.ready.stepruler.bean.user.UserBean;

import java.util.List;

/**
 * Created by ready on 2018/1/2.
 */

public interface IFriendsView {
    interface View  {
        //请求数据
        void onLoadData();

        /**
         * 显示加载动画
         */
        void onShowLoading();

        /**
         * 隐藏加载
         */
        void onHideLoading();

        void onShowNoFriend();

        /**
         * 设置适配器
         */
        void onSetAdapter(List<UserBean> list);
    }

    interface Presenter{
        //获取用户
        void doLoadData();

        //设置适配器
        void doSetAdapter(List<UserBean> list);

        //刷新数据
        void doRefresh();

        //显示网络错误
        void doShowNetError();

        void doShowNoFriend();
    }
}
