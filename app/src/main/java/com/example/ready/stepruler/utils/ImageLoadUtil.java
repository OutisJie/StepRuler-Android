package com.example.ready.stepruler.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;


public class ImageLoadUtil {

    public static void loadCenterCrop(Context context, String url, ImageView view, int defaultResId) {
        if (!NetWorkUtil.isMobileConnected(context)) {
            view.setImageResource(defaultResId);
        } else {
            RequestOptions options = new RequestOptions().centerCrop();
            Glide.with(context).load(url).transition(DrawableTransitionOptions.withCrossFade()).apply(options).into(view);
        }
    }

    /**
     * 带加载异常图片
     */
    public static void loadCenterCrop(Context context, String url, ImageView view, int defaultResId, int errorResId) {
        if (!NetWorkUtil.isMobileConnected(context)) {
            view.setImageResource(defaultResId);
        } else {
            RequestOptions options = new RequestOptions().centerCrop().error(errorResId);
            Glide.with(context).load(url).transition(DrawableTransitionOptions.withCrossFade()).apply(options).into(view);
        }
    }

    /**
     * 带监听处理
     */
    public static void loadCenterCrop(Context context, String url, ImageView view, RequestListener listener) {
        RequestOptions options = new RequestOptions().centerCrop();
        Glide.with(context).load(url).transition(DrawableTransitionOptions.withCrossFade()).listener(listener).into(view);
    }
}
