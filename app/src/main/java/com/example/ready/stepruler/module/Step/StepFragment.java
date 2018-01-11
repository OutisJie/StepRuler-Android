package com.example.ready.stepruler.module.Step;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ready.stepruler.R;
import com.example.ready.stepruler.adapter.CommonAdapter;
import com.example.ready.stepruler.adapter.CommonViewHolder;
import com.example.ready.stepruler.bean.step.StepBean;
import com.example.ready.stepruler.database.StepDBHelper;
import com.example.ready.stepruler.module.Step.accelerate.StepService;
import com.example.ready.stepruler.widget.StepArcWidget;

import java.util.List;

/**
 * Created by ready on 2017/12/3.
 */

public class StepFragment extends Fragment {
    private static final String Tag = "StepFragment";
    private static StepFragment instance = null;
    private boolean isBind = false;

    //控件
    private TextView isSupport;
    private StepArcWidget stepArcWidget;
    private ListView listView;

    public static StepFragment getInstance() {
        if (instance == null) {
            instance = new StepFragment();
        }
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step, container, false);
        initView(view);
        initData();
        setupService();
        return view;
    }

    private void initData() {
        //设置当前步数为0
        stepArcWidget.setCurrentCount(1000, 0);
        isSupport.setText("计步中...");

        setEmptyView(listView);
        if (StepDBHelper.getLiteOrm() == null) {
            StepDBHelper.createDb(this.getContext(), "jingzhi");
        }
        List<StepBean> stepDatas = StepDBHelper.getQueryAll(StepBean.class);
        // Logger.d("stepDatas="+stepDatas);
        listView.setAdapter(new CommonAdapter<StepBean>(this.getContext(), stepDatas, R.layout.item_step_history) {
            @Override
            protected void convertView(View item, StepBean stepData) {
                TextView tv_date = CommonViewHolder.get(item, R.id.tv_date);
                TextView tv_step = CommonViewHolder.get(item, R.id.tv_step);
                tv_date.setText(stepData.getToday());
                tv_step.setText(stepData.getStep() + "步");
            }
        });
    }


    private void initView(View view) {
        isSupport = (TextView) view.findViewById(R.id.tv_isSupport);
        stepArcWidget = (StepArcWidget) view.findViewById(R.id.stepcircle);
        listView = (ListView) view.findViewById(R.id.lv_history);
    }

    private void setupService() {
        Intent intent = new Intent(this.getContext(), StepService.class);
        isBind = this.getContext().bindService(intent, conn, Context.BIND_AUTO_CREATE);
        this.getContext().startService(intent);
    }

    protected <T extends View> T setEmptyView(ListView listView) {
        TextView emptyView = new TextView(this.getContext());
        emptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        emptyView.setText("暂无数据！");
        emptyView.setGravity(Gravity.CENTER);
        emptyView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        emptyView.setVisibility(View.GONE);
        ((ViewGroup) listView.getParent()).addView(emptyView);
        listView.setEmptyView(emptyView);
        return (T) emptyView;
    }

    /**
     * 用于查询应用服务（application Service）的状态的一种interface，
     * 和许多来自系统的回调方式一样，ServiceConnection的方法都是进程的主线程中调用的。
     */
    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            StepService stepService = ((StepService.StepBinder) service).getService();
            //设置初始化数据
            stepArcWidget.setCurrentCount(1000, stepService.getStepCount());
            //设置步数监听回调
            stepService.registerCallback(new UpdateUiCallBack() {
                @Override
                public void updateUi(int stepCount) {
                    stepArcWidget.setCurrentCount(1000, stepCount);
                    List<StepBean> stepDatas = StepDBHelper.getQueryAll(StepBean.class);
                    // Logger.d("stepDatas="+stepDatas);
                    listView.setAdapter(new CommonAdapter<StepBean>(instance.getContext(), stepDatas, R.layout.item_step_history) {
                        @Override
                        protected void convertView(View item, StepBean stepData) {
                            TextView tv_date = CommonViewHolder.get(item, R.id.tv_date);
                            TextView tv_step = CommonViewHolder.get(item, R.id.tv_step);
                            tv_date.setText(stepData.getToday());
                            tv_step.setText(stepData.getStep() + "步");
                        }
                    });
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
}
