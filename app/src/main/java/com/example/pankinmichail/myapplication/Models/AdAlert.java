package com.example.pankinmichail.myapplication.Models;

import org.parceler.Parcel;

/**
 * Created by Mikhail Pankin
 * mikhail.p-n@yandex.ru
 * on 03.03.16.
 */
@Parcel
public class AdAlert extends Ad {
	String title;
	String description;

	public AdAlert() {//for parcel lib

	}

	public AdAlert(AdAction action, String description, String title, long showTime) {
		this.title = title;
		this.description = description;
		setShowTime(showTime);
		setAction(action);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
