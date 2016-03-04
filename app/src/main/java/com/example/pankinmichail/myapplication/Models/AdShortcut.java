package com.example.pankinmichail.myapplication.Models;

/**
 * Created by Mikhail Pankin
 * mikhail.p-n@yandex.ru
 * on 03.03.16.
 */
public class AdShortcut extends Ad {
    public static final long IMMORTAL = -1;

    private String name;
    private int icon;
    private long deathMoment = IMMORTAL;

    public AdShortcut(String name, int icon, AdAction action, long showTime) {
        this.name = name;
        this.icon = icon;
        setShowTime(showTime);
        setAction(action);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
