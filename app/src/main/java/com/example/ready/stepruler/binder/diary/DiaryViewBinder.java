package com.example.ready.stepruler.binder.diary;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.support.v7.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ready.stepruler.R;
import com.example.ready.stepruler.module.Diary.editor.EditorActivity;
import com.example.ready.stepruler.bean.diary.DiaryBean;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;
import me.drakeet.multitype.BuildConfig;
import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by ready on 2017/12/15.
 */

public class DiaryViewBinder extends ItemViewBinder<DiaryBean,DiaryViewBinder.ViewHolder> {
    private static final String Tag = "DiaryViewBinder";

    @NonNull
    @Override
    protected DiaryViewBinder.ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_diary, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull final DiaryBean item) {
        final Context context = holder.itemView.getContext();

        try {
            holder.tv_diary_date.setText(item.getDate());
            holder.tv_diary_title.setText(item.getTitle());
            holder.tv_diary_content.setText(item.getContent());
            holder.iv_diary_dots.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(context, holder.iv_diary_dots, Gravity.END, 0,R.style.MyPopupMenu);
                    popupMenu.inflate(R.menu.menu_diary_dots);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menu) {
                            int itemId = menu.getItemId();
                            if (itemId == R.id.action_edit){
                               // Toast.makeText(context,,Toast.LENGTH_SHORT).show();
                            }
                            if (itemId == R.id.action_delete){
                                Toast.makeText(context,itemId,Toast.LENGTH_SHORT).show();
                            }
                            if (itemId == R.id.action_share){
                                Toast.makeText(context,itemId,Toast.LENGTH_SHORT).show();
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
                            EditorActivity.startActivity(context, item.getTitle(), item.getContent());
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

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_diary_date;
        TextView tv_diary_title;
        TextView tv_diary_content;
        ImageView iv_diary_dots;
        ImageView iv_diary_circle;
        LinearLayout item_diary;
        LinearLayout diary;
        RelativeLayout rl_diary_edit;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_diary_date = (TextView) itemView.findViewById(R.id.tv_diary_date);
            tv_diary_title = (TextView) itemView.findViewById(R.id.tv_diary_title);
            tv_diary_content = (TextView) itemView.findViewById(R.id.tv_diary_content);
            iv_diary_circle = (ImageView) itemView.findViewById(R.id.iv_diary_circle);
            iv_diary_dots = (ImageView) itemView.findViewById(R.id.iv_diary_dots);
            item_diary = (LinearLayout) itemView.findViewById(R.id.item_diary);
            item_diary = (LinearLayout) itemView.findViewById(R.id.item_diary);
            diary = (LinearLayout) itemView.findViewById(R.id.diary);
            rl_diary_edit = (RelativeLayout) itemView.findViewById(R.id.rl_diary_edit);
        }
    }
}
