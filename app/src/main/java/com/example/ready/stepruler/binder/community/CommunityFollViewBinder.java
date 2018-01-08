package com.example.ready.stepruler.binder.community;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ready.stepruler.R;
import com.example.ready.stepruler.bean.community.CommunityBean;
import com.example.ready.stepruler.utils.AppManager;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;

import io.reactivex.functions.Consumer;
import me.drakeet.multitype.BuildConfig;
import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by single dog on 2017/12/30.
 */

public class CommunityFollViewBinder extends ItemViewBinder<CommunityBean,CommunityFollViewBinder.ViewHolder> {
    private static final String Tag = "CommunityFollViewBinder";

    @Nonnull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent){
        View view = inflater.inflate(R.layout.item_community_hot, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull CommunityBean item) {
        final Context context=holder.itemView.getContext();

        try {
            holder.username.setText(String.valueOf(item.getCommunityId()));
            holder.time.setText(item.getCommunityDate());
            holder.content.setText(item.getCommunityText());

            holder.zan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(AppManager.getAppManager().currentActivity(),"你好",Toast.LENGTH_SHORT).show();
                }
            });
            RxView.clicks(holder.itemView)
                    .throttleFirst(1, TimeUnit.SECONDS)
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(Object o) throws Exception{
                            // EditorActivity.startActivity(context);
                        }
                    });
        }catch (Exception e){
            print(e);
        }
    }

    private static void print(Throwable throwable){
        if(BuildConfig.DEBUG){
            throwable.printStackTrace();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView usericon;
        TextView username;
        TextView time;
        TextView content;
        ImageView zan;
        ImageView comment;
        TextView numOfZan;
        TextView numOfComment;
        public ViewHolder(View itemView){
            super(itemView);
            usericon=(ImageView)itemView.findViewById(R.id.iv_community_usericon);
            username=(TextView)itemView.findViewById(R.id.tv_community_username);
            time=(TextView)itemView.findViewById(R.id.tv_community_time);
            content=(TextView)itemView.findViewById(R.id.tv_community_content);
            zan= (ImageView) itemView.findViewById(R.id.iv_community_zan);
            comment= (ImageView) itemView.findViewById(R.id.iv_community_comment);
            numOfZan=(TextView)itemView.findViewById(R.id.tv_community_numOfZan);
            numOfComment=(TextView)itemView.findViewById(R.id.tv_community_numOfComment);
        }
    }
}
