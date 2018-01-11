package com.example.ready.stepruler.module.Diary.diarys;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ready.stepruler.binder.BindItem;
import com.example.ready.stepruler.widget.listener.OnLoadMoreListener;
import com.example.ready.stepruler.R;
import com.example.ready.stepruler.bean.diary.DiaryBean;
import com.example.ready.stepruler.database.DiaryDataBaseHelper;
import com.example.ready.stepruler.module.Diary.editor.EditorActivity;

import java.util.Collections;
import java.util.List;

import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Created by ready on 2017/12/3.
 */

public class DiaryFragment extends Fragment implements IDiaryView {
    private static final String Tag = "DiaryFragment";
    private static DiaryFragment instance = null;
    //控件
    private ImageView iv_diary_circle;
    private TextView tv_diary_date;
    private LinearLayout diary_home;
    private LinearLayout diary_tips;
    private LinearLayout tv_diary_tips;
    private RecyclerView rv_diary_show;
    private FloatingActionButton fb_diary_add;
    //今天是否已经写了日记
    private boolean isWrite;
    //日记数据
    private DiaryDataBaseHelper dbHelper;
    private DiaryPresenter diaryPresenter;
    //recyclerView
    private MultiTypeAdapter multiTypeAdapter;
    private Items diaryItems;

    public static DiaryFragment getInstace() {
        if(instance == null)
            instance = new DiaryFragment();
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diary, container, false);
        diaryPresenter = new DiaryPresenter();
        diaryPresenter.attachView(this);
        initView(view);
        return view;
    }
    private void initView(final View view){
        iv_diary_circle = (ImageView) view.findViewById(R.id.iv_diary_circle);
        tv_diary_date = (TextView) view.findViewById(R.id.tv_diary_date);
        tv_diary_date.setText(diaryPresenter.getDate());
        diary_home = (LinearLayout) view.findViewById(R.id.diary_home);
        diary_tips = (LinearLayout) view.findViewById(R.id.diary_tips);
        rv_diary_show = (RecyclerView) view.findViewById(R.id.rv_show_diary);
        diaryPresenter.doInitRecyclerView();
        fb_diary_add = (FloatingActionButton) view.findViewById(R.id.add_diary);
        fb_diary_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diaryPresenter.doAddDiary();
            }
        });
    }
    @Override
    public void onLoadData(List<DiaryBean> diarys) {
        dbHelper = new DiaryDataBaseHelper(this.getContext(),"Diary.db",null,1);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query("Diary",null,null,null,null,null,null);
        //删除提示
        if(cursor.moveToFirst()){
            do{
                String date = cursor.getString(cursor.getColumnIndex("date"));
                String systemDate = diaryPresenter.getDate().toString();
                if(date.equals(systemDate)){
                    diary_home.removeView(diary_tips);
                    break;
                }
            }while (cursor.moveToNext());
        }
        //获取数据
        if(cursor.moveToFirst()) {
            do {
                String date = cursor.getString(cursor.getColumnIndex("date"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String content = cursor.getString(cursor.getColumnIndex("content"));
                String tag = cursor.getString(cursor.getColumnIndex("tag"));
                diarys.add(new DiaryBean(date, title, content, tag));
            } while (cursor.moveToNext());
        }
        Collections.reverse(diarys);
        cursor.close();
    }

    @Override
    public void onReloadDiary() {
        diaryPresenter.doInitRecyclerView();
    }

    @Override
    public void onInitRecyclerView() {
        multiTypeAdapter = new MultiTypeAdapter(diaryItems);
        BindItem.registerDiaryItem(multiTypeAdapter);
        rv_diary_show.setHasFixedSize(true);
        rv_diary_show.setLayoutManager(new LinearLayoutManager(this.getContext()));
        rv_diary_show.setAdapter(multiTypeAdapter);
        rv_diary_show.addOnScrollListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                Toast.makeText(getActivity(),"nihao",Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onRegisterItems(List<?> diarys) {
        Items newItems = new Items(diarys);
        diaryItems = new Items();
        diaryItems.clear();
        diaryItems.addAll(newItems);
    }

    @Override
    public void onAddDiary() {
        EditorActivity.startActivity(this.getContext());
    }

    @Override
    public void updateDiary() {

    }

    @Override
    public void removeDiary() {

    }
}
