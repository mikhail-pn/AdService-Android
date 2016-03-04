package com.example.pankinmichail.myapplication;

import android.app.Application;

/**
 * Created by Mikhail Pankin
 * mikhail.p-n@yandex.ru
 * on 03.03.16.
 */
public class AdApp extends Application {
    private static AdApp instance;

    public static AdApp getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
