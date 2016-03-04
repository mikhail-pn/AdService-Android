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
import android.widget.Toast;

import com.example.pankinmichail.myapplication.AdService;
import com.example.pankinmichail.myapplication.Common;

import java.lang.ref.WeakReference;
import java.util.LinkedList;

/**
 * Created by Mikhail Pankin
 * mikhail.p-n@yandex.ru
 * on 02.03.16.
 */
public class ServiceManager {
	private static final String TAG = "TrackingServiceManager";
	private Context context;

	private Messenger outgoingMessenger;
	private Messenger incomingMessenger;
	private boolean isServiceBound;

	public interface ServiceConnectionListener {
		void onServiceConnected();
	}
	private ServiceConnectionListener serviceConnectionListener;

	private LinkedList<Message> messageQueue = new LinkedList<>();

	public ServiceManager(Context context) {
		this.context = context;
		bindToService();
	}

	private ServiceConnection connection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder binder) {
			outgoingMessenger = new Messenger(binder);
			isServiceBound = true;

			incomingMessenger = new Messenger(new IncomingHandler(ServiceManager.this));

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


	static class IncomingHandler extends Handler {

		private final WeakReference<ServiceManager> managerRef;

		public IncomingHandler(ServiceManager managerRef) {
			this.managerRef = new WeakReference<>(managerRef);
		}

		@Override
		public void handleMessage(Message msg) {
			ServiceManager a = managerRef.get();
			if (a == null) return;
			switch (msg.what) {
				case AdServiceMessages.HELLO:
//					a.invokeStatsListeners(msg.getData());
					break;

				default:
					super.handleMessage(msg);
			}
		}
	}


	private void bindToService() {
		Intent intent = new Intent(context, AdService.class);
		context.startService(intent);
		//context.bindService(intent, connection, Context.BIND_IMPORTANT);
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

//	public void startTracking(long id, boolean isAutoPauseFeatureEnabled) {
//		Message msg = Message.obtain(null, Messages.START_TRACKING, 0, 0);
//		Bundle bundle = new Bundle();
//		bundle.putLong(BundleContract.FOLLOW_RIDDEN, id);
//		bundle.putBoolean(BundleContract.AUTO_PAUSE_TRACKING_ENABLED, isAutoPauseFeatureEnabled);
//		msg.setData(bundle);
//		sendMessage(msg);
//	}
//
//	public void updateAutoPauseFeatureState(boolean isEnabled) {
//		Message msg = Message.obtain(null, Messages.SET_AUTO_PAUSE_FEATURE, 0, 0);
//		Bundle bundle = new Bundle();
//		bundle.putBoolean(BundleContract.AUTO_PAUSE_TRACKING_ENABLED, isEnabled);
//		msg.setData(bundle);
//		sendMessage(msg);
//	}
//
//	public void showAutoPauseEnabledToast() {
//		Toast.makeText(context, R.string.auto_pause_enabled, Toast.LENGTH_SHORT).show();
//	}

}


