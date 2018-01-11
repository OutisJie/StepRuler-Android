package com.example.ready.stepruler.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVPush;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.SendCallback;
import com.example.ready.stepruler.R;
import com.example.ready.stepruler.MainActivity;
import com.example.ready.stepruler.api.UserApi;
import com.example.ready.stepruler.adapter.adapterItem.UserItem;
import com.example.ready.stepruler.utils.RetrofitFactory;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by single dog on 2018/1/3.
 */

public class MyAdapter extends BaseAdapter {
    private ArrayList<HashMap<String, Object>> data;
    /**
     * LayoutInflater 类是代码实现中获取布局文件的主要形式
     * LayoutInflater layoutInflater = LayoutInflater.from(context);
     * View convertView = layoutInflater.inflate();
     * LayoutInflater的使用,在实际开发种LayoutInflater这个类还是非常有用的,它的作用类似于 findViewById(),
     * 不同点是LayoutInflater是用来找layout下xml布局文件，并且实例化！
     * 而findViewById()是找具体xml下的具体 widget控件(如:Button,TextView等)。
     */
    private LayoutInflater layoutInflater;
    private Context context;

    //添加好友的网络请求

    public MyAdapter(Context context, ArrayList<HashMap<String, Object>> data) {
        this.context = context;
        this.data = data;
        this.layoutInflater = LayoutInflater.from(context);
    }

    /**
     * 获取列数
     */
    @Override
    public int getCount() {
        return data.size();
    }

    /**
     * 获取某一位置的数据
     */
    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    /**
     * 获取唯一标识
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * android绘制每一列的时候，都会调用这个方法
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        UserItem userItem = null;
        if (convertView == null) {
            userItem = new UserItem();
            //获取组件布局
            convertView = layoutInflater.inflate(R.layout.search_item, null);
            userItem.textView = (TextView) convertView.findViewById(R.id.tv_search_username);
            userItem.button = (Button) convertView.findViewById(R.id.bt_search_addUser);
            //这里要注意，是使用tag来存储数据的
            convertView.setTag(userItem);
        } else {
            userItem = (UserItem) convertView.getTag();
        }

        //绑定数据、以及事件触发
        final String username = (String) data.get(position).get("username");
        userItem.textView.setText(username);
        userItem.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.getLoginState() == true) {
                    Call<String> call = RetrofitFactory.getRetrofit().create(UserApi.class).getDevice(username, MainActivity.getUser().getUserId());

                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if (response.body() != null && !response.body().equals("-1") && !response.body().equals("-2")) {
                                String channel = username;
                                String message = "你收到一份好友请求，来自：" + MainActivity.getUser().getUserName();

                                //发送添加好友的请求
                                AVPush push = new AVPush();

                                AVQuery<AVInstallation> query = AVInstallation.getQuery();
                                query.whereEqualTo("installationId", response.body().replace("\"", ""));
                                push.setQuery(query);
                                push.setChannel(channel.trim());
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("action", "com.invitation.action");
                                jsonObject.put("alert", message.trim());
                                jsonObject.put("id", String.valueOf(MainActivity.getUser().getUserId()).trim());

                                push.setData(jsonObject);
                                push.setPushToAndroid(true);
                                // 推送
                                push.sendInBackground(new SendCallback() {
                                    @Override
                                    public void done(AVException e) {
                                        Toast toast = null;
                                        if (e == null) {
                                            toast = Toast.makeText(context, "发送成功.", Toast.LENGTH_SHORT);
                                        } else {
                                            toast = Toast.makeText(context, "发送失败:" + e.getMessage(), Toast.LENGTH_LONG);
                                        }
                                        // 放心大胆地show，我们保证 callback 运行在 UI 线程。
                                        toast.show();
                                    }
                                });
                            } else if (response.body().equals("-1")) {
                                Toast.makeText(context, "你们已经是好友啦！", Toast.LENGTH_SHORT).show();
                            } else if (response.body().equals("-2")) {
                                Toast.makeText(context, "此用户没有验证！", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(context, "请先登录！", Toast.LENGTH_SHORT).show();
                }
            }
        });
        if (username.equals("没有查询到该数据")) userItem.button.setVisibility(View.INVISIBLE);
        return convertView;
    }
}
