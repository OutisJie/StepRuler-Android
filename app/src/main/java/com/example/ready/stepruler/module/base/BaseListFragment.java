package com.example.ready.stepruler.module.base;

import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.ready.stepruler.R;
import com.example.ready.stepruler.RxBus;
import com.example.ready.stepruler.bean.end.LoadingEndBean;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Created by ready on 2017/12/9.
 * 继承自基类BaseFragment和LazyLoadFragment
 * 绑定视图
 * 初始化
 */

public abstract class BaseListFragment<T extends IBasePresenter> extends LazyLoadFragment<T> implements IBaseListView<T>, SwipeRefreshLayout.OnRefreshListener {
    public static final String TAG = "BaseListFragment";
    protected RecyclerView recyclerView;
    protected SwipeRefreshLayout swipeRefreshLayout;
    protected MultiTypeAdapter adapter;
    protected Items oldItems = new Items();
    protected boolean canLoadMore = false;
    protected boolean canRefresh = false;
    protected Observable<Integer> observable;

    @Override
    protected int attachLayoutId() {
        return R.layout.fragment_list;
    }

    @Override
    protected void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onShowLoading() {
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });
    }

    @Override
    public void onHideLoading() {
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void fetchData() {
        observable = RxBus.getInstance().register(BaseListFragment.TAG);
        observable.subscribe(new Consumer<Integer>() {
            @Override
            public void accept(@NonNull Integer integer) throws Exception {
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onShowNetError() {
        Toast.makeText(getActivity(), R.string.network_error, Toast.LENGTH_SHORT).show();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //网络异常时，恢复原有的状态
                if(oldItems.size() > 1){
                    Items newItems = new Items(oldItems);
                    newItems.remove(newItems.size() - 1);//将末尾的加载动画去掉
                    adapter.setItems(newItems);
                    adapter.notifyDataSetChanged();
                }else if(oldItems.size() == 0){
                    oldItems.add(new LoadingEndBean());//显示暂无更多内容
                    adapter.setItems(oldItems);
                    adapter.notifyDataSetChanged();
                }
                //就算网络异常，讲道理我也能继续再刷新的
                canLoadMore = false;
                canRefresh = true;
            }
        });
    }

    @Override
    public void onShowNoMore() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(oldItems.size() > 0){
                    Items newItems = new Items(oldItems);
                    newItems.remove(newItems.size() - 1);
                    newItems.add(new LoadingEndBean());//显示暂无更多内容
                    adapter.setItems(newItems);
                    adapter.notifyDataSetChanged();
                }else if(oldItems.size() == 0){
                    oldItems.add(new LoadingEndBean());
                    adapter.setItems(oldItems);
                    adapter.notifyDataSetChanged();
                }
                canLoadMore = false;
                canRefresh = true;
            }
        });
    }
    @Override
    public void onRefresh() {
        int firstVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
        if (firstVisibleItemPosition == 0 && canRefresh) {
            canLoadMore = false;
            canRefresh = false;
            presenter.doRefresh();
            return;
        }
        recyclerView.scrollToPosition(5);
        recyclerView.smoothScrollToPosition(0);
    }
    @Override
    public void onDestroy() {
        RxBus.getInstance().unregister(BaseListFragment.TAG,observable);
        super.onDestroy();
    }
}
