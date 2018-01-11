package com.example.ready.stepruler.module.community.editor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.ready.stepruler.R;
import com.example.ready.stepruler.MainActivity;
import com.example.ready.stepruler.api.CommunityApi;
import com.example.ready.stepruler.module.community.me.CommMeFragment;
import com.example.ready.stepruler.utils.RetrofitFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by single dog on 2018/1/1.
 */

public class EditCommunityActivity extends AppCompatActivity {
    private EditText editText;
    private Toolbar toolbar;
    CommunityApi communityApi = RetrofitFactory.getRetrofit().create(CommunityApi.class);

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, EditCommunityActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_community);
        initView();
        setListener();
    }

    private void initView() {
        editText = (EditText) findViewById(R.id.et_editor_community_editText);

        toolbar = (Toolbar) findViewById(R.id.community_toolbar);
        toolbar.inflateMenu(R.menu.menu_community);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.white_exit);
    }

    private void setListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_community, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.community_complete) {
            //获取时间
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
            Date curDate = new Date(System.currentTimeMillis());//获取当前时间
            String str = formatter.format(curDate);


            Call<String> post = communityApi.post(MainActivity.getUser().getUserId(), str, editText.getText().toString(), 0, 0);
            post.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                   // CommMeFragment.getInstace().setCanfresh();
                    //CommMeFragment.getInstace().onSetAdapter(new ArrayList<Object>());
                    CommMeFragment.getInstace().onRefresh();
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                }
            });
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
