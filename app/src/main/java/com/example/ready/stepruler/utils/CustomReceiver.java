package com.example.ready.stepruler.utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.avos.avoscloud.AVOSCloud;
import com.example.ready.stepruler.R;
import com.example.ready.stepruler.module.social.InvitationActivity;

import org.json.JSONObject;

/**
 * Created by ready on 2018/1/7.
 */

public class CustomReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

    Toast.makeText(context,"ceshi", Toast.LENGTH_SHORT).show();
        try {
            if (intent.getAction().equals("com.invitation.action")) {
                JSONObject json = new JSONObject(intent.getExtras().getString("com.avos.avoscloud.Data"));
                final String message = json.getString("alert");
                final String id = json.getString("id");

                Intent resultIntent = new Intent(AVOSCloud.applicationContext, InvitationActivity.class);
                resultIntent.putExtra("id", id);

                PendingIntent pendingIntent =
                        PendingIntent.getActivity(AVOSCloud.applicationContext, 0, resultIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT);
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(AVOSCloud.applicationContext)
                                .setSmallIcon(R.drawable.ic_center_friends)
                                .setContentTitle(
                                        AVOSCloud.applicationContext.getResources().getString(R.string.app_name))
                                .setContentText(message)
                                .setTicker(message);
                mBuilder.setContentIntent(pendingIntent);
                mBuilder.setAutoCancel(true);

                int mNotificationId = 10086;
                NotificationManager mNotifyMgr =
                        (NotificationManager) AVOSCloud.applicationContext
                                .getSystemService(
                                        Context.NOTIFICATION_SERVICE);
                mNotifyMgr.notify(mNotificationId, mBuilder.build());

            }
        } catch (Exception e) {

        }
    }
}
