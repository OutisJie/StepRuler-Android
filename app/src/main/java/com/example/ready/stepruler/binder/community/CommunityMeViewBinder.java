package com.example.ready.stepruler.binder.community;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ready.stepruler.R;
import com.example.ready.stepruler.MainActivity;
import com.example.ready.stepruler.adapter.MyCommentAdapter;
import com.example.ready.stepruler.api.CommentApi;
import com.example.ready.stepruler.api.CommunityApi;
import com.example.ready.stepruler.api.UserApi;
import com.example.ready.stepruler.bean.comment.CommentBean;
import com.example.ready.stepruler.bean.community.CommunityBean;
import com.example.ready.stepruler.module.community.me.CommMeFragment;
import com.example.ready.stepruler.utils.ImageLoadUtil;
import com.example.ready.stepruler.utils.RetrofitFactory;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

public class CommunityMeViewBinder extends ItemViewBinder<CommunityBean, CommunityMeViewBinder.ViewHolder> {
    private static final String Tag = "CommunityMeViewBinder";
    CommunityApi communityApi = RetrofitFactory.getRetrofit().create(CommunityApi.class);
    CommentApi commentApi = RetrofitFactory.getRetrofit().create(CommentApi.class);
    UserApi userApi=RetrofitFactory.getRetrofit().create(UserApi.class);

