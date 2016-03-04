package com.example.pankinmichail.myapplication;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;

import com.example.pankinmichail.myapplication.Models.AdNotification;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Mikhail Pankin
 * mikhail.p-n@yandex.ru
 * on 02.03.16.
 */
public class NotificationsManager {
    private static final int CHECK_INTERVAL_SEC = 3;

    ArrayList<AdNotification> notificationsQueue = new ArrayList<>();

    Handler handler = new Handler(Looper.getMainLooper());
    final Runnable r = new Runnable() {
        public void run() {
            showOccurNotification();
            handler.postDelayed(this, CHECK_INTERVAL_SEC * 1000);
        }
    };

    public NotificationsManager(ArrayList<AdNotification> notificationsQueue) {
        this.notificationsQueue = notificationsQueue;
        handler.postDelayed(r, 1000);
    }

    public void addNotificationInQueue(AdNotification notification) {
        notificationsQueue.add(notification);
        SharedPrefsManager.getInstance().saveNotificationsQueue(notificationsQueue);
    }

    private void showOccurNotification() {
        for (int i = notificationsQueue.size() - 1; i >= 0; --i) {
            AdNotification notification = notificationsQueue.get(i);
            if (notification.getShowTime() < System.currentTimeMillis()) {//event  occured
                notificationsQueue.remove(i);
                SharedPrefsManager.getInstance().saveNotificationsQueue(notificationsQueue);
                if (System.currentTimeMillis() - notification.getShowTime() < (CHECK_INTERVAL_SEC + 10) * 1000) {//not too old
                    showNotification(notification);
                }
            }
        }
    }


    private void showNotification(AdNotification notification) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(AdApp.getInstance())
                .setSmallIcon(notification.getIcon())
                .setContentTitle(notification.getTitle())
                .setContentText(notification.getDescriprion())
                .setAutoCancel(true);

        Intent notificationIntent = IntentCreator.create(notification.getAction());

        PendingIntent pi = PendingIntent.getActivity(AdApp.getInstance(), 0, notificationIntent, PendingIntent.FLAG_ONE_SHOT);
        mBuilder.setContentIntent(pi);
        NotificationManager mNotificationManager =
                (NotificationManager) AdApp.getInstance().getSystemService(Context.NOTIFICATION_SERVICE);

        //TODO implement id's
        int notificationId = Math.abs(new Random().nextInt());
        mNotificationManager.notify(notificationId, mBuilder.build());
    }
}
