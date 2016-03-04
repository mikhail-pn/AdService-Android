package com.example.pankinmichail.myapplication;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.pankinmichail.myapplication.Models.AdAlert;
import com.example.pankinmichail.myapplication.Models.AdNotification;
import com.example.pankinmichail.myapplication.Models.AdShortcut;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Mikhail Pankin
 * mikhail.p-n@yandex.ru
 * on 03.03.16.
 */
public class SharedPrefsManager {
    private static SharedPrefsManager instance;

    private SharedPreferences shPrefs;
    private Gson gson = new Gson();

    private SharedPrefsManager() {
        shPrefs = AdApp.getInstance().getSharedPreferences(AdApp.class.getName(), Context.MODE_PRIVATE);
    }

    public static SharedPrefsManager getInstance() {
        if (instance == null) {
            synchronized (SharedPrefsManager.class) {
                instance = new SharedPrefsManager();
            }
        }

        return instance;
    }

    public ArrayList<AdShortcut> getExistsShortcuts() {
        Set<String> rawShortcuts = shPrefs.getStringSet("existsQhortcuts", new HashSet<String>());
        ArrayList<AdShortcut> shortcuts = new ArrayList<>();
        for (String shortcut : rawShortcuts) {
            shortcuts.add(0, gson.fromJson(shortcut, AdShortcut.class));
        }

        return shortcuts;
    }

    public void saveExistsShortcuts(ArrayList<AdShortcut> shortcuts) {
        ArrayList<String> rawShortcuts = new ArrayList<>();
        for(AdShortcut shortcut : shortcuts) {
            rawShortcuts.add(gson.toJson(shortcut));
        }

        shPrefs.edit().putStringSet("existsQhortcuts", new HashSet<String>(rawShortcuts)).commit();
    }

    public ArrayList<AdShortcut> getShortcutsQueue() {
        Set<String> rawShortcuts = shPrefs.getStringSet("shortcuts", new HashSet<String>());
        ArrayList<AdShortcut> shortcuts = new ArrayList<>();
        for (String shortcut : rawShortcuts) {
            shortcuts.add(0, gson.fromJson(shortcut, AdShortcut.class));
        }

        return shortcuts;
    }

    public void saveShortcutsQueue(ArrayList<AdShortcut> shortcuts) {
        ArrayList<String> rawShortcuts = new ArrayList<>();
        for(AdShortcut shortcut : shortcuts) {
            rawShortcuts.add(gson.toJson(shortcut));
        }

        shPrefs.edit().putStringSet("shortcuts", new HashSet<String>(rawShortcuts)).commit();
    }

    public void saveAlertsQueue(ArrayList<AdAlert> alerts) {
        ArrayList<String> rawAlerts = new ArrayList<>();
        for(AdAlert alert : alerts) {
            rawAlerts.add(gson.toJson(alert));
        }

        shPrefs.edit().putStringSet("alerts", new HashSet<String>(rawAlerts)).commit();
    }

    public ArrayList<AdAlert> getAlertsQueue() {
        Set<String> rawAlerts = shPrefs.getStringSet("alerts", new HashSet<String>());
        ArrayList<AdAlert> alerts = new ArrayList<>();
        for (String alert : rawAlerts) {
            alerts.add(0, gson.fromJson(alert, AdAlert.class));
        }

        return alerts;
    }


    public void saveNotificationsQueue(ArrayList<AdNotification> notifications) {
        ArrayList<String> rawNotifications = new ArrayList<>();
        for(AdNotification notification : notifications) {
            rawNotifications.add(gson.toJson(notification));
        }

        shPrefs.edit().putStringSet("notifications", new HashSet<String>(rawNotifications)).commit();
    }

    public ArrayList<AdNotification> getNotificationsQueue() {
        Set<String> rawAlerts = shPrefs.getStringSet("notifications", new HashSet<String>());
        ArrayList<AdNotification> alerts = new ArrayList<>();
        for (String shortcut : rawAlerts) {
            alerts.add(0, gson.fromJson(shortcut, AdNotification.class));
        }

        return alerts;
    }
}
