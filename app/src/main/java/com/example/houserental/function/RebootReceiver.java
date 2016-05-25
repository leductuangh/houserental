package com.example.houserental.function;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import com.example.houserental.function.model.DAOManager;
import com.example.houserental.function.model.SettingDAO;

import java.util.Calendar;

import core.util.Constant;

/**
 * Created by Tyrael on 5/25/16.
 */
public class RebootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            SettingDAO setting = DAOManager.getSetting();
            if (setting != null && setting.isNotification()) {
                // Set the alarm here
                AlarmManager manager = (AlarmManager) HouseRentalApplication.getContext().getSystemService(Context.ALARM_SERVICE);
                Calendar afternoonSection = Calendar.getInstance();
                afternoonSection.setTimeInMillis(System.currentTimeMillis());
                afternoonSection.set(Calendar.HOUR_OF_DAY, 17);

                Calendar morningSection = Calendar.getInstance();
                morningSection.setTimeInMillis(System.currentTimeMillis());
                morningSection.set(Calendar.HOUR_OF_DAY, 9);

                Intent i = new Intent(context, ReminderReceiver.class);
                i.setAction(Constant.REMINDER_ACTION);
                PendingIntent pIn = PendingIntent.getBroadcast(HouseRentalApplication.getContext(), 999, i, PendingIntent.FLAG_UPDATE_CURRENT);
                ComponentName alarmReceiver = new ComponentName(context, ReminderReceiver.class);
                PackageManager pm = context.getPackageManager();
                pm.setComponentEnabledSetting(alarmReceiver,
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP);
                manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, afternoonSection.getTimeInMillis(),
                        AlarmManager.INTERVAL_DAY, pIn);
                manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, morningSection.getTimeInMillis(),
                        AlarmManager.INTERVAL_DAY, pIn);
            }
        }
    }
}
