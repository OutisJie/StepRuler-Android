package com.example.ready.stepruler.module.social;

import android.widget.Toast;

import com.example.ready.stepruler.R;
import com.example.ready.stepruler.MainActivity;
import com.example.ready.stepruler.api.UserApi;
import com.example.ready.stepruler.bean.user.UserBean;
import com.example.ready.stepruler.utils.AppManager;
import com.example.ready.stepruler.utils.RetrofitFactory;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ready on 2018/1/2.
 */

public class FriendsPresenter implements IFriendsView.Presenter {
    //参数
    private static final String TAG = "FriendsPresenter";
    private IFriendsView.View view;
    private List<UserBean> friends = new ArrayList<>();

    FriendsPresenter(IFriendsView.View view){
        this.view = view;
    }

    @Override
    public void doRefresh() {
        //刷新数据之前，把旧数据清空
        if (friends.size() != 0) {
            friends.clear();
        }
        view.onShowLoading();
        doLoadData();
    }

    @Override
    public void doLoadData() {
        if (MainActivity.getLoginState()) {
            Toast.makeText(AppManager.getAppManager().currentActivity(), "测试", Toast.LENGTH_SHORT).show();
            getFriends(MainActivity.getUser().getUserId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<List<UserBean>>() {
                        @Override
                        public void accept(List<UserBean> userBeans) throws Exception {
                            if (userBeans != null && userBeans.size() > 0) {
                                //有数据返回
                                doSetAdapter(userBeans);
                            } else {
                                //没有数据返回
                                doShowNoFriend();
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            //网络异常
                            doShowNetError();
                        }
                    });
        }else {
            doShowNoFriend();
        }
    }


    @Override
    public void doSetAdapter(List list) {
        friends.addAll(list);
        view.onSetAdapter(friends);
        view.onHideLoading();
    }

    @Override
    public void doShowNetError() {
        view.onHideLoading();
        Toast.makeText(AppManager.getAppManager().currentActivity(), R.string.network_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void doShowNoFriend() {
        view.onHideLoading();
        view.onShowNoFriend();
    }

    private Observable<List<UserBean>> getFriends(int user_id){
        Observable<List<UserBean>> ob = RetrofitFactory.getRetrofit().create(UserApi.class)
                .getFriends(user_id);
        return ob;
    }
}
