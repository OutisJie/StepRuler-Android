package com.example.ready.stepruler.module.push.content;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.ready.stepruler.R;
import com.example.ready.stepruler.bean.push.PushArticleDataBean;
import com.example.ready.stepruler.utils.AppManager;

public class ArticleContentActivity extends AppCompatActivity {
    private static final String TAG = "ArticleContentActivity";
    private static final String IMG = "img";

    public static void lunch(PushArticleDataBean item){
        Context context = AppManager.getAppManager().currentActivity();
        context.startActivity(new Intent(context, ArticleContentActivity.class)
                .putExtra(TAG, item)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

    }

    public static void lunch(PushArticleDataBean item, String imgUrl){
        Context context = AppManager.getAppManager().currentActivity();
        context.startActivity(new Intent(context, ArticleContentActivity.class)
                .putExtra(TAG, item)
                .putExtra(IMG, imgUrl)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container);
        Intent intent = getIntent();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, ArticleContentFragment.newInstance(intent.getParcelableExtra(TAG), intent.getStringExtra(IMG)))
                .commit();
    }
}
