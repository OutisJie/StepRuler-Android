package com.example.ready.stepruler.binder.community;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ready.stepruler.R;
import com.example.ready.stepruler.api.CommentApi;
import com.example.ready.stepruler.api.CommunityApi;
import com.example.ready.stepruler.bean.comment.CommentBean;
import com.example.ready.stepruler.bean.community.CommunityBean;
import com.example.ready.stepruler.utils.RetrofitFactory;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;

import io.reactivex.functions.Consumer;
import me.drakeet.multitype.BuildConfig;
import me.drakeet.multitype.ItemViewBinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by single dog on 2017/12/28.
 */

public class CommunityMeViewBinder extends ItemViewBinder<CommunityBean,CommunityMeViewBinder.ViewHolder>{
    private static final String Tag="CommunityMeViewBinder";
    CommunityApi communityApi= RetrofitFactory.getRetrofit().create(CommunityApi.class);
    CommentApi commentApi=RetrofitFactory.getRetrofit().create(CommentApi.class);


    @Nonnull
    @Override
    protected CommunityMeViewBinder.ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent){
        View view=inflater.inflate(R.layout.item_community_hot,parent,false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull final CommunityMeViewBinder.ViewHolder holder, @NonNull final CommunityBean item) {
        final Context context = holder.itemView.getContext();

        Call<ArrayList<CommentBean>> getComments = commentApi.getComments(item.getCommunityId());
        getComments.enqueue(new Callback<ArrayList<CommentBean>>() {
            @Override
            public void onResponse(Call<ArrayList<CommentBean>> call, Response<ArrayList<CommentBean>> response) {
                ArrayList<CommentBean>comments= response.body();
                ArrayAdapter<CommentBean> arrayAdapter=new ArrayAdapter<>(context,R.layout.comment_item,comments);
                holder.listView.setAdapter(arrayAdapter);
            }

            @Override
            public void onFailure(Call<ArrayList<CommentBean>> call, Throwable t) {

            }
        });
        try {
            String tv_username = "张家浩";
            String tv_date = item.getCommunityDate();
            String tv_content = item.getCommunityText();

            holder.username.setText(tv_username);
            holder.time.setText(tv_date);
            holder.content.setText(tv_content);
            holder.numOfZan.setText(String.valueOf(item.getCommunityZan()));
            holder.numOfComment.setText(String.valueOf(item.getCommunityComment()));

            holder.zan.setOnClickListener(new View.OnClickListener() {
                boolean haszan = false;
                @Override
                public void onClick(View v) {
                    if (haszan==false) {
                        holder.zan.setImageResource(R.drawable.community_zan2);
                        haszan=true;
                        Call<Integer>dianzan=communityApi.dianzan(item.getCommunityId());
                        dianzan.enqueue(new Callback<Integer>() {
                            @Override
                            public void onResponse(Call<Integer> call, Response<Integer> response) {
                                item.setCommunityZan(response.body());
                                holder.numOfZan.setText(response.body().toString());
                            }
                            @Override
                            public void onFailure(Call<Integer> call, Throwable t) {

                            }
                        });
                    } else {
                        holder.zan.setImageResource(R.drawable.community_zan1);
                        haszan=false;
                        Call<Integer> quxiaozan = communityApi.quxiaozan(item.getCommunityId());
                        quxiaozan.enqueue(new Callback<Integer>() {
                            @Override
                            public void onResponse(Call<Integer> call, Response<Integer> response) {
                                item.setCommunityZan(response.body());
                                holder.numOfZan.setText(response.body().toString());
                            }
                            @Override
                            public void onFailure(Call<Integer> call, Throwable t) {

                            }
                        });
                    }
                }
            });
            RxView.clicks(holder.itemView)
                    .throttleFirst(1, TimeUnit.SECONDS)
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(Object o) throws Exception {
                            //EditorActivity.startActivity(context);
                        }
                    });
        } catch (Exception e) {
            if(BuildConfig.DEBUG){
                e.printStackTrace();
            }
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
        ListView listView;

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
            listView = (ListView) itemView.findViewById(R.id.lv_community_comment);
        }
    }
}
