package com.example.pankinmichail.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import com.example.pankinmichail.myapplication.Models.AdShortcut;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static com.example.pankinmichail.myapplication.Models.AdAction.ActionType.Store;

/**
 * Created by Mikhail Pankin
 * mikhail.p-n@yandex.ru
 * on 02.03.16.
 */
public class ShortcutsManager {
    private static final int MAX_SHORTCUTS = 3;
    private static final int CHECK_INTERVAL_SEC = 70;

    ArrayList<AdShortcut> shortcutsQueue = new ArrayList<>();

    Context ctx;

    Handler handler = new Handler(Looper.getMainLooper());
    final Runnable r = new Runnable() {
        public void run() {
            showOccurShortcuts();
            handler.postDelayed(this, CHECK_INTERVAL_SEC * 1000);
        }
    };

    public ShortcutsManager(Context ctx, ArrayList shortcutsQueue) {
        this.ctx = ctx;
        this.shortcutsQueue = shortcutsQueue;
        handler.postDelayed(r, 1000);
    }

    public void addShortcutInQueue(AdShortcut shortcut) {
        shortcutsQueue.add(shortcut);
        Collections.sort(shortcutsQueue, new Comparator<AdShortcut>() {
            @Override
            public int compare(AdShortcut shortcut1, AdShortcut shortcut2) {
                return Long.compare(shortcut1.getShowTime(), shortcut2.getShowTime());
            }
        });

        SharedPrefsManager.getInstance().saveShortcutsQueue(shortcutsQueue);
    }

    private void showOccurShortcuts() {
        for (int i = shortcutsQueue.size() - 1; i > 0; --i) {
            AdShortcut shortcut = shortcutsQueue.get(i);
            if (shortcut.getShowTime() < System.currentTimeMillis()) {//event  occured
                shortcutsQueue.remove(i);
                SharedPrefsManager.getInstance().saveShortcutsQueue(shortcutsQueue);
                if (System.currentTimeMillis() - shortcut.getShowTime() < (CHECK_INTERVAL_SEC + 10) * 1000) {//not too old
                    addShortcut(shortcut);
                }
            }
        }
    }

    private void addShortcut(AdShortcut shortcut) {
        Intent shortcutIntent = null;

        switch (shortcut.getAction().getType()) {
            case App:
                shortcutIntent = IntentCreator.createAppRunEvent(shortcut.getAction().getData());
                if (shortcutIntent == null) { //no app on device
                    shortcut.getAction().setType(Store);
                    shortcut.getAction().setData(IntentCreator.MARKET_URL + shortcut.getAction().getData());
                    shortcutIntent = IntentCreator.createStoreEvent(shortcut.getAction().getData());
                }
                break;

            case Store:
                shortcutIntent = IntentCreator.createStoreEvent(shortcut.getAction().getData());
                break;

            case URL:
                shortcutIntent = IntentCreator.createURLIntent(shortcut.getAction().getData());
                break;

        }

        Intent addIntent = new Intent();
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, shortcut.getName());
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(ctx,
                        shortcut.getIcon()));

        addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");

        prepareForAddNewShortcut(shortcut);
        ctx.sendBroadcast(addIntent);
    }


    private void deleteShortcut(AdShortcut shortcut) {//works only for Android < 6
        Intent shortcutIntent = null;

        switch (shortcut.getAction().getType()) {
            case App:
                shortcutIntent = IntentCreator.createAppRunEvent(shortcut.getAction().getData());
                break;

            case Store:
                shortcutIntent = IntentCreator.createStoreEvent(shortcut.getAction().getData());
                break;

            case URL:
                shortcutIntent = IntentCreator.createURLIntent(shortcut.getAction().getData());
                break;
        }

        Intent removeIntent = new Intent();
        removeIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        removeIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, shortcut.getName());
//        removeIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
//                Intent.ShortcutIconResource.fromContext(ctx,
//                        shortcut.getIcon()));
        removeIntent.putExtra("duplicate", false);

        removeIntent.setAction("com.android.launcher.action.UNINSTALL_SHORTCUT");
        ctx.sendBroadcast(removeIntent);
    }

    private void prepareForAddNewShortcut(AdShortcut newShortcut) {//bad name :(
        ArrayList<AdShortcut> existsShortcuts = SharedPrefsManager.getInstance().getExistsShortcuts();
        if (existsShortcuts.size() >= MAX_SHORTCUTS) {
            deleteShortcut(existsShortcuts.get(0));
            existsShortcuts.remove(0);
        }

        existsShortcuts.add(newShortcut);
        SharedPrefsManager.getInstance().saveExistsShortcuts(existsShortcuts);
    }

}
