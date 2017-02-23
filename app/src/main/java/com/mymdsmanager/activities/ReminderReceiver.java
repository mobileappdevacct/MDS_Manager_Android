package com.mymdsmanager.activities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.mymdsmanager.R;

import java.util.Random;

/**
 * Created by nitin on 5/4/16.
 */
public class ReminderReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent k2) {
        // TODO Auto-generated method stub

        Log.e("Alarm Recieved", "Alarm Recieved");



        Intent intent = new Intent(context, MainActivity.class);//new Intent(this, MainActivity.class);


        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,  (int) System.currentTimeMillis() /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = Uri.parse("android.resource://com.mymdsmanager/raw/" + k2.getStringExtra("sound"));
        Notification.Builder notificationBuilder = new Notification.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(context.getResources().getString(R.string.app_name))
                .setStyle(new Notification.BigTextStyle().bigText("Take your medicine " +k2.getStringExtra("title") ))

                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Random random = new Random();
        int m = random.nextInt(9999 - 1000) +
                1000;
        notificationManager.notify(m /* ID of notification */, notificationBuilder.build());

    }

}
