package com.example.ready.stepruler.binder;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.example.ready.stepruler.bean.community.CommunityBean;
import com.example.ready.stepruler.bean.diary.DiaryBean;
import com.example.ready.stepruler.bean.end.LoadingBean;
import com.example.ready.stepruler.bean.end.LoadingEndBean;
import com.example.ready.stepruler.bean.push.PushArticleDataBean;
import com.example.ready.stepruler.binder.community.CommunityMeViewBinder;
import com.example.ready.stepruler.binder.community.CommunityViewBinder;
import com.example.ready.stepruler.binder.diary.DiaryViewBinder;
import com.example.ready.stepruler.binder.end.LoadingEndViewBinder;
import com.example.ready.stepruler.binder.end.LoadingViewBinder;
import com.example.ready.stepruler.binder.push.PushArticleImgViewBinder;
import com.example.ready.stepruler.binder.push.PushArticleTextViewBinder;

import javax.annotation.Nonnull;

import me.drakeet.multitype.ClassLinker;
import me.drakeet.multitype.ItemViewBinder;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Created by ready on 2017/12/10.
 */

public class BindItem {
    //主页推送文章
    public static void registerPushArticleItem(@Nonnull MultiTypeAdapter adapter) {
        //一个类型对应多个ItemViewBinder
        adapter.register(PushArticleDataBean.class)
                .to(new PushArticleTextViewBinder(), new PushArticleImgViewBinder())
                .withClassLinker(new ClassLinker<PushArticleDataBean>() {
                    @NonNull
                    @Override
                    public Class<? extends ItemViewBinder<PushArticleDataBean, ?>> index(@NonNull PushArticleDataBean item) {
                        if (item.getArticleImg() != null || !TextUtils.isEmpty(item.getArticleImg())) {
                            return PushArticleImgViewBinder.class;
                        }
                        return PushArticleTextViewBinder.class;
                    }
                });
        adapter.register(LoadingBean.class, new LoadingViewBinder());
        adapter.register(LoadingEndBean.class, new LoadingEndViewBinder());
    }

    //日记
    public static void registerDiaryItem(@NonNull MultiTypeAdapter adapter) {
        adapter.register(DiaryBean.class)
                .to(new DiaryViewBinder())
                .withClassLinker(new ClassLinker<DiaryBean>() {
                    @NonNull
                    @Override
                    public Class<? extends ItemViewBinder<DiaryBean, ?>> index(@NonNull DiaryBean item) {
                        return DiaryViewBinder.class;
                    }
                });
        adapter.register(LoadingBean.class, new LoadingViewBinder());
        adapter.register(LoadingEndBean.class, new LoadingEndViewBinder());
    }

    //社区推送热门文章
    public static void registerCommunityHotItem(@Nonnull MultiTypeAdapter adapter) {
        adapter.register(CommunityBean.class)
                .to(new CommunityViewBinder())
                .withClassLinker(new ClassLinker<CommunityBean>() {
                    @NonNull
                    @Override
                    public Class<? extends ItemViewBinder<CommunityBean, ?>> index(@NonNull CommunityBean communityBean) {
                        return CommunityViewBinder.class;
                    }
                });
        adapter.register(LoadingBean.class, new LoadingViewBinder());
        adapter.register(LoadingEndBean.class, new LoadingEndViewBinder());
    }

    //社区推送我的关注
    public static void registerCommunityFollItem(@Nonnull MultiTypeAdapter adapter) {
        adapter.register(CommunityBean.class)
                .to(new CommunityViewBinder())
                .withClassLinker(new ClassLinker<CommunityBean>() {
                    @NonNull
                    @Override
                    public Class<? extends ItemViewBinder<CommunityBean, ?>> index(@NonNull CommunityBean communityBean) {
                        return CommunityViewBinder.class;
                    }
                });
        adapter.register(LoadingBean.class, new LoadingViewBinder());
        adapter.register(LoadingEndBean.class, new LoadingEndViewBinder());
    }

    //社区我的文章
    public static void registerCommunityMeItem(@Nonnull MultiTypeAdapter adapter) {
        adapter.register(CommunityBean.class)
                .to(new CommunityMeViewBinder())
                .withClassLinker(new ClassLinker<CommunityBean>() {
                    @NonNull
                    @Override
                    public Class<? extends ItemViewBinder<CommunityBean, ?>> index(@NonNull CommunityBean communityBean) {
                        return CommunityMeViewBinder.class;
                    }
                });
        adapter.register(LoadingBean.class, new LoadingViewBinder());
        adapter.register(LoadingEndBean.class, new LoadingEndViewBinder());
    }
}
