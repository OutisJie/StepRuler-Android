package com.example.ready.stepruler.module.Diary.diarys;

import com.example.ready.stepruler.bean.diary.DiaryBean;

import java.util.List;

/**
 * Created by ready on 2017/12/13.
 */

public interface IDiaryView{
    void onAddDiary();
    void updateDiary();
    void removeDiary();
    void onLoadData(List<DiaryBean> diarys);
    void onReloadDiary();
    void onRegisterItems(List<?> diarys);
    void onInitRecyclerView();
}
