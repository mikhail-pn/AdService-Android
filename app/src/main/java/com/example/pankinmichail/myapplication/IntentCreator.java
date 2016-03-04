package com.example.pankinmichail.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.example.pankinmichail.myapplication.Models.Ad;
import com.example.pankinmichail.myapplication.Models.AdAction;
import com.example.pankinmichail.myapplication.Models.AdNotification;

/**
 * Created by Mikhail Pankin
 * mikhail.p-n@yandex.ru
 * on 03.03.16.
 */
public class IntentCreator {
    public static final String MARKET_URL = "market://details?id=";

    public static Intent create(AdAction action) {
        switch (action.getType()) {
            case App:
                Intent intent = IntentCreator.createAppRunEvent(action.getData());
                if (intent == null) { //no app on device
                    return IntentCreator.createStoreEvent(action.getData());
                }

                return intent;

            case Store:
                return IntentCreator.createStoreEvent(action.getData());

            case URL:
                return IntentCreator.createURLIntent(action.getData());
        }

        return null;
    }

    public static Intent createAppRunEvent(String packageName) {
        Intent intent = AdApp.getInstance().getPackageManager().getLaunchIntentForPackage(packageName);

        return intent;
    }

    public static Intent createStoreEvent(String packageName) {
        return new Intent(Intent.ACTION_VIEW, Uri.parse(MARKET_URL + packageName));
    }

    public static Intent createURLIntent(String url) {
        return new Intent(Intent.ACTION_VIEW, Uri.parse(url));
    }
}
