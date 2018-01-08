package com.example.ready.stepruler.module.Diary.diarys;

import com.example.ready.stepruler.bean.diary.DiaryBean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by ready on 2017/12/13.
 */

public class DiaryPresenter {
    private IDiaryView view;

    private List<DiaryBean> diaryList;

    public void attachView (IDiaryView view){
        this.view = view;
    }
    //添加日记
    public void doAddDiary(){
        view.onAddDiary();
    }
    //加载日记
    public void doInitRecyclerView(){
        if(diaryList == null) {
            diaryList = new ArrayList<>();
        }else if(diaryList.size() != 0){
            diaryList.clear();
        }
        view.onLoadData(diaryList);
        view.onRegisterItems(diaryList);
        view.onInitRecyclerView();
    }

    public static StringBuilder getDate(){
        StringBuilder stringBuilder = new StringBuilder();
        Calendar now = Calendar.getInstance();
        stringBuilder.append(now.get(Calendar.YEAR) + "年");
        stringBuilder.append(now.get(Calendar.MONTH) + 1 + "月");
        stringBuilder.append(now.get(Calendar.DAY_OF_MONTH) + "日");
        return stringBuilder;
    }
}
