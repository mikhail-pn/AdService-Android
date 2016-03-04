package com.example.pankinmichail.myapplication.Models;

import org.parceler.Parcel;

/**
 * Created by Mikhail Pankin
 * mikhail.p-n@yandex.ru
 * on 03.03.16.
 */
@Parcel
public class Ad {
	AdAction action;
	long showTime;

	public long getShowTime() {
		return showTime;
	}

	public void setShowTime(long showTime) {
		this.showTime = showTime;
	}

	public AdAction getAction() {
		return action;
	}

	public void setAction(AdAction action) {
		this.action = action;
	}
}
