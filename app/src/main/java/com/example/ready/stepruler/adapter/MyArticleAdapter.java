package com.example.ready.stepruler.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ready.stepruler.R;
import com.example.ready.stepruler.adapter.adapterItem.ArticleItem;
import com.example.ready.stepruler.bean.push.PushArticleDataBean;
import com.example.ready.stepruler.module.push.content.ArticleContentActivity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by single dog on 2018/1/4.
 */

public class MyArticleAdapter extends BaseAdapter{
    private ArrayList<HashMap<String,Object>> data;
    /**
     * LayoutInflater 类是代码实现中获取布局文件的主要形式
     *LayoutInflater layoutInflater = LayoutInflater.from(context);
     *View convertView = layoutInflater.inflate();
     *LayoutInflater的使用,在实际开发种LayoutInflater这个类还是非常有用的,它的作用类似于 findViewById(),
     不同点是LayoutInflater是用来找layout下xml布局文件，并且实例化！
     而findViewById()是找具体xml下的具体 widget控件(如:Button,TextView等)。
     */
    private LayoutInflater layoutInflater;
    private Context context;

    public MyArticleAdapter(Context context,ArrayList<HashMap<String,Object>> data){
        this.context=context;
        this.data=data;
        this.layoutInflater=LayoutInflater.from(context);
    }
    /**
     *获取列数
     */
    @Override
    public int getCount(){
        return data.size();
    }
    /**
     *获取某一位置的数据
     */
    @Override
    public Object getItem(int position) {
        return data.get(position);
    }
    /**
     *获取唯一标识
     */
    @Override
    public long getItemId(int position) {
        return position;
    }
    /**
     * android绘制每一列的时候，都会调用这个方法
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        ArticleItem articleItem=null;
        if(convertView==null){
            articleItem=new ArticleItem();
            //获取组件布局
            convertView=layoutInflater.inflate(R.layout.search_article_item,null);
            articleItem.title=(TextView)convertView.findViewById(R.id.search_article_title);
            articleItem.subtitle=(TextView)convertView.findViewById(R.id.search_article_subtitle);
            articleItem.linearLayout=(LinearLayout)convertView.findViewById(R.id.search_article_layout);
            //这里要注意，是使用tag来存储数据的
            convertView.setTag(articleItem);
        }else {
            articleItem=(ArticleItem)convertView.getTag();
        }

        //绑定数据，以及事件触发
        articleItem.title.setText((String)data.get(position).get("article_title"));
        articleItem.subtitle.setText((String)data.get(position).get("article_subtitle"));
        articleItem.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PushArticleDataBean item = new PushArticleDataBean();
                item.setArticleTitle((String)data.get(position).get("article_title"));
                item.setArticleSubTitle((String)data.get(position).get("article_subtitle"));
                item.setArticleBodyhtml((String)data.get(position).get("article_content"));
                item.setArticleImg((String)data.get(position).get("article_img"));
                item.setArticleImgs((String)data.get(position).get("article_imgs"));
                item.setArticleText((String)data.get(position).get("article_text"));
                String img = (String)data.get(position).get("article_img");
                if(TextUtils.isEmpty(img)){
                    ArticleContentActivity.lunch(item);
                }else {
                    ArticleContentActivity.lunch(item, img);
                }
            }
        });
        return convertView;
    }

}
