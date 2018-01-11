package com.example.ready.stepruler.module.social;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ready.stepruler.R;
import com.example.ready.stepruler.MainActivity;
import com.example.ready.stepruler.bean.user.UserBean;
import com.example.ready.stepruler.binder.friends.FriendsViewBinder;
import com.example.ready.stepruler.widget.LetterWidget;

import java.util.ArrayList;
import java.util.List;

import cn.leancloud.chatkit.view.LCIMDividerItemDecoration;
import de.greenrobot.event.EventBus;

/**
 * A placeholder fragment containing a simple view.
 */
public class FriendsFragment extends Fragment implements IFriendsView.View {

    private static final String TAG = "FriendsFragment";
    //控件
    TextView tv_friends_desc;
    protected RecyclerView recyclerView;
    protected SwipeRefreshLayout swipeRefreshLayout;
    //参数
    private List<UserBean> friendLists = new ArrayList<>();
    private FriendsViewBinder itemAdapter;
    private FriendsPresenter friendsPresenter;
    LinearLayoutManager layoutManager;

    public static FriendsFragment newInstance(){
        return new FriendsFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        tv_friends_desc = (TextView) view.findViewById(R.id.tv_friends_desc);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.refresh_layout);
        layoutManager = new LinearLayoutManager(getActivity());
        setPresenter();
        initView();
        EventBus.getDefault().register(this);
        return view;
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        friendsPresenter.doRefresh();
    }

    private void initView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        if(MainActivity.getLoginState()) {
            tv_friends_desc.setVisibility(View.GONE);
            recyclerView.addItemDecoration(new LCIMDividerItemDecoration(getActivity()));
            itemAdapter = new FriendsViewBinder();
            recyclerView.setAdapter(itemAdapter);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    friendsPresenter.doRefresh();
                }
            });
        }else {
            tv_friends_desc.setVisibility(View.VISIBLE);
        }
    }

    public void setPresenter() {
        if(friendsPresenter == null){
            this.friendsPresenter = new FriendsPresenter(this);
        }
    }

    @Override
    public void onSetAdapter(List<UserBean> list) {
        friendLists.clear();
        friendLists.addAll(list);
        UserProvider.getInstance().addUsers(friendLists);
        itemAdapter.setFriendList(UserProvider.getInstance().getAllUsers());
        itemAdapter.notifyDataSetChanged();
        onHideLoading();
        tv_friends_desc.setVisibility(View.GONE);
    }


    @Override
    public void onLoadData() {
        onShowLoading();
        friendsPresenter.doLoadData();
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
    public void onShowNoFriend() {
        tv_friends_desc.setVisibility(View.VISIBLE);
    }

    /**
     * 处理 LetterView 发送过来的 MemberLetterEvent
     * 会通过 MembersAdapter 获取应该要跳转到的位置，然后跳转
     */
    public void onEvent(LetterWidget.MemberLetterEvent event) {
        Character targetChar = Character.toLowerCase(event.letter);
        if (itemAdapter.getIndexMap().containsKey(targetChar)) {
            int index = itemAdapter.getIndexMap().get(targetChar);
            if (index > 0 && index < itemAdapter.getItemCount()) {
                layoutManager.scrollToPositionWithOffset(index, 0);
            }
        }
    }
}
