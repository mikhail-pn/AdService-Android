package com.example.pankinmichail.myapplication.Models;

import org.parceler.Parcel;

/**
 * Created by Mikhail Pankin
 * mikhail.p-n@yandex.ru
 * on 02.03.16.
 */
@Parcel
public class AdAction {
	public enum ActionType {URL, Store, App}
	ActionType type;
	String data;

	public AdAction() {//for parcel lib

	}

	public AdAction(ActionType type, String data) {
		this.type = type;
		this.data = data;
	}

	public ActionType getType() {
		return type;
	}

	public void setType(ActionType type) {
		this.type = type;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
}
