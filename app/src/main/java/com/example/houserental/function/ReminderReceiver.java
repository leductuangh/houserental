package com.example.houserental.function;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

import core.data.DataSaver;
import core.util.Constant;
import core.util.DLog;
import core.util.notification.NotificationController;

/**
 * Created by leductuan on 5/23/16.
 */
public class ReminderReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.getAction().equals(Constant.REMINDER_ACTION)) {
            TimeZone.setDefault(new SimpleTimeZone(7, "Asia/Bangkok"));
            Calendar now = Calendar.getInstance();
            if (now.get(Calendar.DAY_OF_MONTH) == 28 || now.get(Calendar.DAY_OF_MONTH) == 29 || now.get(Calendar.DAY_OF_MONTH) == 30 || now.get(Calendar.DAY_OF_MONTH) == 31) {
                NotificationController.sendNotification(DataSaver.getInstance().nextNotificationId(), "Title", "message", new Intent(HouseRentalApplication.getContext(), MainActivity.class));
            }
            DLog.d("ReminderReceiver", "receive broadcast");
        }
    }
}
