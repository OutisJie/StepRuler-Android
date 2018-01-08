package com.example.ready.stepruler.module.Diary.editor;

import java.util.Calendar;

/**
 * Created by ready on 2017/12/19.
 */

public class EditorPresenter {
    private IEditorView view;

    public void attchView(IEditorView view){
        this.view = view;
    }
    public void doOpenDiary(){
        view.onOpenDiary();
    }
    public void doSaveDiary(){
        view.onSaveDiary();
    }
    public void doCancel(){
        view.onCancel();
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
