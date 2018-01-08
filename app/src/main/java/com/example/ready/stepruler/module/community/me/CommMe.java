package com.example.ready.stepruler.module.community.me;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

import com.example.ready.stepruler.R;
import com.example.ready.stepruler.bean.LoadingBean;
import com.example.ready.stepruler.binder.BindItem;
import com.example.ready.stepruler.binder.DiffCallback;
import com.example.ready.stepruler.module.base.BaseListFragment;
import com.example.ready.stepruler.module.community.ICommunityView;
import com.example.ready.stepruler.module.community.editor.EditCommunityActivity;
import com.example.ready.stepruler.widget.OnLoadMoreListener;

import java.util.List;

import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Created by single dog on 2017/12/28.
 */

public class CommMe extends BaseListFragment<ICommunityView.Presenter> implements ICommunityView.View {
    private static final String Tag = "CommunityMe";
    //浮动按钮
    private FloatingActionButton  addCommunity;
    public static CommMe newInstance() {
        return new CommMe();
    }

    @Override
    protected int attachLayoutId() {
        super.attachLayoutId();
        return R.layout.fragment_community_me;
    }

    @Override
    public void setPresenter(ICommunityView.Presenter presenter) {
        if (presenter == null)
            this.presenter = new CommMePresenter(this);
    }

    @Override
    protected void initData() throws NullPointerException {
    }

    @Override
    protected void initView(final View view) {
        super.initView(view);
        adapter = new MultiTypeAdapter(oldItems);
        BindItem.registerCommunityMeItem(adapter);
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

        addCommunity=(FloatingActionButton)view.findViewById(R.id.add_community);
        addCommunity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(view.getContext(), EditCommunityActivity.class);
                view.getContext().startActivity(intent);
            }
        });


    }

    @Override
    public void onloadData() {
        //记录请求的页
        onShowLoading();
        presenter.doLoadData();
    }

    @Override
    public void fetchData() {
        super.fetchData();
        onloadData();
    }

    @Override
    public void onSetAdapter(List<?> list) {
        Items newItems = new Items(list);
        newItems.add(new LoadingBean());
        DiffCallback.notifyDataSetChanged(oldItems, newItems, DiffCallback.MY_COMMUNITY, adapter);
        oldItems.clear();
        oldItems.addAll(newItems);
        canLoadMore = true;
        canRefresh = true;
    }
}
