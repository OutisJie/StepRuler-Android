package com.example.ready.stepruler.module.Diary.editor;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ready.stepruler.R;
import com.example.ready.stepruler.database.DiaryDataBaseHelper;
import com.example.ready.stepruler.module.Diary.diarys.DiaryFragment;
import com.example.ready.stepruler.utils.AppManager;
import com.example.ready.stepruler.widget.LineEditWidget;

import cc.trity.floatingactionbutton.FloatingActionButton;

public class EditorActivity extends AppCompatActivity implements IEditorView {
    private Toolbar toolbar;
    private TextView tv_editor_diary_date;
    private EditText et_editor_diary_title;
    private LineEditWidget et_editor_diary_content;
    private FloatingActionButton fb_editor_diary_save;
    private FloatingActionButton fb_editor_diary_cancel;
    private EditorPresenter editorPresenter;

    private DiaryDataBaseHelper diaryDataBaseHelper;
    public static void startActivity(Context context){
        Intent intent = new Intent(context,EditorActivity.class);
        context.startActivity(intent);
       // AppManager.getAppManager().finishActivity(context.getClass());
    }
    public static void startActivity(Context context, String title, String content){
        Intent intent = new Intent(context, EditorActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("content", content);
        context.startActivity(intent);
        //AppManager.getAppManager().finishActivity(context.getClass());
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_diary);
        AppManager.getAppManager().addActivity(this);
        editorPresenter = new EditorPresenter();
        editorPresenter.attchView(this);
        editorPresenter.doOpenDiary();
        Intent intent = getIntent();
        initView(intent);
        diaryDataBaseHelper = new DiaryDataBaseHelper(this, "Diary.db", null, 1);

    }

    private void initView(Intent intent){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_diary_edit_option);
        toolbar.setTitle(R.string.diary_edit);
        tv_editor_diary_date = (TextView) findViewById(R.id.tv_editor_diary_date);
        tv_editor_diary_date.setText("今天" + editorPresenter.getDate());

        et_editor_diary_title = (EditText) findViewById(R.id.et_editor_diary_title);
        et_editor_diary_title.setText(intent.getStringExtra("title"));

        et_editor_diary_content = (LineEditWidget) findViewById(R.id.et_editor_diary_content);
        et_editor_diary_content.setText(intent.getStringExtra("content"));

        fb_editor_diary_save = (FloatingActionButton) findViewById(R.id.fb_editor_diary_save);
        fb_editor_diary_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editorPresenter.doSaveDiary();
            }
        });

        fb_editor_diary_cancel = (FloatingActionButton) findViewById(R.id.fb_editor_diary_cancel);
        fb_editor_diary_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               editorPresenter.doCancel();
            }
        });
    }

    @Override
    public void onOpenDiary() {

    }

    @Override
    public void onSaveDiary() {
        final String dateBack = editorPresenter.getDate().toString();
        final String titleBack = et_editor_diary_title.getText().toString();
        final String contentBack = et_editor_diary_content.getText().toString();
        final String tag = String.valueOf(System.currentTimeMillis());
        if(!titleBack.equals("") || !contentBack.equals("")){
            SQLiteDatabase db = diaryDataBaseHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("date",dateBack);
            values.put("title", titleBack);
            values.put("content", contentBack);
            values.put("tag", tag);
            db.insert("Diary", null, values);
            values.clear();
            //返回到mainActivity,并刷新日记
            AppManager.getAppManager().finishActivity(EditorActivity.this);
            DiaryFragment.getInstace().onReloadDiary();
        }

    }

    @Override
    public void onCancel() {
        final String dateBack = editorPresenter.getDate().toString();
        final String titleBack = et_editor_diary_title.getText().toString();
        final String contentBack = et_editor_diary_content.getText().toString();
        final String tag = String.valueOf(System.currentTimeMillis());

        if(!titleBack.isEmpty() || !contentBack.isEmpty()){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("是否保存日记内容?").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SQLiteDatabase db = diaryDataBaseHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("date", dateBack);
                    values.put("title", titleBack);
                    values.put("content", contentBack);
                    values.put("tag", tag);
                    db.insert("Diary", null, values);
                    values.clear();
                    //返回到mainActivity,并刷新日记
                    AppManager.getAppManager().finishActivity(EditorActivity.this);
                    DiaryFragment.getInstace().onReloadDiary();
                }
            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    AppManager.getAppManager().finishActivity(EditorActivity.this);
                }
            }).show();
        }else {
            AppManager.getAppManager().finishActivity(EditorActivity.this);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AppManager.getAppManager().finishActivity(EditorActivity.this);
    }

    @Override
    public void onRedo() {

    }

    @Override
    public void onUndo() {

    }
}
