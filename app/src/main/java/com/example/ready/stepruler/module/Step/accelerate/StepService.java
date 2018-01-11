package com.example.ready.stepruler.module.Step.accelerate;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.ready.stepruler.R;
import com.example.ready.stepruler.bean.step.StepBean;
import com.example.ready.stepruler.database.StepDBHelper;
import com.example.ready.stepruler.module.Step.StepFragment;
import com.example.ready.stepruler.module.Step.UpdateUiCallBack;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class StepService extends Service implements SensorEventListener{

   private static final String TAG = "StepService";

   //默认30秒存储一次
   private static int duration = 30 * 1000;
   //当前日期
    private static String CURRENT_DATE = "";
    //传感器
    private SensorManager sensorManager;
    //广播接受
    private BroadcastReceiver broadcastReceiver;
    //保存计步计时器，类的声明在这个文件最后
    private TimeCount timeCount;
    //当前所走的步数
    private int CURRENT_STEP;
    //计步传感器类型  Sensor.TYPE_STEP_COUNTER或者Sensor.TYPE_STEP_DETECTOR
    private static int stepSensorType = -1;
    //每次第一次启动计步服务时是否从系统中获取了已有的步数记录
    private boolean hasRecord = false;
    //系统中获取到的已有的步数
    private int hasStepCount = 0;
    //上一次的步数
    private int previousStepCount = 0;
    //通知管理对象
    private NotificationManager notificationManager;
    //加速度传感器中获取的步数
    private StepCount stepCount;
    //IBinder对象，向Activity传递数据的桥梁,类的声明在这个文件最后
    private StepBinder stepBinder = new StepBinder();
    //构建通知者
    private NotificationCompat.Builder builder;
    //记步Notification的ID
    int notifyId_Step = 100;
    //UI监听器对象
    private UpdateUiCallBack mCallback;

    @Override
    public void onCreate() {
        super.onCreate();
        initNotification();
        initTodayData();
        initBroadcastReceiver();
        new Thread(new Runnable() {
            @Override
            public void run() {
                startStepDetector();
            }
        }).start();
        startTimeCount();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return stepBinder;
    }

    /**
     * 传感器监听回调，关键代码
     * 1. TYPE_STEP_COUNTER API的解释说返回从开机被激活后统计的步数，当重启手机后该数据归零，
     * 该传感器是一个硬件传感器所以它是低功耗的。
     * 为了能持续的计步，请不要反注册事件，就算手机处于休眠状态它依然会计步。
     * 当激活的时候依然会上报步数。该sensor适合在长时间的计步需求。
     * 2.TYPE_STEP_DETECTOR只用来监监测走步，每次返回数字1.0。
     *  * 如果需要长事件的计步请使用TYPE_STEP_COUNTER。
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (stepSensorType == Sensor.TYPE_STEP_COUNTER) {
            //获取当前传感器返回的临时步数
            int tempStep = (int) event.values[0];
            //首次如果没有获取手机系统中已有的步数则获取一次系统中APP还未开始记步的步数
            if (!hasRecord) {
                hasRecord = true;
                hasStepCount = tempStep;
            } else {
                //获取APP打开到现在的总步数=本次系统回调的总步数-APP打开之前已有的步数
                int thisStepCount = tempStep - hasStepCount;
                //本次有效步数=（APP打开后所记录的总步数-上一次APP打开后所记录的总步数）
                int thisStep = thisStepCount - previousStepCount;
                //总步数=现有的步数+本次有效步数
                CURRENT_STEP += (thisStep);
                //记录最后一次APP打开到现在的总步数
                previousStepCount = thisStepCount;
            }
        } else if (stepSensorType == Sensor.TYPE_STEP_DETECTOR) {
            if (event.values[0] == 1.0) {
                CURRENT_STEP++;
            }
        }
        updateNotification();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    //初始化通知栏
    private void initNotification(){
        builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("步尺计步")
                .setContentText("今日步数" + CURRENT_STEP + "步" )
                .setContentIntent(getDefalutIntent(Notification.FLAG_ONGOING_EVENT))
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示
                .setPriority(Notification.PRIORITY_DEFAULT)//设置该通知优先级
                .setAutoCancel(false)//设置这个标志当用户单击面板就可以让通知将自动取消
                .setOngoing(true)
                .setSmallIcon(R.drawable.ic_stat_step);
        Notification notification = builder.build();
        notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        startForeground(notifyId_Step, notification);
        Log.d(TAG, "initNotification()");
    }

    /*在顶部常驻:Notification.FLAG_ONGOING_EVENT
     * 点击去除： Notification.FLAG_AUTO_CANCEL
     * */
    public PendingIntent getDefalutIntent(int flags) {
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, new Intent(), flags);
        return pendingIntent;
    }

    //注册广播
    private void initBroadcastReceiver(){
        final IntentFilter filter = new IntentFilter();
        // 屏幕灭屏广播
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        //关机广播
        filter.addAction(Intent.ACTION_SHUTDOWN);
        // 屏幕亮屏广播
        filter.addAction(Intent.ACTION_SCREEN_ON);
        // 当长按电源键弹出“关机”对话或者锁屏时系统会发出这个广播
        // example：有时候会用到系统对话框，权限可能很高，会覆盖在锁屏界面或者“关机”对话框之上，
        // 所以监听这个广播，当收到时就隐藏自己的对话，如点击pad右下角部分弹出的对话框
        filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        //监听日期变化
        filter.addAction(Intent.ACTION_DATE_CHANGED);
        filter.addAction(Intent.ACTION_TIME_CHANGED);
        filter.addAction(Intent.ACTION_TIME_TICK);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(final Context context, final Intent intent) {
                String action = intent.getAction();
                if (Intent.ACTION_SCREEN_ON.equals(action)) {
                    Log.d(TAG, "screen on");
                } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                    Log.d(TAG, "screen off");
                    //改为60秒一存储
                    duration = 60000;
                } else if (Intent.ACTION_USER_PRESENT.equals(action)) {
                    Log.d(TAG, "screen unlock");
                    //改为30秒一存储
                    duration = 30000;
                } else if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(intent.getAction())) {
                    Log.i(TAG, " receive Intent.ACTION_CLOSE_SYSTEM_DIALOGS");
                    //保存一次
                    save();
                } else if (Intent.ACTION_SHUTDOWN.equals(intent.getAction())) {
                    Log.i(TAG, " receive ACTION_SHUTDOWN");
                    save();
                } else if (Intent.ACTION_DATE_CHANGED.equals(action)) {
                    //日期变化步数重置为0
                    save();
                    isNewDay();
                } else if (Intent.ACTION_TIME_CHANGED.equals(action)) {
                    //时间变化步数重置为0
                    save();
                    isNewDay();
                } else if (Intent.ACTION_TIME_TICK.equals(action)) {
                    //日期变化步数重置为0
                    save();
                    isNewDay();
                }
            }
        };
        registerReceiver(broadcastReceiver, filter);
    }

    //监听晚上0点变化初始化数据
    private void isNewDay() {
        String time = "00:00";
        if (time.equals(new SimpleDateFormat("HH:mm").format(new Date())) || !CURRENT_DATE.equals(getTodayDate())) {
            initTodayData();
        }
    }

    //初始化当天的步数
    private void initTodayData() {
        CURRENT_DATE = getTodayDate();
        StepDBHelper.createDb(this, "DylanStepCount");
        StepDBHelper.getLiteOrm().setDebugged(false);
        //获取当天的数据，用于展示
        List<StepBean> list = StepDBHelper.getQueryByWhere(StepBean.class, "today", new String(CURRENT_DATE));
        if (list.size() == 0 || list.isEmpty()) {
            CURRENT_STEP = 0;
        } else if (list.size() == 1) {
            Log.v(TAG, "StepData=" + list.get(0).toString());
            CURRENT_STEP = Integer.parseInt(list.get(0).getStep());
        } else {
            Log.v(TAG, "出错了！");
        }
        if (stepCount != null) {
            stepCount.setSteps(CURRENT_STEP);
        }
        updateNotification();
    }

    //更新步数通知
    private void updateNotification() {
        //设置点击跳转
        Intent hangIntent = new Intent(this, StepFragment.getInstance().getClass());
        PendingIntent hangPendingIntent = PendingIntent.getActivity(this, 0, hangIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        Notification notification = builder.setContentTitle(getResources().getString(R.string.app_name))
                .setContentText("今日步数" + CURRENT_STEP + " 步")
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示
                .setContentIntent(hangPendingIntent)
                .build();
        notificationManager.notify(notifyId_Step, notification);
        if (mCallback != null) {
            mCallback.updateUi(CURRENT_STEP);
        }
        Log.d(TAG, "updateNotification()");
    }

    //获取当天日期
    private String getTodayDate() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    //获取传感器实例
    private void startStepDetector() {
        if (sensorManager != null) {
            sensorManager = null;
        }
        // 获取传感器管理器的实例
        sensorManager = (SensorManager) this
                .getSystemService(SENSOR_SERVICE);
        //android4.4以后可以使用计步传感器
        int VERSION_CODES = Build.VERSION.SDK_INT;
        if (VERSION_CODES >= 19) {
            addCountStepListener();
        } else {
            addBasePedometerListener();
        }
    }

    //添加传感器监听
    private void addCountStepListener() {
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        Sensor detectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        if (countSensor != null) {
            stepSensorType = Sensor.TYPE_STEP_COUNTER;
            Log.v(TAG, "Sensor.TYPE_STEP_COUNTER");
            sensorManager.registerListener(StepService.this, countSensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else if (detectorSensor != null) {
            stepSensorType = Sensor.TYPE_STEP_DETECTOR;
            Log.v(TAG, "Sensor.TYPE_STEP_DETECTOR");
            sensorManager.registerListener(StepService.this, detectorSensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            Log.v(TAG, "Count sensor not available!");
            addBasePedometerListener();
        }
    }

    //通过加速度传感器来记步
    private void addBasePedometerListener() {
        stepCount = new StepCount();
        stepCount.setSteps(CURRENT_STEP);
        // 获得传感器的类型，这里获得的类型是加速度传感器
        // 此方法用来注册，只有注册过才会生效，参数：SensorEventListener的实例，Sensor的实例，更新速率
        Sensor sensor = sensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        boolean isAvailable = sensorManager.registerListener(stepCount.getStepDetector(), sensor,
                SensorManager.SENSOR_DELAY_UI);
        stepCount.initListener(new StepValuePassListener() {
            @Override
            public void stepChanged(int steps) {
                CURRENT_STEP = steps;
                updateNotification();
            }
        });
        if (isAvailable) {
            Log.v(TAG, "加速度传感器可以使用");
        } else {
            Log.v(TAG, "加速度传感器无法使用");
        }
    }

    //开始保存记步数据
    private void startTimeCount() {
        if (timeCount == null) {
            timeCount = new TimeCount(duration, 1000);
        }
        timeCount.start();
    }

    //保存数据
    private void save(){
        int tempStep = CURRENT_STEP;
        List<StepBean> list = StepDBHelper.getQueryByWhere(StepBean.class, "today", new String(CURRENT_DATE));
        if(list.size() == 0 || list.isEmpty()){
            StepBean data = new StepBean();
            data.setToday(CURRENT_DATE);
            data.setStep(tempStep + "");
            StepDBHelper.insert(data);
        }else if(list.size() == 1){
            StepBean data = list.get(0);
            data.setStep(tempStep + "");
            StepDBHelper.update(data);
        }
    }

    //获取当前步数
    public int getStepCount() {
        return CURRENT_STEP;
    }

    //注册UI更新监听
    public void registerCallback(UpdateUiCallBack paramICallback) {
        this.mCallback = paramICallback;
    }



    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //取消前台进程
        stopForeground(true);
        StepDBHelper.closeDb();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    //向Activity传递数据的纽带
    public class StepBinder extends Binder {
        /**
         * 获取当前service对象
         */
        public StepService getService() {
            return StepService.this;
        }
    }

    //计时器
    class TimeCount extends CountDownTimer{

        /**
         * 编译器自己生成的注释，不怪我啊=_=
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
        }

        @Override
        public void onFinish() {
            // 如果计时器正常结束，则开始计步
            timeCount.cancel();
            save();
            startTimeCount();
        }
    }
}
