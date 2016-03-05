package com.example.pankinmichail.myapplication.Service;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.example.pankinmichail.myapplication.AlertsManager;
import com.example.pankinmichail.myapplication.Models.AdAlert;
import com.example.pankinmichail.myapplication.Models.AdNotification;
import com.example.pankinmichail.myapplication.Models.AdShortcut;
import com.example.pankinmichail.myapplication.NotificationsManager;
import com.example.pankinmichail.myapplication.SharedPrefsManager;
import com.example.pankinmichail.myapplication.ShortcutsManager;
import com.google.gson.Gson;

import org.parceler.Parcels;

import java.lang.ref.WeakReference;

/**
 * Created by Mikhail Pankin
 * mikhail.p-n@yandex.ru
 * on 03.03.16.
 */
public class AdService extends android.app.Service {
    private final static String TAG = "AdService";

    private Messenger incomingMessenger = new Messenger(new IncomingHandler(this));
    private Messenger outgoingMessenger;

    private ShortcutsManager shortcutsManager;
    private NotificationsManager notificationsManager;
    private AlertsManager alertsManager;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        shortcutsManager = new ShortcutsManager(this, SharedPrefsManager.getInstance().getExistsShortcuts());
        notificationsManager = new NotificationsManager(SharedPrefsManager.getInstance().getNotificationsQueue());
        alertsManager = new AlertsManager(SharedPrefsManager.getInstance().getAlertsQueue());

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return incomingMessenger.getBinder();
    }

    private void replyMessage(int msgType, Bundle bundle) {
        if (!isOutgoingMessengerBound()) return;
        Message msg = Message.obtain(null, msgType, 0, 0);
        msg.setData(bundle);

        try {
            outgoingMessenger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private boolean isOutgoingMessengerBound() {
        return outgoingMessenger != null && outgoingMessenger.getBinder().isBinderAlive()
                && outgoingMessenger.getBinder().pingBinder();
    }


    private void setOutgoingMessenger(Message msg) {
        outgoingMessenger = msg.replyTo;
    }

    static class IncomingHandler extends Handler {
        private final WeakReference<AdService> service;

        public IncomingHandler(AdService service) {
            this.service = new WeakReference<>(service);
        }

        @Override
        public void handleMessage(Message msg) {
            AdService s = service.get();
            switch (msg.what) {
                case AdServiceMessages.HELLO:
                    Log.d(TAG, "Service receive HELLO message");
                    s.setOutgoingMessenger(msg);
                    break;

                case AdServiceMessages.ADD_ALERT:
                    Log.d(TAG, "Service receive ADD_ALERT message");
                    //TODO make with Parcel
                    AdAlert newAlert = new Gson().fromJson(msg.getData().getString("alert"), AdAlert.class);
                    s.alertsManager.addAlertInQueue(newAlert);
                    break;

                case AdServiceMessages.ADD_SHORTCUT:
                    Log.d(TAG, "Service receive ADD_SHORTCUT message");
                    //TODO make with Parcel
                    AdShortcut newShortcut = new Gson().fromJson(msg.getData().getString("shortcut"), AdShortcut.class);
                    s.shortcutsManager.addShortcutInQueue(newShortcut);
                    break;

                case AdServiceMessages.ADD_NOTIFICATION:
                    Log.d(TAG, "Service receive ADD_NOTIFICATION message");
                    //TODO make with Parcel
                    AdNotification newNotification = new Gson().fromJson(msg.getData().getString("notification"), AdNotification.class);
                    s.notificationsManager.addNotificationInQueue(newNotification);
                    break;

                default:
                    super.handleMessage(msg);
            }
        }
    }
}
