package com.example.pankinmichail.myapplication.Service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import com.example.pankinmichail.myapplication.Common;
import com.example.pankinmichail.myapplication.Models.AdAlert;
import com.example.pankinmichail.myapplication.Models.AdNotification;
import com.example.pankinmichail.myapplication.Models.AdShortcut;
import com.google.gson.Gson;

import java.lang.ref.WeakReference;
import java.util.LinkedList;

/**
 * Created by Mikhail Pankin
 * mikhail.p-n@yandex.ru
 * on 02.03.16.
 */
public class AdServiceManager {
    private Context context;

    private Messenger outgoingMessenger;
    private Messenger incomingMessenger;
    private boolean isServiceBound;
    private ServiceConnectionListener serviceConnectionListener;
    private LinkedList<Message> messageQueue = new LinkedList<>();
    private ServiceConnection connection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder binder) {
            outgoingMessenger = new Messenger(binder);
            isServiceBound = true;

            incomingMessenger = new Messenger(new IncomingHandler(AdServiceManager.this));

            Message helloMsg = Message.obtain(null, AdServiceMessages.HELLO, 0, 0);
            helloMsg.replyTo = incomingMessenger;

            try {
                outgoingMessenger.send(helloMsg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            if (serviceConnectionListener != null) serviceConnectionListener.onServiceConnected();

            for (int i = 0; i < messageQueue.size(); i++) {
                Message message = messageQueue.poll();
                try {
                    outgoingMessenger.send(message);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            outgoingMessenger = null;
            incomingMessenger = null;
            isServiceBound = false;
        }
    };

    public AdServiceManager(Context context) {
        this.context = context;
        bindToService();
    }

    public void addNotification(AdNotification notification) {
        Message addNotificationMsg = Message.obtain(null, AdServiceMessages.ADD_NOTIFICATION, 0, 0);

        Bundle bundle = new Bundle();
        //TODO make with Parcel
        bundle.putString("notification", new Gson().toJson(notification));
        addNotificationMsg.setData(bundle);
        sendMessage(addNotificationMsg);
    }

    public void addAlert(AdAlert alert) {
        Message addAlertnMsg = Message.obtain(null, AdServiceMessages.ADD_ALERT, 0, 0);

        Bundle bundle = new Bundle();
        //TODO make with Parcel
        bundle.putString("alert", new Gson().toJson(alert));
        addAlertnMsg.setData(bundle);
        sendMessage(addAlertnMsg);
    }

    public void addShortcut(AdShortcut shortcut) {
        Message addShortcut = Message.obtain(null, AdServiceMessages.ADD_SHORTCUT, 0, 0);

        Bundle bundle = new Bundle();
        //TODO make with Parcel
        bundle.putString("shortcut", new Gson().toJson(shortcut));
        addShortcut.setData(bundle);
        sendMessage(addShortcut);

    }

    private void bindToService() {
        Intent intent = new Intent(context, AdService.class);
        context.startService(intent);
        if (Common.isAdServiceRunning(context, AdService.class)) {
            context.bindService(intent, connection, Context.BIND_IMPORTANT);
        } else {
            context.bindService(intent, connection, Context.BIND_AUTO_CREATE | Context.BIND_IMPORTANT);
        }
    }

    private boolean sendMessageWithId(int msgType) {
        Message msg = Message.obtain(null, msgType, 0, 0);
        sendMessage(msg);
        return true;
    }

    private void sendMessage(Message msg) {
        try {
            messageQueue.offer(msg);
            for (int i = 0; i < messageQueue.size(); i++) {
                Message message = messageQueue.poll();
                outgoingMessenger.send(message);
            }
        } catch (Exception e) {
            // trying to rebind, then msg should be send one more time
            messageQueue.offer(msg);
            bindToService();
        }
    }

    private boolean isOutgoingMessengerBound() {
        return outgoingMessenger != null && outgoingMessenger.getBinder().isBinderAlive();
    }

    public boolean isConnected() {
        return isServiceBound;
    }

    public void setServiceConnectionListener(ServiceConnectionListener serviceConnectionListener) {
        this.serviceConnectionListener = serviceConnectionListener;
    }

    public void disconnect() {
        if (isServiceBound) {
            context.unbindService(connection);
            isServiceBound = false;
        }
    }

    public interface ServiceConnectionListener {
        void onServiceConnected();
    }

    static class IncomingHandler extends Handler {

        private final WeakReference<AdServiceManager> managerRef;

        public IncomingHandler(AdServiceManager managerRef) {
            this.managerRef = new WeakReference<>(managerRef);
        }

        @Override
        public void handleMessage(Message msg) {
            AdServiceManager a = managerRef.get();
            if (a == null) return;
            switch (msg.what) {
                case AdServiceMessages.HELLO:
                    break;

                default:
                    super.handleMessage(msg);
            }
        }
    }
}


