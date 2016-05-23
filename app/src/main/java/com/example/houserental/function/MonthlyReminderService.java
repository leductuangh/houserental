package com.example.houserental.function;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by leductuan on 5/23/16.
 */
public class MonthlyReminderService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public MonthlyReminderService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }
}
