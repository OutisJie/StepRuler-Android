package com.example.ready.stepruler.module.social;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.ready.stepruler.R;
import com.example.ready.stepruler.MainActivity;
import com.example.ready.stepruler.api.UserApi;
import com.example.ready.stepruler.utils.AppManager;
import com.example.ready.stepruler.utils.RetrofitFactory;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class InvitationActivity extends AppCompatActivity {

    private int userId1;
    private int userId2;
    private Button agree;
    private Button disagree;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invatation);
        AppManager.getAppManager().addActivity(this);
        Intent intent = getIntent();
        userId1 = MainActivity.getUser().getUserId();
        userId2 = Integer.parseInt(intent.getStringExtra("id"));
        initView();
    }
    private void initView(){
        agree = (Button) findViewById(R.id.agree);
        agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<Boolean> call  = RetrofitFactory.getRetrofit().create(UserApi.class).addFriend(userId1, userId2);
                call.enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if(response.body()){
                            Toast.makeText(InvitationActivity.this,"添加成功",Toast.LENGTH_SHORT).show();

                        }else {
                            Toast.makeText(InvitationActivity.this,"添加失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        Toast.makeText(InvitationActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
                    }
                });
                finishActivity();
            }
        });
        disagree = (Button) findViewById(R.id.disagree);
        disagree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishActivity();
            }
        });
    }

    private void finishActivity(){
        if(AppManager.getAppManager().findActivity(MainActivity.class) == null){
            MainActivity.startActivity(this);
            AppManager.getAppManager().finishActivity(this);
        }else {
            AppManager.getAppManager().finishActivity(this);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AppManager.getAppManager().finishActivity(this);
    }
}
