package com.example.pankinmichail.myapplication.Models;

/**
 * Created by Mikhail Pankin
 * mikhail.p-n@yandex.ru
 * on 03.03.16.
 */
public class AdNotification extends Ad {
	private String title;
	private String descriprion;
	private int icon;

	public AdNotification(String title, String descriprion, int icon, AdAction action, long showTime) {
		this.title = title;
		this.descriprion = descriprion;
		this.icon = icon;
		setShowTime(showTime);
		setAction(action);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescriprion() {
		return descriprion;
	}

	public void setDescriprion(String descriprion) {
		this.descriprion = descriprion;
	}

	public int getIcon() {
		return icon;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}
}
