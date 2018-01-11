package com.example.ready.stepruler.module.community.me;

import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.Toast;

import com.example.ready.stepruler.MainActivity;
import com.example.ready.stepruler.R;
import com.example.ready.stepruler.bean.end.LoadingBean;
import com.example.ready.stepruler.bean.end.LoadingEndBean;
import com.example.ready.stepruler.binder.BindItem;
import com.example.ready.stepruler.module.base.BaseListFragment;
import com.example.ready.stepruler.module.community.ICommunityView;
import com.example.ready.stepruler.module.community.editor.EditCommunityActivity;
import com.example.ready.stepruler.widget.listener.OnLoadMoreListener;

import java.util.List;

import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Created by single dog on 2017/12/28.
 */

public class CommMeFragment extends BaseListFragment<ICommunityView.Presenter> implements ICommunityView.View {
    private static final String Tag = "CommunityMe";
    private FloatingActionButton addCommunity;

    public static CommMeFragment newInstance() {
        instance = new CommMeFragment();
        return instance;
    }

    @Override
    protected int attachLayoutId() {
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

        addCommunity = (FloatingActionButton) view.findViewById(R.id.add_community);
        addCommunity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.getLoginState() == true) {
                    EditCommunityActivity.startActivity(getContext());
                } else {
                    Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT).show();
                }
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
        if (newItems.size() > 6) {
            newItems.add(new LoadingBean());
        } else {
            newItems.add(new LoadingEndBean());
        }
        // DiffCallUtil.notifyDataSetChanged(oldItems, newItems, DiffCallUtil.MY_COMMUNITY, adapter);
        adapter.setItems(newItems);
        adapter.notifyDataSetChanged();
        oldItems.clear();
        oldItems.addAll(newItems);
        canLoadMore = true;
        canRefresh = true;
    }

    private static CommMeFragment instance;

    public static CommMeFragment getInstace() {
        if (instance == null) {
            return new CommMeFragment();
        }
        return instance;
    }

    public void update(List<?> list) {
        Items newItems = new Items(list);
        if (newItems.size() > 6) {
            newItems.add(new LoadingBean());
        } else {
            newItems.add(new LoadingEndBean());
        }
        adapter.setItems(newItems);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        super.onRefresh();

    }
}
