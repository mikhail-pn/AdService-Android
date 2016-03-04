package com.example.pankinmichail.myapplication;

import android.app.ActivityManager;
import android.content.Context;

/**
 * Created by Mikhail Pankin
 * mikhail.p-n@yandex.ru
 * on 02.03.16.
 */
public class Common {

	public static boolean isAdServiceRunning(Context context, Class<?> serviceClass) {
		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if (serviceClass.getName().equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}
}