    @Nonnull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_community_hot, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull final CommunityBean item) {
        final Context context = holder.itemView.getContext();

        try {
            String tv_username = MainActivity.getUser().getUserName();
            String tv_date = item.getCommunityDate();
            String tv_content = item.getCommunityText();
            holder.username.setText(tv_username);
            holder.time.setText(tv_date);
            holder.content.setText(tv_content);

            holder.numOfZan.setText(String.valueOf(item.getCommunityZan()));

            //绑定用户名
            Call<List<String>> getUserInfo=userApi.getUserInfo(item.getUserId());
            getUserInfo.enqueue(new Callback<List<String>>() {
                @Override
                public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                    holder.username.setText(response.body().get(0).toString());
                    ImageLoadUtil.loadCenterCrop(context, response.body().get(1).toString(), holder.usericon, R.color.viewBackground, R.drawable.ice_user_header);
                }

                @Override
                public void onFailure(Call<List<String>> call, Throwable t) {

                }
            });

            //点赞功能
            holder.zan.setOnClickListener(new View.OnClickListener() {
                boolean haszan = false;

                @Override
                public void onClick(View v) {
                    if (haszan == false) {
                        holder.zan.setImageResource(R.drawable.community_zan2);
                        haszan = true;
                        Call<Integer> dianzan = communityApi.dianzan(item.getCommunityId());
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
                        haszan = false;
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

            //评论功能
            holder.comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //显示评论框
                    holder.linearLayout.setVisibility(View.VISIBLE);
                    //弹出输入法
                    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                }
            });
            holder.hideDown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //隐藏评论框
                    holder.linearLayout.setVisibility(View.GONE);
                    //隐藏输入法
                    InputMethodManager im = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    im.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                }
            });

            //发送评论
            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //添加评论
                    Call<ArrayList<CommentBean>> addComment = commentApi.addComment(item.getCommunityId(), holder.editText.getText().toString(), MainActivity.getUser().getUserName());
                    addComment.enqueue(new Callback<ArrayList<CommentBean>>() {
                        @Override
                        public void onResponse(Call<ArrayList<CommentBean>> call, Response<ArrayList<CommentBean>> response) {
                            //评论显示
                            Call<ArrayList<CommentBean>> getCommentsA = commentApi.getComments(item.getCommunityId());
                            getCommentsA.enqueue(new Callback<ArrayList<CommentBean>>() {
                                @Override
                                public void onResponse(Call<ArrayList<CommentBean>> call, Response<ArrayList<CommentBean>> response) {
                                    if (response == null) {
                                    } else {
                                        ArrayList<CommentBean> comments = response.body();
                                        ArrayList<HashMap<String, Object>> data = new ArrayList<>();
                                        for (int i = 0; i < comments.size(); i++) {
                                            HashMap<String, Object> hashMap = new HashMap<>();
                                            hashMap.put("username", comments.get(i).getUserName());
                                            hashMap.put("comment", comments.get(i).getCommentText());
                                            data.add(hashMap);
                                        }
                                        MyCommentAdapter myCommentAdapter = new MyCommentAdapter(context, data);
                                        holder.listView.setAdapter(myCommentAdapter);
                                        ViewGroup.LayoutParams params = holder.listView.getLayoutParams();
                                        params.height = comments.size() * 75;
                                        holder.listView.setLayoutParams(params);
                                    }
                                }

                                @Override
                                public void onFailure(Call<ArrayList<CommentBean>> call, Throwable t) {
                                    t.printStackTrace();
                                }
                            });
                        }

                        @Override
                        public void onFailure(Call<ArrayList<CommentBean>> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
                    //隐藏评论框
                    holder.linearLayout.setVisibility(View.GONE);
                    //隐藏输入法
                    InputMethodManager im = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    im.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    Toast.makeText(context, "评论成功", Toast.LENGTH_SHORT).show();
                }
            });

            //评论显示
            Call<ArrayList<CommentBean>> getcomments=commentApi.getComments(item.getCommunityId());
            getcomments.enqueue(new Callback<ArrayList<CommentBean>>() {
                @Override
                public void onResponse(Call<ArrayList<CommentBean>> call, Response<ArrayList<CommentBean>> response) {
                    if (response.body() == null) {
                    } else {
                        ArrayList<CommentBean> comments = response.body();
                        ArrayList<HashMap<String, Object>> data = new ArrayList<>();
                        for (int i = 0; i < comments.size(); i++) {
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("username", comments.get(i).getUserName());
                            hashMap.put("comment", comments.get(i).getCommentText());
                            data.add(hashMap);
                        }
                        MyCommentAdapter myCommentAdapter = new MyCommentAdapter(context, data);
                        holder.listView.setAdapter(myCommentAdapter);
                        ViewGroup.LayoutParams params = holder.listView.getLayoutParams();
                        params.height = comments.size() * 75;
                        holder.listView.setLayoutParams(params);
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<CommentBean>> call, Throwable t) {

                }
            });

            //删除说说
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(context,
                            holder.delete, Gravity.END, 0, R.style.MyPopupMenu);
                    popupMenu.inflate(R.menu.menu_delete);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menu) {
                            int itemId = menu.getItemId();
                            if (itemId == R.id.action_delete) {
                                Call<List<CommunityBean>> delete = communityApi.delete(item.getCommunityId(), MainActivity.getUser().getUserId());
                                delete.enqueue(new Callback<List<CommunityBean>>() {
                                    @Override
                                    public void onResponse(Call<List<CommunityBean>> call, Response<List<CommunityBean>> response) {
                                        if (response.body() != null) {
                                            CommMeFragment.getInstace().update(response.body());
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<List<CommunityBean>> call, Throwable t) {
                                        t.printStackTrace();
                                    }
                                });
                                Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
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
                            //EditorActivity.startActivity(context);
                        }
                    });
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace();
            }
        }
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        //说说控件
        ImageView usericon;
        TextView username;
        TextView time;
        TextView content;
        ImageView delete;

        //显示评论控件
        ImageView zan;
        ImageView comment;
        TextView numOfZan;
        //        TextView numOfComment;
        ListView listView;

        //评论功能控件
        LinearLayout linearLayout;
        LinearLayout hideDown;
        EditText editText;
        Button button;

        public ViewHolder(View itemView) {
            super(itemView);
            usericon = (ImageView) itemView.findViewById(R.id.iv_community_usericon);
            username = (TextView) itemView.findViewById(R.id.tv_community_username);
            time = (TextView) itemView.findViewById(R.id.tv_community_time);
            content = (TextView) itemView.findViewById(R.id.tv_community_content);
            zan = (ImageView) itemView.findViewById(R.id.iv_community_zan);
            comment = (ImageView) itemView.findViewById(R.id.iv_community_comment);
            numOfZan = (TextView) itemView.findViewById(R.id.tv_community_numOfZan);
//            numOfComment = (TextView) itemView.findViewById(R.id.tv_community_numOfComment);
            listView = (ListView) itemView.findViewById(R.id.lv_community_comment);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.lt_community_comment);
            hideDown = (LinearLayout) itemView.findViewById(R.id.bt_community_hideDown);
            editText = (EditText) itemView.findViewById(R.id.et_community_editText);
            button = (Button) itemView.findViewById(R.id.bt_community_editComplete);
            delete = (ImageView) itemView.findViewById(R.id.iv_community_delete);
        }
    }
}
