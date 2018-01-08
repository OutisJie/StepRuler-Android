package com.example.ready.stepruler.utils;

import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;

import com.example.ready.stepruler.bean.community.CommunityBean;
import com.example.ready.stepruler.bean.push.PushArticleDataBean;

import java.util.List;


public class DiffCallUtil extends DiffUtil.Callback {

    public static final int ARTICLE = 1;
    public static final int PHOTO = 2;
    public static final int HOT_COMMUNITY = 3;
    public static final int FRIENDS_COMMUNITY = 4;
    public static final int MY_COMMUNITY = 5;
    public static final int FRIENDS = 6;
    private List oldList, newList;
    private int type;

    public DiffCallUtil(List oldList, List newList, int type) {
        this.oldList = oldList;
        this.newList = newList;
        this.type = type;
    }

    public static void notifyDataSetChanged(List oldList, List newList, int type, RecyclerView.Adapter adapter) {
        DiffCallUtil diffCallUtil = new DiffCallUtil(oldList, newList, type);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(diffCallUtil, true);
        result.dispatchUpdatesTo(adapter);
    }

    @Override
    public int getOldListSize() {
        return oldList != null ? oldList.size() : 0;
    }

    @Override
    public int getNewListSize() {
        return newList != null ? newList.size() : 0;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        try {
            switch (type) {
                case ARTICLE:
                    return ((PushArticleDataBean) oldList.get(oldItemPosition)).getArticleTitle().equals(
                            ((PushArticleDataBean) newList.get(newItemPosition)).getArticleTitle());
                case HOT_COMMUNITY:
                    return ((CommunityBean) oldList.get(oldItemPosition)).getCommunityId() ==
                            ((CommunityBean) newList.get(newItemPosition)).getCommunityId();
                case FRIENDS_COMMUNITY:
                    return ((CommunityBean) oldList.get(oldItemPosition)).getCommunityId() ==
                            ((CommunityBean) newList.get(newItemPosition)).getCommunityId();
                case MY_COMMUNITY:
                    return ((CommunityBean) oldList.get(oldItemPosition)).getCommunityId() ==
                            ((CommunityBean) newList.get(newItemPosition)).getCommunityId();
            }
        } catch (Exception e) {
//            异常
        }
        return false;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        try {
            switch (type) {
                case ARTICLE:
                    return ((PushArticleDataBean) oldList.get(oldItemPosition)).getArticleTheme().equals(
                            ((PushArticleDataBean) newList.get(newItemPosition)).getArticleTheme());
                case HOT_COMMUNITY:
                    return ((CommunityBean) oldList.get(oldItemPosition)).getCommunityText().equals(
                            ((CommunityBean) newList.get(newItemPosition)).getCommunityText());
                case FRIENDS_COMMUNITY:
                    return ((CommunityBean) oldList.get(oldItemPosition)).getCommunityText().equals(
                            ((CommunityBean) newList.get(newItemPosition)).getCommunityText());
                case MY_COMMUNITY:
                    return ((CommunityBean) oldList.get(oldItemPosition)).getCommunityText().equals(
                            ((CommunityBean) newList.get(newItemPosition)).getCommunityText());
            }
        } catch (Exception e) {
//            异常
        }
        return false;
    }
}
