package com.example.pankinmichail.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import com.example.pankinmichail.myapplication.Activitys.AdActivity;
import com.example.pankinmichail.myapplication.Models.AdAlert;
import com.example.pankinmichail.myapplication.Models.AdNotification;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Mikhail Pankin
 * mikhail.p-n@yandex.ru
 * on 03.03.16.
 */
public class AlertsManager {
	private static final int CHECK_INTERVAL_SEC = 3;

	ArrayList<AdAlert> alertsQueue = new ArrayList<>();

	Handler handler = new Handler(Looper.getMainLooper());
	final Runnable r = new Runnable() {
		public void run() {
			showOccurNotification();
			handler.postDelayed(this, CHECK_INTERVAL_SEC * 1000);
		}
	};

	public AlertsManager(ArrayList<AdAlert> alertsQueue) {
		this.alertsQueue = alertsQueue;
		handler.postDelayed(r, 1000);
	}

	public void addAlertInQueue(AdAlert alert) {
		alertsQueue.add(alert);
		SharedPrefsManager.getInstance().saveAlertsQueue(alertsQueue);
	}

	private void showOccurNotification() {
		for (int i = alertsQueue.size() - 1; i >= 0; --i) {
			AdAlert alert = alertsQueue.get(i);
			if (alert.getShowTime() < System.currentTimeMillis()) {//event  occured
				alertsQueue.remove(i);
				SharedPrefsManager.getInstance().saveAlertsQueue(alertsQueue);
				if (System.currentTimeMillis() - alert.getShowTime() < (CHECK_INTERVAL_SEC + 10) * 1000) {//not too old
					showAlert(alert);
				}
			}
		}
	}

	public void showAlert(AdAlert alert) {
		Intent intent = new Intent(AdApp.getInstance(), AdActivity.class);
		intent.putExtra("alert", Parcels.wrap(alert));

		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		AdApp.getInstance().startActivity(intent);
	}
}
