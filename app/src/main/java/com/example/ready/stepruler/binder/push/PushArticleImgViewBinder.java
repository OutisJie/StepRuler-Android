package com.example.ready.stepruler.binder.push;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ready.stepruler.BuildConfig;
import com.example.ready.stepruler.R;
import com.example.ready.stepruler.bean.push.PushArticleDataBean;
import com.example.ready.stepruler.module.push.content.ArticleContentActivity;
import com.example.ready.stepruler.utils.ImageLoadUtil;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;
import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by ready on 2018/1/2.
 */

public class PushArticleImgViewBinder extends ItemViewBinder<PushArticleDataBean, PushArticleImgViewBinder.ViewHolder> {
    private static final String TAG = "PushArticleImgViewBinder";

    @NonNull
    @Override
    protected PushArticleImgViewBinder.ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_push_article_img, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull final PushArticleImgViewBinder.ViewHolder holder, @NonNull final PushArticleDataBean item) {
        final Context context = holder.itemView.getContext();

        try {
            //图片
            final String imgUrl = item.getArticleImg();
            if (!TextUtils.isEmpty(imgUrl)) {
                ImageLoadUtil.loadCenterCrop(context, imgUrl, holder.iv_image, R.color.viewBackground);
            }

            String tv_title = item.getArticleTitle();
            String tv_abstract = item.getArticleTheme();
            String tv_source = "作者:" + item.getArticleAuthor();
            String tv_datetime = item.getArticleDate();

            holder.tv_title.setText(tv_title);
            holder.tv_abstract.setText(tv_abstract);
            holder.tv_extra.setText(tv_source + " - " + tv_datetime);
            holder.iv_dots.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(context,
                            holder.iv_dots, Gravity.END, 0, R.style.MyPopupMenu);
                    popupMenu.inflate(R.menu.menu_share);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menu) {
                            int itemId = menu.getItemId();
                            if (itemId == R.id.action_share) {
                            }
                            return false;
                        }
                    });
                    popupMenu.show();
                }
            });
            RxView.clicks(holder.itemView)
                    .throttleFirst(1, TimeUnit.SECONDS)
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(Object o) throws Exception {
                            //点击单个item跳转
                            ArticleContentActivity.lunch(item , imgUrl);
                        }
                    });

        } catch (Exception e){
            if(BuildConfig.DEBUG){
                e.printStackTrace();
            }
        }

    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_extra;
        private ImageView iv_dots;
        private ImageView iv_image;
        private TextView tv_title;
        private TextView tv_abstract;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_extra = (TextView) itemView.findViewById(R.id.tv_extra);
            iv_dots = (ImageView) itemView.findViewById(R.id.iv_dots);
            iv_image = (ImageView) itemView.findViewById(R.id.iv_image);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_abstract = (TextView) itemView.findViewById(R.id.tv_abstract);
        }
    }
}
