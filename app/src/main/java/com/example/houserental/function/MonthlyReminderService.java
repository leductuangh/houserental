package com.example.houserental.function;

import android.app.IntentService;
import android.content.Intent;

import java.util.Calendar;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

import core.data.DataSaver;
import core.util.notification.NotificationController;

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

    public MonthlyReminderService() {
        super(MonthlyReminderService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        TimeZone.setDefault(new SimpleTimeZone(7, "Asia/Bangkok"));
        Calendar now = Calendar.getInstance();
        if (now.get(Calendar.DAY_OF_MONTH) == 28 || now.get(Calendar.DAY_OF_MONTH) == 29 || now.get(Calendar.DAY_OF_MONTH) == 30 || now.get(Calendar.DAY_OF_MONTH) == 31) {
            NotificationController.sendNotification(DataSaver.getInstance().nextNotificationId(), "Title", "message", new Intent(HouseRentalApplication.getContext(), MainActivity.class));
        }
    }
}
