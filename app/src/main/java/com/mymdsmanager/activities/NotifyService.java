package com.mymdsmanager.activities;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.mymdsmanager.R;
import com.mymdsmanager.database.DBAdapter;
import com.mymdsmanager.wrapper.AppointmentsWrapper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class NotifyService extends Service{
    ArrayList<AppointmentsWrapper> notesList;
    boolean setnotyfy = true;
    private DBAdapter dbAdapter;
//    public NotifyService(ArrayList<AppointmentsWrapper> notesList)
//    {
//        this.notesList=notesList;
//    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        dbAdapter = new DBAdapter(getApplicationContext());
        notesList = dbAdapter.getAppointmentList("");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        notifyAlret();

        return Service.START_STICKY;
    }

    public void notifyAlret() {


        new CountDownTimer(1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if (notesList.size()>0) {
                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("MMM dd yyyy");
                    String formattedDate = df.format(c.getTime());
                    SimpleDateFormat sdfs = new SimpleDateFormat("hh:mm a");
                    String time = sdfs.format(c.getTime());
                    String matchString = formattedDate + " " + time;

                    for (int i = 0; i < notesList.size(); i++) {
                        String dateNTime = notesList.get(i).getDateNtime();
                        System.out.println("dateNTime" + dateNTime);
                        String arr[] = dateNTime.split("###");

                        if (matchString.equalsIgnoreCase(arr[0] + " " + arr[1])) {
                            if (setnotyfy) {
                                Intent intent = new Intent(getApplicationContext(), CalenderActivity.class);//new Intent(this, MainActivity.class);


                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),  (int) System.currentTimeMillis() /* Request code */, intent,
                                        PendingIntent.FLAG_ONE_SHOT);

                                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                Notification.Builder notificationBuilder = new Notification.Builder(getApplicationContext())
                                        .setSmallIcon(R.mipmap.ic_launcher)
                                        .setContentTitle(getResources().getString(R.string.app_name))
                                        .setStyle(new Notification.BigTextStyle().bigText("Toady you have appointment with " + notesList.get(i).getProvider()))

                                        .setAutoCancel(true)
                                        .setSound(defaultSoundUri)
                                        .setContentIntent(pendingIntent);

                                NotificationManager notificationManager =
                                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                Random random = new Random();
                                int m = random.nextInt(9999 - 1000) +
                                        1000;
                                notificationManager.notify(m /* ID of notification */, notificationBuilder.build());
                            }
                            setnotyfy = false;

                        }
                    }
                    if (setnotyfy) {
                        start();
                    }
                }
            }
        }.start();
    }
}
