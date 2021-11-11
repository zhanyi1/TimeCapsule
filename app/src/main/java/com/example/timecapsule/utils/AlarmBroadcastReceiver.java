package com.example.timecapsule.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.view.View;

import androidx.core.app.NotificationCompat;

import com.example.timecapsule.R;

public class AlarmBroadcastReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "Reminders";
    @Override
    public void onReceive(Context context, Intent intent) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Reminders", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("For reminding users");
            channel.setLockscreenVisibility(View.VISIBLE);
            channel.setSound(alarmSound, new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build());
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        showNotification(context, intent.getIntExtra("id",0), intent.getStringExtra("name"), intent.getStringExtra("time"));
    }

    private void showNotification(Context context,int id, String title, String text) {
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,CHANNEL_ID);
        builder.setContentTitle(title);
        builder.setContentText(text);
        builder.setSmallIcon(R.drawable.time);
        // builder.setOngoing(true);
        builder.setAutoCancel(true);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            builder.setSound(alarmSound);
        }

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(id, builder.build());
    }
}
