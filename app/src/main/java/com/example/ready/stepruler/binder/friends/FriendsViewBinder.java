package com.example.ready.stepruler.binder.friends;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ready.stepruler.R;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import com.squareup.picasso.Picasso;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.leancloud.chatkit.LCChatKitUser;
import cn.leancloud.chatkit.activity.LCIMConversationActivity;
import cn.leancloud.chatkit.utils.LCIMConstants;
import cn.leancloud.chatkit.viewholder.LCIMCommonViewHolder;

/**
 * Created by ready on 2018/1/3.
 * 成员列表adapter
 */

public class FriendsViewBinder extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    //所有成员的list
    private List<FriendItem> friendList = new ArrayList<FriendItem>();
    //有序memberList中 MemberItem.sortContent 第一次出现时的字母与位置的 map
    private Map<Character, Integer> indexMap = new HashMap<Character, Integer>();
    //简体中文的Collator
    Collator cmp = Collator.getInstance(Locale.SIMPLIFIED_CHINESE);

    public FriendsViewBinder(){}

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FriendsViewBinder.ViewHolder(parent.getContext(), parent);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((FriendsViewBinder.ViewHolder) holder).bindData(friendList.get(position).lcChatKitUser);
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    public Map<Character, Integer> getIndexMap(){
        return indexMap;
    }

    /*
     *设置成员列表，然后更新索引
     *此处会对数据以 空格、数字、字母（汉字转化为拼音后的字母） 的顺序进行重新排列
     * */
    public void setFriendList(List<LCChatKitUser> userList){
        friendList.clear();
        if(userList != null){
            for(LCChatKitUser user : userList){
                FriendItem item = new FriendItem();
                item.lcChatKitUser = user;
                item.sortContent = PinyinHelper.convertToPinyinString(user.getUserName(), "",PinyinFormat.WITHOUT_TONE);
                friendList.add(item);
            }
        }
        Collections.sort(friendList, new SortChineseName());
        updateIndex();
    }
    private void updateIndex() {
        Character lastCharcter = '#';
        indexMap.clear();
        for (int i = 0; i < friendList.size(); i++) {
            Character curChar = Character.toLowerCase(friendList.get(i).sortContent.charAt(0));
            if (!lastCharcter.equals(curChar)) {
                indexMap.put(curChar, i);
            }
            lastCharcter = curChar;
        }
    }

    public class SortChineseName implements Comparator<FriendItem>{
        @Override
        public int compare(FriendItem o1, FriendItem o2) {
            if (null == o1) {
                return -1;
            }
            if (null == o2) {
                return 1;
            }
            if (cmp.compare(o1.sortContent, o2.sortContent)>0){
                return 1;
            }else if (cmp.compare(o1.sortContent, o2.sortContent)<0){
                return -1;
            }
            return 0;
        }
    }

    public static class FriendItem{
        public LCChatKitUser lcChatKitUser;
        public String sortContent;
    }

    class ViewHolder extends LCIMCommonViewHolder<LCChatKitUser>{
        private ImageView iv_friend_head;
        private TextView tv_username;

        public LCChatKitUser lcChatKitUser;

        public ViewHolder(Context context, ViewGroup root) {
            super(context, root, R.layout.item_friends);
            initView();
        }
        protected void  initView(){
            iv_friend_head = (ImageView) itemView.findViewById(R.id.iv_friend_head);
            tv_username = (TextView) itemView.findViewById(R.id.tv_username);

            //点击好友新建会话
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), LCIMConversationActivity.class);
                    intent.putExtra(LCIMConstants.PEER_ID, lcChatKitUser.getUserId());
                    getContext().startActivity(intent);
                }
            });
        }

        @Override
        public void bindData(LCChatKitUser lcChatKitUser) {
            this.lcChatKitUser = lcChatKitUser;
            final String imgUrl = lcChatKitUser.getAvatarUrl();
            if(!TextUtils.isEmpty(imgUrl)){
                Picasso.with(getContext()).load(imgUrl).into(iv_friend_head);
            }else {
                iv_friend_head.setImageResource(R.drawable.ice_user_header);
            }
            tv_username.setText(lcChatKitUser.getUserName());
        }
    }
}
