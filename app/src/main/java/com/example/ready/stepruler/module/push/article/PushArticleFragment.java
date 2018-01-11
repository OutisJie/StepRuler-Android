package com.example.ready.stepruler.module.push.article;

import android.view.View;

import com.example.ready.stepruler.bean.end.LoadingBean;
import com.example.ready.stepruler.binder.BindItem;
import com.example.ready.stepruler.utils.DiffCallUtil;
import com.example.ready.stepruler.module.base.BaseListFragment;
import com.example.ready.stepruler.widget.listener.OnLoadMoreListener;

import java.util.List;

import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Created by ready on 2017/12/9.
 */

public class PushArticleFragment extends BaseListFragment<IPushArticleView.Presenter> implements IPushArticleView.View{

    public static PushArticleFragment newInstance() {
       return new PushArticleFragment();
    }
    @Override
    protected void initData() throws NullPointerException {
    }
    @Override
    protected void initView(final View view) {
        super.initView(view);
        adapter = new MultiTypeAdapter(oldItems);
        BindItem.registerPushArticleItem(adapter);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                //上拉刷新还没有结束时，不能加载
                if (canLoadMore) {
                    //加载时，设置不能加载或刷新
                    canLoadMore = false;
                    canRefresh = false;
                    presenter.doLoadMoreData();
                }
            }
        });

    }

    @Override
    public void fetchData() {
        super.fetchData();
        onloadData();
    }
    @Override
    public void onloadData() {
        onShowLoading();
        presenter.doLoadData();
    }
    @Override
    public void setPresenter(IPushArticleView.Presenter presenter) {
        if(presenter == null){
            this.presenter = new PushArticlePresenter(this);
        }
    }

    @Override
    public void onSetAdapter(List<?> list) {
        Items newItems = new Items(list);
        newItems.add(new LoadingBean());//添加末尾的加载动画
        DiffCallUtil.notifyDataSetChanged(oldItems, newItems, DiffCallUtil.ARTICLE, adapter);
        oldItems.clear();
        oldItems.addAll(newItems);
        //数据加载完，既可以刷新也可以加载
        canLoadMore = true;
        canRefresh = true;
    }
}
