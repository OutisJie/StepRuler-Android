package com.example.ready.stepruler.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ready.stepruler.R;
import com.example.ready.stepruler.adapter.adapterItem.CommentItem;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by single dog on 2018/1/4.
 */

public class MyCommentAdapter extends BaseAdapter {
    private ArrayList<HashMap<String, Object>> data;

    private LayoutInflater layoutInflater;
    private Context context;

    public MyCommentAdapter(Context context, ArrayList<HashMap<String, Object>> data) {
        this.context = context;
        this.data = data;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setData(ArrayList<HashMap<String, Object>> data) {
        this.data = data;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CommentItem commentItem = null;
        if (convertView == null) {
            commentItem = new CommentItem();
            convertView = layoutInflater.inflate(R.layout.comment_item, null);
            commentItem.username = (TextView) convertView.findViewById(R.id.tv_comment_username);
            commentItem.comment = (TextView) convertView.findViewById(R.id.tv_comment_text);
            convertView.setTag(commentItem);
        } else {
            commentItem = (CommentItem) convertView.getTag();
        }

        commentItem.username.setText((String) data.get(position).get("username"));
        commentItem.username.setTextSize(18);
        commentItem.username.getPaint().setFakeBoldText(true);
        commentItem.comment.setText((String) data.get(position).get("comment"));
        return convertView;
    }
}
